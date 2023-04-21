package dev.qiuhong.biliapi.entity.login.qr

import io.ktor.http.Cookie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class QRLoginResponse(
    val code: Int,
    val message: String,
    val ttl: Int,
    val data: QRLoginData,
    @Transient
    var cookies: List<Cookie> = emptyList()
)

@Serializable
data class QRLoginData(
    val url: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    val timestamp: Long,
    val code: Int,
    val message: String
)