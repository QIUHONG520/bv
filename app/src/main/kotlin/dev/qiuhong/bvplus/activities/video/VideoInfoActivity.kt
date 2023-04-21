package dev.qiuhong.bvplus.activities.video

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.qiuhong.bvplus.screen.VideoInfoScreen
import dev.qiuhong.bvplus.ui.theme.BVTheme

class VideoInfoActivity : ComponentActivity() {
    companion object {
        fun actionStart(context: Context, aid: Int, fromSeason: Boolean = false) {
            context.startActivity(
                Intent(context, VideoInfoActivity::class.java).apply {
                    putExtra("aid", aid)
                    putExtra("fromSeason", fromSeason)
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BVTheme {
                VideoInfoScreen()
            }
        }
    }
}
