package com.example.c001apk.compose.ui.selfupdate

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.logic.update.GithubRelease
import com.example.c001apk.compose.logic.update.SelfUpdateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelfUpdateViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
    var checking by mutableStateOf(false)
        private set
    var release by mutableStateOf<GithubRelease?>(null)
        private set
    var message by mutableStateOf<String?>(null)
        private set

    fun check(automatic: Boolean = false) {
        if (checking) return
        checking = true
        viewModelScope.launch {
            try {
                release = SelfUpdateRepository.check(context, automatic)
                if (!automatic && release == null) message = "当前已是最新版本（包含已发布的预览版）"
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                if (!automatic) message = "暂时无法连接 GitHub，请检查网络后重试，或前往发布页查看。"
            } finally {
                checking = false
            }
        }
    }

    fun dismiss() {
        release = null
        message = null
    }
}
