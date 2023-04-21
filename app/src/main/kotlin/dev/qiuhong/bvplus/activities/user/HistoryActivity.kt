package dev.qiuhong.bvplus.activities.user

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.qiuhong.bvplus.screen.user.HistoryScreen
import dev.qiuhong.bvplus.ui.theme.BVTheme

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BVTheme {
                HistoryScreen()
            }
        }
    }
}
