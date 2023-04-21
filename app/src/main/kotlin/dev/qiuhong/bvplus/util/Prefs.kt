package dev.qiuhong.bvplus.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import de.schnettler.datastore.manager.PreferenceRequest
import dev.qiuhong.bvplus.BVApp
import dev.qiuhong.bvplus.entity.Resolution
import dev.qiuhong.bvplus.entity.VideoCodec
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.util.Date
import java.util.UUID
import kotlin.math.roundToInt

object Prefs {
    private val dsm = BVApp.dataStoreManager
    val logger = KotlinLogging.logger { }

    var isLogin: Boolean
        get() = runBlocking { dsm.getPreferenceFlow(PrefKeys.prefIsLoginRequest).first() }
        set(value) = runBlocking { dsm.editPreference(PrefKeys.prefIsLoginKey, value) }

    var uid: Long
        get() = runBlocking { dsm.getPreferenceFlow(PrefKeys.prefUidRequest).first() }
        set(value) = runBlocking { dsm.editPreference(PrefKeys.prefUidKey, value) }

    var sid: String
        get() = runBlocking { dsm.getPreferenceFlow(PrefKeys.prefSidRequest).first() }
        set(value) = runBlocking { dsm.editPreference(PrefKeys.prefSidKey, value) }

    var sessData: String
        get() = runBlocking { dsm.getPreferenceFlow(PrefKeys.prefSessDataRequest).first() }
        set(value) = runBlocking { dsm.editPreference(PrefKeys.prefSessDataKey, value) }

    var biliJct: String
        get() = runBlocking { dsm.getPreferenceFlow(PrefKeys.prefBiliJctRequest).first() }
        set(value) = runBlocking { dsm.editPreference(PrefKeys.prefBiliJctKey, value) }

    var uidCkMd5: String
        get() = runBlocking { dsm.getPreferenceFlow(PrefKeys.prefUidCkMd5Request).first() }
        set(value) = runBlocking { dsm.editPreference(PrefKeys.prefUidCkMd5Key, value) }

    var tokenExpiredData: Date
        get() = Date(runBlocking {
            dsm.getPreferenceFlow(PrefKeys.prefTokenExpiredDateRequest).first()
        })
        set(value) = runBlocking {
            dsm.editPreference(PrefKeys.prefTokenExpiredDateKey, value.time)
        }

    var defaultQuality: Int
        get() = runBlocking { dsm.getPreferenceFlow(PrefKeys.prefDefaultQualityRequest).first() }
        set(value) = runBlocking { dsm.editPreference(PrefKeys.prefDefaultQualityKey, value) }

    var defaultDanmakuSize: Int
        get() = runBlocking {
            dsm.getPreferenceFlow(PrefKeys.prefDefaultDanmakuSizeRequest).first()
        }
        set(value) = runBlocking { dsm.editPreference(PrefKeys.prefDefaultDanmakuSizeKey, value) }

    var defaultDanmakuTransparency: Int
        get() = runBlocking {
            dsm.getPreferenceFlow(PrefKeys.prefDefaultDanmakuTransparencyRequest).first()
        }
        set(value) = runBlocking {
            dsm.editPreference(PrefKeys.prefDefaultDanmakuTransparencyKey, value)
        }

    var defaultDanmakuEnabled: Boolean
        get() = runBlocking {
            dsm.getPreferenceFlow(PrefKeys.prefDefaultDanmakuEnabledRequest).first()
        }
        set(value) = runBlocking {
            dsm.editPreference(PrefKeys.prefDefaultDanmakuEnabledKey, value)
        }

    var defaultDanmakuArea: Float
        get() = runBlocking {
            dsm.getPreferenceFlow(PrefKeys.prefDefaultDanmakuAreaRequest).first()
        }
        set(value) = runBlocking { dsm.editPreference(PrefKeys.prefDefaultDanmakuAreaKey, value) }

    var defaultVideoCodec: VideoCodec
        get() = VideoCodec.fromCode(
            runBlocking { dsm.getPreferenceFlow(PrefKeys.prefDefaultVideoCodecRequest).first() }
        )
        set(value) = runBlocking {
            dsm.editPreference(PrefKeys.prefDefaultVideoCodecKey, value.ordinal)
        }

    var enableFirebaseCollection: Boolean
        get() = runBlocking {
            dsm.getPreferenceFlow(PrefKeys.prefEnabledFirebaseCollectionRequest).first()
        }
        set(value) = runBlocking {
            dsm.editPreference(PrefKeys.prefEnabledFirebaseCollectionKey, value)
        }

    var incognitoMode: Boolean
        get() = runBlocking { dsm.getPreferenceFlow(PrefKeys.prefIncognitoModeRequest).first() }
        set(value) = runBlocking { dsm.editPreference(PrefKeys.prefIncognitoModeKey, value) }

    var defaultSubtitleFontSize: TextUnit
        get() = runBlocking {
            dsm.getPreferenceFlow(PrefKeys.prefDefaultSubtitleFontSizeRequest).first().sp
        }
        set(value) = runBlocking {
            dsm.editPreference(PrefKeys.prefDefaultSubtitleFontSizeKey, value.value.roundToInt())
        }

    var defaultSubtitleBottomPadding: Dp
        get() = runBlocking {
            dsm.getPreferenceFlow(PrefKeys.prefDefaultSubtitleBottomPaddingRequest).first().dp
        }
        set(value) = runBlocking {
            dsm.editPreference(
                PrefKeys.prefDefaultSubtitleBottomPaddingKey, value.value.roundToInt()
            )
        }

    var showFps: Boolean
        get() = runBlocking { dsm.getPreferenceFlow(PrefKeys.prefShowFpsRequest).first() }
        set(value) = runBlocking { dsm.editPreference(PrefKeys.prefShowFpsKey, value) }

    var buvid3: String
        get() = runBlocking {
            val id = dsm.getPreferenceFlow(PrefKeys.prefBuvid3Request).first()
            if (id != "") {
                id
            } else {
                //random buvid3
                val randomBuvid3 = "${UUID.randomUUID()}${(0..9).random()}infoc"
                buvid3 = randomBuvid3
                randomBuvid3
            }
        }
        set(value) = runBlocking { dsm.editPreference(PrefKeys.prefBuvid3Key, value) }

    val densityFlow: Flow<Float> get() = dsm.getPreferenceFlow(PrefKeys.prefDensityRequest)
    var density: Float
        get() = runBlocking { dsm.getPreferenceFlow(PrefKeys.prefDensityRequest).first() }
        set(value) = runBlocking { dsm.editPreference(PrefKeys.prefDensityKey, value) }
}

private object PrefKeys {
    val prefIsLoginKey = booleanPreferencesKey("il")
    val prefUidKey = longPreferencesKey("uid")
    val prefSidKey = stringPreferencesKey("sid")
    val prefSessDataKey = stringPreferencesKey("sd")
    val prefBiliJctKey = stringPreferencesKey("bj")
    val prefUidCkMd5Key = stringPreferencesKey("ucm")
    val prefTokenExpiredDateKey = longPreferencesKey("ted")
    val prefDefaultQualityKey = intPreferencesKey("dq")
    val prefDefaultDanmakuSizeKey = intPreferencesKey("dds")
    val prefDefaultDanmakuTransparencyKey = intPreferencesKey("ddt")
    val prefDefaultDanmakuEnabledKey = booleanPreferencesKey("dde")
    val prefDefaultDanmakuAreaKey = floatPreferencesKey("dda")
    val prefDefaultVideoCodecKey = intPreferencesKey("dvc")
    val prefEnabledFirebaseCollectionKey = booleanPreferencesKey("efc")
    val prefIncognitoModeKey = booleanPreferencesKey("im")
    val prefDefaultSubtitleFontSizeKey = intPreferencesKey("dsfs")
    val prefDefaultSubtitleBottomPaddingKey = intPreferencesKey("dsbp")
    val prefShowFpsKey = booleanPreferencesKey("sf")
    val prefBuvid3Key = stringPreferencesKey("random_buvid3")
    val prefDensityKey = floatPreferencesKey("density")

    val prefIsLoginRequest = PreferenceRequest(prefIsLoginKey, false)
    val prefUidRequest = PreferenceRequest(prefUidKey, 0)
    val prefSidRequest = PreferenceRequest(prefSidKey, "")
    val prefSessDataRequest = PreferenceRequest(prefSessDataKey, "")
    val prefBiliJctRequest = PreferenceRequest(prefBiliJctKey, "")
    val prefUidCkMd5Request = PreferenceRequest(prefUidCkMd5Key, "")
    val prefTokenExpiredDateRequest = PreferenceRequest(prefTokenExpiredDateKey, 0)
    val prefDefaultQualityRequest = PreferenceRequest(prefDefaultQualityKey, Resolution.R1080P.code)
    val prefDefaultDanmakuSizeRequest = PreferenceRequest(prefDefaultDanmakuSizeKey, 6)
    val prefDefaultDanmakuTransparencyRequest =
        PreferenceRequest(prefDefaultDanmakuTransparencyKey, 0)
    val prefDefaultDanmakuEnabledRequest = PreferenceRequest(prefDefaultDanmakuEnabledKey, true)
    val prefDefaultDanmakuAreaRequest = PreferenceRequest(prefDefaultDanmakuAreaKey, 1f)
    val prefDefaultVideoCodecRequest =
        PreferenceRequest(prefDefaultVideoCodecKey, VideoCodec.AVC.ordinal)
    val prefEnabledFirebaseCollectionRequest =
        PreferenceRequest(prefEnabledFirebaseCollectionKey, true)
    val prefIncognitoModeRequest = PreferenceRequest(prefIncognitoModeKey, false)
    val prefDefaultSubtitleFontSizeRequest = PreferenceRequest(prefDefaultSubtitleFontSizeKey, 24)
    val prefDefaultSubtitleBottomPaddingRequest =
        PreferenceRequest(prefDefaultSubtitleBottomPaddingKey, 12)
    val prefShowFpsRequest = PreferenceRequest(prefShowFpsKey, false)
    val prefBuvid3Request = PreferenceRequest(prefBuvid3Key, "")
    val prefDensityRequest =
        PreferenceRequest(prefDensityKey, BVApp.context.resources.displayMetrics.widthPixels / 960f)
}