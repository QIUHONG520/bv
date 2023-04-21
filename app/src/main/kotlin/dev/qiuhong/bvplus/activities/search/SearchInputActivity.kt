package dev.qiuhong.bvplus.activities.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.qiuhong.bvplus.screen.search.SearchInputScreen
import dev.qiuhong.bvplus.ui.theme.BVTheme

class SearchInputActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BVTheme {
                SearchInputScreen()
            }
        }
    }
}