package com.example.c001apk.compose.ui.login

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.c001apk.compose.constant.Constants.APP_ID
import com.example.c001apk.compose.util.AccountLoginCredentials
import com.example.c001apk.compose.util.AccountLoginPolicy
import java.util.concurrent.atomic.AtomicBoolean

/** Official pages own the verification and SMS sequence; no credential/verification JS bridge. */
@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun AccountLoginWebView(
    userAgent: String,
    deviceId: String,
    reload: Int,
    onCredentials: (AccountLoginCredentials) -> Unit,
    onProgress: (Int) -> Unit,
    onError: (String?) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val credentialsCallback by rememberUpdatedState(onCredentials)
    val progressCallback by rememberUpdatedState(onProgress)
    val errorCallback by rememberUpdatedState(onError)
    var canGoBack by remember(reload) { mutableStateOf(false) }
    var currentUrl by rememberSaveable(reload) { mutableStateOf(AccountLoginPolicy.LOGIN_URL) }
    val disposed = remember(context, reload) { AtomicBoolean(false) }
    val webView = remember(context, reload) {
        WebView(context).apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                allowFileAccess = false
                allowContentAccess = false
                mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                javaScriptCanOpenWindowsAutomatically = false
                setSupportMultipleWindows(false)
                if (userAgent.isNotBlank()) userAgentString = userAgent
            }
            val cookies = CookieManager.getInstance()
            cookies.setAcceptCookie(true)
            cookies.setAcceptThirdPartyCookies(this, true)

            fun finishLogin(url: String, reportMissing: Boolean = false): Boolean {
                if (disposed.get()) return false
                if (!AccountLoginPolicy.isSuccessLanding(url)) return false
                val credentials = AccountLoginPolicy.credentials(cookies.getCookie("https://www.coolapk.com/"))
                    ?: AccountLoginPolicy.credentials(cookies.getCookie("https://account.coolapk.com/"))
                if (credentials == null) {
                    // A redirect's final response may still be setting the cookies.
                    if (reportMissing) errorCallback("未取得完整登录状态，请重新加载后重试")
                    return false
                }
                cookies.flush()
                credentialsCallback(credentials)
                stopLoading()
                return true
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    if (!request.isForMainFrame) return false
                    val url = request.url.toString()
                    if (!AccountLoginPolicy.allowsNavigation(url)) {
                        errorCallback("请使用手机号或酷安账号密码在此登录")
                        return true
                    }
                    return finishLogin(url)
                }

                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    if (!AccountLoginPolicy.allowsNavigation(url)) {
                        view.stopLoading()
                        errorCallback("已停止打开非酷安登录页面，请重新加载")
                        return
                    }
                    errorCallback(null)
                    if (!finishLogin(url)) currentUrl = url
                }

                override fun onPageFinished(view: WebView, url: String) {
                    canGoBack = view.canGoBack()
                    finishLogin(url, reportMissing = true)
                }

                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    if (request.isForMainFrame) errorCallback("登录页加载失败，请检查网络后重新加载")
                }

                override fun onReceivedHttpError(view: WebView, request: WebResourceRequest, response: WebResourceResponse) {
                    if (request.isForMainFrame) errorCallback("登录服务暂不可用，请稍后重新加载")
                }

                override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                    handler.cancel()
                    errorCallback("登录连接证书验证失败，请检查设备时间和网络")
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    progressCallback(newProgress)
                }
            }
            errorCallback(null)
            progressCallback(0)
            val deviceCookie = AccountLoginPolicy.deviceCookie(deviceId)
            if (deviceCookie != null) {
                cookies.setCookie("https://account.coolapk.com/", deviceCookie) {
                    if (!disposed.get()) loadUrl(currentUrl, mapOf("X-Requested-With" to APP_ID))
                }
            } else {
                loadUrl(currentUrl, mapOf("X-Requested-With" to APP_ID))
            }
        }
    }
    DisposableEffect(webView) {
        onDispose {
            disposed.set(true)
            webView.stopLoading()
            webView.webChromeClient = null
            webView.webViewClient = WebViewClient()
            webView.destroy()
        }
    }
    BackHandler {
        if (canGoBack) webView.goBack() else onBack()
    }
    key(webView) {
        AndroidView(factory = { webView }, modifier = modifier.fillMaxSize())
    }
}
