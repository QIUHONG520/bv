package dev.qiuhong.bvplus.screen.settings.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import dev.qiuhong.bvplus.entity.Resolution
import dev.qiuhong.bvplus.screen.settings.SettingsMenuNavItem
import dev.qiuhong.bvplus.screen.settings.SettingsMenuSelectItem
import dev.qiuhong.bvplus.util.Prefs

@Composable
fun ResolutionSetting(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedResolution by remember { mutableStateOf(Resolution.fromCode(Prefs.defaultQuality)) }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = SettingsMenuNavItem.Resolution.getDisplayName(context),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(12.dp))
            TvLazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = Resolution.values().reversed()) { resolution ->
                    SettingsMenuSelectItem(
                        text = resolution.getDisplayName(context),
                        selected = selectedResolution == resolution,
                        onClick = {
                            selectedResolution = resolution
                            Prefs.defaultQuality = resolution.code
                        }
                    )
                }
            }
        }
    }
}