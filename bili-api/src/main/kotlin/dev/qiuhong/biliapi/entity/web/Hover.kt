package dev.qiuhong.biliapi.entity.web

import kotlinx.serialization.Serializable

@Serializable
data class Hover(
    val text: List<String>,
    val img: String
)