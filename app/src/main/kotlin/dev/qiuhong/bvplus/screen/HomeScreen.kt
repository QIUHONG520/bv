package dev.qiuhong.bvplus.screen

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.grid.rememberTvLazyGridState
import dev.qiuhong.bvplus.R
import dev.qiuhong.bvplus.activities.search.SearchInputActivity
import dev.qiuhong.bvplus.activities.user.FavoriteActivity
import dev.qiuhong.bvplus.activities.user.FollowingSeasonActivity
import dev.qiuhong.bvplus.activities.user.HistoryActivity
import dev.qiuhong.bvplus.activities.user.UserInfoActivity
import dev.qiuhong.bvplus.component.TopNav
import dev.qiuhong.bvplus.component.TopNavItem
import dev.qiuhong.bvplus.component.UserPanel
import dev.qiuhong.bvplus.screen.home.AnimeScreen
import dev.qiuhong.bvplus.screen.home.DynamicsScreen
import dev.qiuhong.bvplus.screen.home.PartitionScreen
import dev.qiuhong.bvplus.screen.home.PopularScreen
import dev.qiuhong.bvplus.screen.home.RcmdScreen
import dev.qiuhong.bvplus.util.fInfo
import dev.qiuhong.bvplus.util.requestFocus
import dev.qiuhong.bvplus.util.toast
import dev.qiuhong.bvplus.viewmodel.UserViewModel
import dev.qiuhong.bvplus.viewmodel.home.DynamicViewModel
import dev.qiuhong.bvplus.viewmodel.home.PopularViewModel
import dev.qiuhong.bvplus.viewmodel.home.RcmdViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    rcmdViewModel: RcmdViewModel = koinViewModel(),
    popularViewModel: PopularViewModel = koinViewModel(),
    dynamicViewModel: DynamicViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val logger = KotlinLogging.logger { }

    val rcmdState = rememberTvLazyGridState()
    val popularState = rememberTvLazyGridState()
    val dynamicState = rememberTvLazyGridState()

    var selectedTab by remember { mutableStateOf(TopNavItem.Rcmd) }
    var showUserPanel by remember { mutableStateOf(false) }
    var lastPressBack: Long by remember { mutableStateOf(0) }
    var focusInNav by remember { mutableStateOf(false) }

    val settingsButtonFocusRequester = remember { FocusRequester() }
    val navFocusRequester = remember { FocusRequester() }

    val onFocusBackToNav: () -> Unit = {
        println("onFocusBackToNav")
        focusInNav = true
    }

    //启动时刷新数据
    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.Default) {
            rcmdViewModel.loadMore()
        }
        scope.launch(Dispatchers.Default) {
            popularViewModel.loadMore()
        }
        scope.launch(Dispatchers.Default) {
            dynamicViewModel.loadMore()
        }
        scope.launch(Dispatchers.Default) {
            userViewModel.updateUserInfo()
        }
    }

    //监听登录变化
    LaunchedEffect(userViewModel.isLogin) {
        if (userViewModel.isLogin) {
            //login
            userViewModel.updateUserInfo()
        } else {
            //logout
            userViewModel.clearUserInfo()
        }
    }

    BackHandler(!showUserPanel) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastPressBack < 1000 * 3) {
            logger.fInfo { "Exiting bug video plus" }
            (context as Activity).finish()
        } else {
            lastPressBack = currentTime
            R.string.home_press_back_again_to_exit.toast(context)
        }
    }

    Box(
        modifier = modifier
    ) {
        Scaffold(
            modifier = Modifier,
            topBar = {
                TopNav(
                    modifier = Modifier.focusRequester(navFocusRequester),
                    isLogin = userViewModel.isLogin,
                    username = userViewModel.username,
                    face = userViewModel.face,
                    focusInNav = focusInNav,
                    settingsButtonFocusRequester = settingsButtonFocusRequester,
                    onSelectedChange = { nav ->
                        selectedTab = nav
                        when (nav) {
                            TopNavItem.Rcmd -> {

                            }

                            TopNavItem.Popular -> {
                                //scope.launch(Dispatchers.Default) { popularState.scrollToItem(0, 0) }
                            }

                            TopNavItem.Partition -> {

                            }

                            TopNavItem.Anime -> {

                            }

                            TopNavItem.Dynamics -> {
                                //scope.launch(Dispatchers.Default) { dynamicState.scrollToItem(0, 0) }
                                if (!dynamicViewModel.loading && dynamicViewModel.isLogin && dynamicViewModel.dynamicList.isEmpty()) {
                                    scope.launch(Dispatchers.Default) { dynamicViewModel.loadMore() }
                                }
                            }

                            TopNavItem.Search -> {

                            }
                        }
                    },
                    onClick = { nav ->
                        when (nav) {
                            TopNavItem.Rcmd -> {
                                logger.fInfo { "clear recommend data" }
                                rcmdViewModel.clearData()
                                logger.fInfo { "reload recommend data" }
                                scope.launch(Dispatchers.Default) { rcmdViewModel.loadMore() }
                            }

                            TopNavItem.Popular -> {
                                //scope.launch(Dispatchers.Default) { popularState.scrollToItem(0, 0) }
                                logger.fInfo { "clear popular data" }
                                popularViewModel.clearData()
                                logger.fInfo { "reload popular data" }
                                scope.launch(Dispatchers.Default) { popularViewModel.loadMore() }
                            }

                            TopNavItem.Partition -> {

                            }

                            TopNavItem.Anime -> {

                            }

                            TopNavItem.Dynamics -> {
                                //scope.launch(Dispatchers.Default) { dynamicState.scrollToItem(0, 0) }
                                dynamicViewModel.clearData()
                                scope.launch(Dispatchers.Default) { dynamicViewModel.loadMore() }
                            }

                            TopNavItem.Search -> {
                                context.startActivity(
                                    Intent(context, SearchInputActivity::class.java)
                                )
                            }
                        }
                    },
                    onShowUserPanel = { showUserPanel = true }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .onFocusChanged {
                        focusInNav = !it.hasFocus
                    }
            ) {
                Crossfade(targetState = selectedTab) { screen ->
                    when (screen) {
                        TopNavItem.Rcmd -> RcmdScreen(
                            tvLazyGridState = rcmdState,
                            onBackNav = onFocusBackToNav
                        )

                        TopNavItem.Popular -> PopularScreen(
                            tvLazyGridState = popularState,
                            onBackNav = onFocusBackToNav
                        )

                        TopNavItem.Partition -> PartitionScreen()
                        TopNavItem.Anime -> AnimeScreen()
                        TopNavItem.Dynamics -> DynamicsScreen(
                            tvLazyGridState = dynamicState,
                            onBackNav = onFocusBackToNav
                        )

                        else -> RcmdScreen(
                            tvLazyGridState = rcmdState,
                            onBackNav = onFocusBackToNav
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showUserPanel,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    visible = showUserPanel,
                    enter = fadeIn() + scaleIn(),
                    exit = shrinkHorizontally()
                ) {
                    UserPanel(
                        modifier = Modifier
                            .padding(12.dp),
                        username = userViewModel.username,
                        face = userViewModel.face,
                        onHide = {
                            showUserPanel = false
                            settingsButtonFocusRequester.requestFocus(scope)
                        },
                        onGoMy = {
                            context.startActivity(Intent(context, UserInfoActivity::class.java))
                        },
                        onGoHistory = {
                            context.startActivity(Intent(context, HistoryActivity::class.java))
                        },
                        onGoFavorite = {
                            context.startActivity(Intent(context, FavoriteActivity::class.java))
                        },
                        onGoFollowing = {
                            context.startActivity(Intent(context, FollowingSeasonActivity::class.java))
                        },
                        onGoLater = {
                            "按钮放在这只是拿来当摆设的！".toast(context)
                        }
                    )
                }
            }
        }
    }
}