package dev.qiuhong.bvplus.entity

import android.content.Context
import dev.qiuhong.bvplus.R

enum class VideoCodec(private val strRes: Int, val prefix: String) {
    AVC(R.string.video_codec_avc, "avc1"),
    HEVC(R.string.video_codec_hevc, "hev1"),
    AV1(R.string.video_codec_av1, "av01"),
    DVH1(R.string.video_codec_dvh1, "dvh1"),
    HVC1(R.string.video_codec_hvc1, "hvc");

    companion object {
        fun fromCode(code: Int?) = runCatching {
            values().find { it.ordinal == code }!!
        }.getOrDefault(AVC)

        fun fromCodecString(codec: String) = runCatching {
            values().forEach {
                if (codec.startsWith(it.prefix)) return@runCatching it
            }
            return@runCatching null
        }.getOrNull()
    }

    fun getDisplayName(context: Context) = context.getString(strRes)
}