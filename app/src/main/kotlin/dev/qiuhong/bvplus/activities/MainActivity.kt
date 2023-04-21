package dev.qiuhong.bvplus.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.qiuhong.bvplus.screen.HomeScreen
import dev.qiuhong.bvplus.screen.RegionBlockScreen
import dev.qiuhong.bvplus.ui.theme.BVTheme
import dev.qiuhong.bvplus.util.NetworkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var keepSplashScreen = true
        installSplashScreen().apply {
            setKeepOnScreenCondition { keepSplashScreen }
        }
        super.onCreate(savedInstanceState)

        setContent {
            val scope = rememberCoroutineScope()
            //var isMainlandChina by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                scope.launch(Dispatchers.Default) {
                    //isMainlandChina = NetworkUtil.isMainlandChina()
                    keepSplashScreen = false
                }
            }

            BVTheme {
                //if (isMainlandChina) RegionBlockScreen() else HomeScreen()
                //去除大陆使用检测
                HomeScreen()
            }
        }
    }
}

