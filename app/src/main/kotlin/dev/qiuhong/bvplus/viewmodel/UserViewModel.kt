package dev.qiuhong.bvplus.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.qiuhong.biliapi.BiliApi
import dev.qiuhong.biliapi.entity.AuthFailureException
import dev.qiuhong.biliapi.entity.user.MyInfoData
import dev.qiuhong.bvplus.BVApp
import dev.qiuhong.bvplus.BuildConfig
import dev.qiuhong.bvplus.R
import dev.qiuhong.bvplus.repository.UserRepository
import dev.qiuhong.bvplus.util.Prefs
import dev.qiuhong.bvplus.util.fInfo
import dev.qiuhong.bvplus.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mu.KotlinLogging

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private var shouldUpdateInfo = true
    private val logger = KotlinLogging.logger { }
    val isLogin get() = userRepository.isLogin
    val username get() = userRepository.username
    val face get() = userRepository.face

    var responseData: MyInfoData? by mutableStateOf(null)

    fun updateUserInfo() {
        if (!shouldUpdateInfo || !userRepository.isLogin) return
        logger.fInfo { "Update user info" }
        viewModelScope.launch {
            runCatching {
                responseData = BiliApi.getUserSelfInfo(sessData = Prefs.sessData).getResponseData()
                logger.fInfo { "Update user info success" }
                shouldUpdateInfo = false
                userRepository.username = responseData!!.name
                userRepository.face = responseData!!.face
            }.onFailure {
                when (it) {
                    is AuthFailureException -> {
                        withContext(Dispatchers.Main) {
                            BVApp.context.getString(R.string.exception_auth_failure)
                                .toast(BVApp.context)
                        }
                        logger.fInfo { "User auth failure" }
                        if (!BuildConfig.DEBUG) userRepository.logout()
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            "获取用户信息失败：${it.message}".toast(BVApp.context)
                        }
                    }
                }
            }
        }
    }

    fun logout() {
        userRepository.logout()
    }

    fun clearUserInfo() {
        userRepository.username = ""
        userRepository.face = ""
    }
}