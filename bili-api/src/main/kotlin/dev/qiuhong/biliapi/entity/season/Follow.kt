package dev.qiuhong.biliapi.entity.season

import kotlinx.serialization.Serializable

@Serializable
data class SeasonFollowData(
    val fmid: Int,
    val relation: Boolean,
    val status: Int,
    val toast: String
)