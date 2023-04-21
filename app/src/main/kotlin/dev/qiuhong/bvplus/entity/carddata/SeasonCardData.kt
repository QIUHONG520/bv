package dev.qiuhong.bvplus.entity.carddata

import dev.qiuhong.biliapi.entity.search.SearchMediaResult

data class SeasonCardData(
    val seasonId: Int,
    val title: String,
    val subTitle: String? = null,
    val cover: String,
    val rating: String? = null,
    val badge: SearchMediaResult.Badge? = null,
)
