package dev.qiuhong.bvplus.entity.carddata

import dev.qiuhong.bvplus.util.formatMinSec

data class VideoCardData(
    val avid: Int,
    val title: String,
    val cover: String,
    val upName: String,
    val reason: String = "",
    val play: Int? = null,
    var playString: String = "",
    val danmaku: Int? = null,
    var danmakuString: String = "",
    val time: Long? = null,
    var timeString: String = ""
) {
    init {
        play?.let {
            playString = if (it >= 10000) "${it / 10000}万" else "$it"
        }
        danmaku?.let {
            danmakuString = if (it >= 10000) "${it / 10000}万" else "$it"
        }
        time?.let {
            timeString = it.formatMinSec()
        }
    }
}
