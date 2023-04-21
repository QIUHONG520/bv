package dev.qiuhong.bvplus.activities.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.qiuhong.bvplus.screen.search.SearchResultScreen
import dev.qiuhong.bvplus.ui.theme.BVTheme

class SearchResultActivity : ComponentActivity() {
    companion object {
        fun actionStart(context: Context, keyword: String) {
            context.startActivity(
                Intent(context, SearchResultActivity::class.java).apply {
                    putExtra("keyword", keyword)
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BVTheme {
                SearchResultScreen()
            }
        }
    }
}