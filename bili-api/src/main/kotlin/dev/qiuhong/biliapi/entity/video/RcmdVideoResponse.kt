package dev.qiuhong.biliapi.entity.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 推荐视频
 */
@Serializable
data class RcmdVideoData(
    val item: List<VideoInfo>,
    @SerialName("business_card")
    val business_card: String? = null,
    @SerialName("floor_info")
    val floor_info: String? = null,
    @SerialName("user_feature")
    val user_feature: String? = null,
    @SerialName("preload_expose_pct")
    val preload_expose_pct: Double,
    @SerialName("preload_floor_expose_pct")
    val preload_floor_expose_pct: Double,
    @SerialName("mid")
    val mid: Int
)