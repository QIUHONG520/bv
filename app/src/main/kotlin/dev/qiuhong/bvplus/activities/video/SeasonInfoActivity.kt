package dev.qiuhong.bvplus.activities.video

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.qiuhong.bvplus.screen.SeasonInfoScreen
import dev.qiuhong.bvplus.ui.theme.BVTheme

class SeasonInfoActivity : ComponentActivity() {
    companion object {
        fun actionStart(context: Context, epId: Int? = null, seasonId: Int? = null) {
            context.startActivity(
                Intent(context, SeasonInfoActivity::class.java).apply {
                    epId?.let { putExtra("epid", epId) }
                    seasonId?.let { putExtra("seasonid", seasonId) }
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BVTheme {
                SeasonInfoScreen()
            }
        }
    }
}
