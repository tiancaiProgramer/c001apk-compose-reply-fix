package com.example.c001apk.compose.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.logic.repository.UserPreferencesRepository
import com.example.c001apk.compose.util.AccountLoginCredentials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Maintained by tiancaiProgramer.
 * Created by bggRGjQaUbCoE on 2024/6/10
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    var saving by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    private var saved = false

    fun finishLogin(credentials: AccountLoginCredentials) {
        if (saving || saved) return
        saving = true
        error = null
        viewModelScope.launch {
            try {
                userPreferencesRepository.saveLogin(credentials.uid, credentials.username, credentials.token)
                saved = true
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                error = "登录状态保存失败，请重新加载后重试"
            } finally {
                saving = false
            }
        }
    }
}
