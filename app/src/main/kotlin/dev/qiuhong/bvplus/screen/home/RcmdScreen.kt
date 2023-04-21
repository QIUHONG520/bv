package dev.qiuhong.bvplus.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvGridItemSpan
import androidx.tv.foundation.lazy.grid.TvLazyGridState
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.itemsIndexed
import dev.qiuhong.bvplus.activities.video.VideoInfoActivity
import dev.qiuhong.bvplus.component.LoadingTip
import dev.qiuhong.bvplus.component.videocard.SmallVideoCard
import dev.qiuhong.bvplus.entity.carddata.VideoCardData
import dev.qiuhong.bvplus.viewmodel.home.RcmdViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun RcmdScreen(
    modifier: Modifier = Modifier,
    tvLazyGridState: TvLazyGridState,
    onBackNav: () -> Unit,
    rcmdViewModel: RcmdViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var currentFocusedIndex by remember { mutableStateOf(0) }

    LaunchedEffect(currentFocusedIndex) {
        if (currentFocusedIndex + 24 > rcmdViewModel.rcmdVideoList.size) {
            scope.launch(Dispatchers.Default) { rcmdViewModel.loadMore() }
        }
    }

    TvLazyVerticalGrid(
        modifier = modifier
            .onPreviewKeyEvent {
                when (it.key) {
                    Key.Back -> {
                        if (it.type == KeyEventType.KeyUp) {
                            scope.launch(Dispatchers.Main) {
                                tvLazyGridState.animateScrollToItem(0)
                            }
                            onBackNav()
                        }
                        return@onPreviewKeyEvent true
                    }

                    Key.DirectionRight -> {
                        if (currentFocusedIndex % 4 == 3) {
                            return@onPreviewKeyEvent true
                        }
                    }
                }
                return@onPreviewKeyEvent false
            },
        state = tvLazyGridState,
        columns = TvGridCells.Fixed(4),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        itemsIndexed(rcmdViewModel.rcmdVideoList) { index, video ->
            SmallVideoCard(
                data = VideoCardData(
                    avid = video.aid?:0,
                    title = video.title,
                    cover = video.pic,
                    play = video.stat.view,
                    danmaku = video.stat.danmaku,
                    upName = video.owner.name,
                    time = video.duration * 1000L
                ),
                onClick = { VideoInfoActivity.actionStart(context, video.aid?:0) },
                onFocus = { currentFocusedIndex = index }
            )
        }
        if (rcmdViewModel.loading)
            item(
                span = { TvGridItemSpan(4) }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingTip()
                }
            }
    }
}