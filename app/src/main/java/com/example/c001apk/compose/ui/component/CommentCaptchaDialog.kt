package com.example.c001apk.compose.ui.component

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.Keep
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.c001apk.compose.util.CommentCaptcha
import com.example.c001apk.compose.util.CookieUtil

@Keep
private class CaptchaResultBridge(private val onVerified: (String) -> Unit) {
    @JavascriptInterface
    fun verified(validate: String) = onVerified(validate)
}

/** The provider renders its own challenge; only the person using the phone completes it. */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun CommentCaptchaDialog() {
    val challenge by CommentCaptcha.challenge.collectAsState()
    val active = challenge ?: return
    val context = LocalContext.current
    val webView = remember(active) {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.userAgentString = CookieUtil.userAgent
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) = request.isForMainFrame
            }
            addJavascriptInterface(CaptchaResultBridge { value ->
                post { CommentCaptcha.complete(active, value) }
            }, "C002Verification")
            loadDataWithBaseURL("https://captcha.c002apk.invalid/", """
                <!doctype html><html lang="zh-CN"><head><meta charset="utf-8">
                <meta name="viewport" content="width=device-width,initial-scale=1">
                <style>body{font:16px sans-serif;margin:12px;background:#fff;color:#222}p{line-height:1.6}</style></head>
                <body><p id="info">正在加载验证码，请稍候…</p><div id="verification"></div>
                <script>
                function report(text){document.getElementById('info').textContent=text;}
                var deadline=setTimeout(function(){report('加载超时，请取消后重试，并检查网络。');},20000);
                </script><script src="https://cstaticdun.126.net/load.min.js" onerror="report('验证码组件加载失败，请检查网络后重试。')"></script>
                <script>
                if(typeof initNECaptcha==='function'){
                  initNECaptcha({captchaId:'${active.captchaId}',element:'#verification',protocol:'https',
                    mode:'embed',apiVersion:2,width:Math.min(320,window.innerWidth-24)+'px',lang:'zh-CN',
                    onVerify:function(error,result){
                      if(!error && result && typeof result.validate==='string'){
                        C002Verification.verified(result.validate);
                      }
                    }
                  },function(){clearTimeout(deadline);report('请按提示手动完成验证。');},
                    function(){clearTimeout(deadline);report('验证码初始化失败，请取消后重试。');});
                }else{clearTimeout(deadline);report('验证码组件不可用，请检查网络后重试。');}
                </script></body></html>
            """.trimIndent(), "text/html", "UTF-8", null)
        }
    }
    DisposableEffect(webView) {
        onDispose {
            webView.removeJavascriptInterface("C002Verification")
            webView.stopLoading()
            webView.destroy()
            CommentCaptcha.complete(active, null)
        }
    }
    Dialog(onDismissRequest = { CommentCaptcha.complete(active, null) }) {
        Surface(shape = MaterialTheme.shapes.extraLarge) {
            Column(Modifier.fillMaxWidth().padding(20.dp)) {
                Text("完成验证以查看评论", style = MaterialTheme.typography.titleLarge)
                Text("酷安要求本次访问进行人机验证，完成后将自动重试。", Modifier.padding(vertical = 12.dp))
                AndroidView(factory = { webView }, modifier = Modifier.fillMaxWidth().height(380.dp))
                TextButton(onClick = { CommentCaptcha.complete(active, null) }) { Text("取消") }
            }
        }
    }
}
