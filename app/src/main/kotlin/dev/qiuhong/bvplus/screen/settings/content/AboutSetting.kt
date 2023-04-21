package dev.qiuhong.bvplus.screen.settings.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import dev.qiuhong.bvplus.BuildConfig
import dev.qiuhong.bvplus.R
import dev.qiuhong.bvplus.component.settings.UpdateDialog
import dev.qiuhong.bvplus.screen.settings.SettingsMenuButton
import dev.qiuhong.bvplus.screen.settings.SettingsMenuNavItem
import dev.qiuhong.bvplus.ui.theme.BVTheme

@Composable
fun AboutSetting(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var showUpdateDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = SettingsMenuNavItem.About.getDisplayName(context),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.settings_version_current_version,
                        BuildConfig.VERSION_NAME
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(
                            R.string.settings_version_latest_version, ""
                        )
                    )
                    AsyncImage(
                        modifier = Modifier
                            .height(20.dp)
                            .widthIn(max = 200.dp),
                        model = ImageRequest.Builder(context)
                            .data("https://img.shields.io/github/v/tag/aaa1115910/bv?label=Version")
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight
                    )
                }
            }

            var isUpdateButtonHasFocus by remember { mutableStateOf(false) }
            SettingsMenuButton(
                text = stringResource(R.string.settings_version_check_update_button),
                selected = isUpdateButtonHasFocus,
                onFocus = { isUpdateButtonHasFocus = true },
                onLoseFocus = { isUpdateButtonHasFocus = false },
                onClick = { showUpdateDialog = true }
            )
        }
        Text(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = "https://github.com/aaa1115910/bv"
        )
    }

    UpdateDialog(
        show = showUpdateDialog,
        onHideDialog = { showUpdateDialog = false }
    )
}

@Preview
@Composable
private fun AboutSettingPreview() {
    BVTheme {
        AboutSetting()
    }
}