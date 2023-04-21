package dev.qiuhong.bvplus.component.videocard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import dev.qiuhong.bvplus.component.SurfaceWithoutClickable
import dev.qiuhong.bvplus.component.UpIcon
import dev.qiuhong.bvplus.entity.carddata.VideoCardData
import dev.qiuhong.bvplus.ui.theme.BVTheme
import dev.qiuhong.bvplus.util.focusedBorder

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun LargeVideoCard(
    modifier: Modifier = Modifier,
    data: VideoCardData,
    onClick: () -> Unit = {},
    onFocus: () -> Unit = {}
) {
    val view = LocalView.current

    var hasFocus by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (hasFocus) 1f else 0.95f)

    val height = 160.dp
    val reasonColor = Color.Red

    LaunchedEffect(hasFocus) {
        if (hasFocus) onFocus()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .onFocusChanged { hasFocus = it.isFocused }
            .focusedBorder(MaterialTheme.shapes.medium)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .height(height)
        ) {
            Box {
                if (!view.isInEditMode) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1.6f)
                            .clip(MaterialTheme.shapes.large),
                        model = data.cover,
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                } else {
                    SurfaceWithoutClickable(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1.6f),
                        shape = MaterialTheme.shapes.large,
                        color = Color.White
                    ) {}
                }
                SurfaceWithoutClickable(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = data.timeString,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = data.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge
                )
                Box(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .border(
                            width = (1.5).dp,
                            color = if (data.reason.isNotEmpty()) reasonColor else Color.Transparent,
                            shape = RoundedCornerShape(6.dp)
                        )
                ) {
                    Text(
                        modifier = Modifier.padding(6.dp, 2.dp),
                        text = data.reason,
                        style = MaterialTheme.typography.bodySmall,
                        color = reasonColor,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UpIcon()
                    Text(text = data.upName)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)
                ) {
                    Text(text = "P${data.playString}")
                    Text(text = "D${data.danmakuString}")
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview
@Composable
fun LargeVideoCardPreview() {
    val data = VideoCardData(
        avid = 0,
        title = "震惊！太震惊了！真的是太震惊了！我的天呐！真TMD震惊！",
        cover = "http://i2.hdslb.com/bfs/archive/af17fc07b8f735e822563cc45b7b5607a491dfff.jpg",
        reason = "本周必看",
        upName = "bishi",
        play = 2333,
        danmaku = 666,
        time = 2333 * 1000
    )
    BVTheme {
        SurfaceWithoutClickable {
            LargeVideoCard(
                data = data
            )
        }
    }
}