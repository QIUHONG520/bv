package dev.qiuhong.bvplus.activities.anime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.qiuhong.bvplus.screen.home.anime.AnimeIndexScreen
import dev.qiuhong.bvplus.ui.theme.BVTheme

class AnimeIndexActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BVTheme {
                AnimeIndexScreen()
            }
        }
    }
}