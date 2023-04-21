package dev.qiuhong.bvplus.activities.user

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.qiuhong.bvplus.screen.user.FollowingSeasonScreen
import dev.qiuhong.bvplus.ui.theme.BVTheme

class FollowingSeasonActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BVTheme {
                FollowingSeasonScreen()
            }
        }
    }
}