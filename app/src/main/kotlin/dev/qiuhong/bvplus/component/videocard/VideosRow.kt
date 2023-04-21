package dev.qiuhong.bvplus.component.videocard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Text
import dev.qiuhong.bvplus.activities.video.VideoInfoActivity
import dev.qiuhong.bvplus.entity.carddata.VideoCardData

@Composable
fun VideosRow(
    modifier: Modifier = Modifier,
    header: String,
    hideShowMore: Boolean = true,
    videos: List<VideoCardData>,
    showMore: () -> Unit
) {
    val context = LocalContext.current
    var hasFocus by remember { mutableStateOf(false) }
    val titleColor = if (hasFocus) Color.White else Color.White.copy(alpha = 0.6f)
    val titleFontSize by animateFloatAsState(if (hasFocus) 30f else 14f)

    Column(
        modifier = modifier
            .padding(start = 50.dp)
            .onFocusChanged { hasFocus = it.hasFocus }
    ) {
        Text(
            text = header,
            fontSize = titleFontSize.sp,
            color = titleColor
        )
        TvLazyRow(
            modifier = Modifier.padding(top = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(end = 50.dp, start = 12.dp)
        ) {
            items(items = videos) { videoData ->
                SmallVideoCard(
                    modifier = Modifier.width(200.dp),
                    data = videoData,
                    onClick = {
                        VideoInfoActivity.actionStart(context, videoData.avid)
                    }
                )
            }
            if (!hideShowMore) {
                item {
                    TextButton(onClick = {
                        showMore()
                    }) {
                        Text(text = "显示更多")
                    }
                }
            }
        }
    }
}
