package dev.qiuhong.bvplus.activities.user

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.ExperimentalTvMaterial3Api
import dev.qiuhong.bvplus.component.SurfaceWithoutClickable
import dev.qiuhong.bvplus.screen.QRLoginScreen
import dev.qiuhong.bvplus.ui.theme.BVTheme

class LoginActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BVTheme {
                SurfaceWithoutClickable(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFE9487F)
                ) {
                    QRLoginScreen()
                }
            }
        }
    }
}
