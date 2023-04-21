package dev.qiuhong.bvplus.viewmodel.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dev.qiuhong.biliapi.BiliApi
import dev.qiuhong.biliapi.entity.AuthFailureException
import dev.qiuhong.biliapi.entity.dynamic.DynamicItem
import dev.qiuhong.bvplus.BVApp
import dev.qiuhong.bvplus.BuildConfig
import dev.qiuhong.bvplus.R
import dev.qiuhong.bvplus.repository.UserRepository
import dev.qiuhong.bvplus.util.Prefs
import dev.qiuhong.bvplus.util.fInfo
import dev.qiuhong.bvplus.util.fWarn
import dev.qiuhong.bvplus.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mu.KotlinLogging

class DynamicViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val logger = KotlinLogging.logger {}
    val dynamicList = mutableStateListOf<DynamicItem>()

    private var currentPage = 0
    var loading = false
    var hasMore = true
    private var offset: String? = null
    val isLogin get() = userRepository.isLogin

    suspend fun loadMore() {
        if (!loading) loadData()
    }

    private suspend fun loadData() {
        if (!hasMore || !userRepository.isLogin) return
        loading = true
        logger.fInfo { "Load more dynamic videos" }
        runCatching {
            val responseData = runBlocking {
                BiliApi.getDynamicList(
                    page = ++currentPage,
                    type = "video",
                    offset = offset,
                    sessData = Prefs.sessData
                ).getResponseData()
            }
            dynamicList.addAll(responseData.items)
            offset = responseData.offset

            logger.fInfo { "Load dynamic list page: ${currentPage},size: ${responseData.items.size}" }
            val avList = responseData.items.map {
                it.modules.moduleDynamic.major!!.archive!!.aid
            }
            logger.fInfo { "Load dynamic size: ${avList.size}" }
            logger.info { "Load dynamic list ${avList}}" }

            hasMore = responseData.hasMore
        }.onFailure {
            logger.fWarn { "Load dynamic list failed: ${it.stackTraceToString()}" }
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
                        "加载动态失败: ${it.localizedMessage}".toast(BVApp.context)
                    }
                }
            }
        }
        loading = false
    }

    fun clearData() {
        dynamicList.clear()
        currentPage = 0
        loading = false
        hasMore = true
        offset = null
    }
}