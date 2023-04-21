package dev.qiuhong.bvplus.activities.video

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.qiuhong.bvplus.screen.TagScreen
import dev.qiuhong.bvplus.ui.theme.BVTheme

class TagActivity : ComponentActivity() {
    companion object {
        fun actionStart(context: Context, tagId: Int, tagName: String) {
            context.startActivity(
                Intent(context, TagActivity::class.java).apply {
                    putExtra("tagId", tagId)
                    putExtra("tagName", tagName)
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BVTheme {
                TagScreen()
            }
        }
    }
}