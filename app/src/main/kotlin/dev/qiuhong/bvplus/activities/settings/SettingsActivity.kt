package dev.qiuhong.bvplus.activities.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.qiuhong.bvplus.screen.settings.SettingsScreen
import dev.qiuhong.bvplus.ui.theme.BVTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BVTheme {
                SettingsScreen()
            }
        }
    }
}
