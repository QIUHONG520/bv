package dev.qiuhong.bvplus.viewmodel.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dev.qiuhong.biliapi.BiliApi
import dev.qiuhong.biliapi.entity.video.VideoInfo
import dev.qiuhong.bvplus.BVApp
import dev.qiuhong.bvplus.util.Prefs
import dev.qiuhong.bvplus.util.fError
import dev.qiuhong.bvplus.util.fInfo
import dev.qiuhong.bvplus.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mu.KotlinLogging

class RcmdViewModel : ViewModel() {
    private val logger = KotlinLogging.logger {}
    val rcmdVideoList = mutableStateListOf<VideoInfo>()

    private var fresh_idx = 0
    private var fresh_type = 0
    private var pageSize = 20
    var loading = false

    suspend fun loadMore() {
        if (!loading) loadData()
    }

    private suspend fun loadData() {
        loading = true
        logger.fInfo { "Load more recommend videos" }
        runCatching {
            val responseData = runBlocking {
                BiliApi.getRcmdVideoData(
                    fresh_idx = ++fresh_idx,
                    pageSize = pageSize,
                    fresh_type = ++fresh_type,
                    sessData = Prefs.sessData
                ).getResponseData()
            }
            rcmdVideoList.addAll(responseData.item)
        }.onFailure {
            logger.fError { "Load recommend video list failed: ${it.stackTraceToString()}" }
            withContext(Dispatchers.Main) {
                "加载推荐视频失败: ${it.localizedMessage}".toast(BVApp.context)
            }
        }
        loading = false
    }

    fun clearData() {
        rcmdVideoList.clear()
        fresh_idx = 0
        fresh_type = 0
        loading = false
        pageSize = 20
    }

    fun readAllData(){

    }
}