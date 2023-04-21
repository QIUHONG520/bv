package dev.qiuhong.bvplus.screen.home

import android.content.Intent
import android.view.KeyEvent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.itemsIndexed
import androidx.tv.material3.Border
import androidx.tv.material3.Carousel
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import dev.qiuhong.biliapi.entity.anime.AnimeFeedData
import dev.qiuhong.biliapi.entity.anime.CarouselItem
import dev.qiuhong.biliapi.entity.web.Hover
import dev.qiuhong.bvplus.R
import dev.qiuhong.bvplus.activities.anime.AnimeIndexActivity
import dev.qiuhong.bvplus.activities.anime.AnimeTimelineActivity
import dev.qiuhong.bvplus.activities.user.FollowingSeasonActivity
import dev.qiuhong.bvplus.activities.video.SeasonInfoActivity
import dev.qiuhong.bvplus.component.videocard.SeasonCard
import dev.qiuhong.bvplus.entity.carddata.SeasonCardData
import dev.qiuhong.bvplus.ui.theme.BVTheme
import dev.qiuhong.bvplus.util.ImageSize
import dev.qiuhong.bvplus.util.focusedBorder
import dev.qiuhong.bvplus.util.resizedImageUrl
import dev.qiuhong.bvplus.viewmodel.home.AnimeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnimeScreen(
    modifier: Modifier = Modifier,
    animeViewModel: AnimeViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val carouselItems = animeViewModel.carouselItems
    val animeFeeds = animeViewModel.feedItems

    TvLazyColumn(
        modifier = modifier
    ) {
        item {
            AnimeCarousel(
                modifier = Modifier.padding(32.dp, 0.dp),
                data = carouselItems
            )
        }
        item {
            AnimeFeatureButtons(
                modifier = Modifier.padding(32.dp, 24.dp),
                onOpenTimeline = {
                    context.startActivity(Intent(context, AnimeTimelineActivity::class.java))
                },
                onOpenFollowing = {
                    context.startActivity(Intent(context, FollowingSeasonActivity::class.java))
                },
                onOpenIndex = {
                    context.startActivity(Intent(context, AnimeIndexActivity::class.java))
                }
            )
        }
        itemsIndexed(items = animeFeeds) { index, feedItems ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .onFocusChanged {
                        if (it.hasFocus) {
                            if (index + 10 > animeFeeds.size) {
                                animeViewModel.loadMore()
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                when (feedItems.firstOrNull()?.cardStyle) {
                    "v_card" -> AnimeFeedVideoRow(
                        data = feedItems
                    )

                    "rank" -> AnimeFeedRankRow(
                        data = feedItems
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AnimeCarousel(
    modifier: Modifier = Modifier,
    data: List<CarouselItem>
) {
    val context = LocalContext.current

    Carousel(
        slideCount = data.size,
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(MaterialTheme.shapes.large)
            .focusedBorder()
    ) { itemIndex ->
        CarouselSlide {
            AnimeCarouselCard(
                data = data[itemIndex],
                onClick = {
                    SeasonInfoActivity.actionStart(
                        context = context,
                        epId = data[itemIndex].episodeId,
                        seasonId = data[itemIndex].seasonId
                    )
                }
            )
        }
    }
}

@Composable
fun AnimeCarouselCard(
    modifier: Modifier = Modifier,
    data: CarouselItem,
    onClick: () -> Unit = {}
) {
    AsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable { onClick() },
        model = data.cover,
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        alignment = Alignment.TopCenter
    )
}

@Composable
private fun AnimeFeatureButtons(
    modifier: Modifier = Modifier,
    onOpenTimeline: () -> Unit,
    onOpenFollowing: () -> Unit,
    onOpenIndex: () -> Unit,
    onOpenUnknown: () -> Unit = {}
) {
    val buttons = listOf(
        Triple(
            stringResource(R.string.anime_home_button_timeline),
            Icons.Rounded.Alarm,
            onOpenTimeline
        ),
        Triple(
            stringResource(R.string.anime_home_button_following),
            Icons.Rounded.Favorite,
            onOpenFollowing
        ),
        Triple(
            stringResource(R.string.anime_home_button_index),
            Icons.Rounded.List,
            onOpenIndex
        ),
        Triple(
            stringResource(R.string.anime_home_button_unknown),
            Icons.Rounded.QuestionMark,
            onOpenUnknown
        )
    )

    Row(
        modifier = modifier.height(80.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        buttons.forEach { (title, icon, onClick) ->
            AnimeFeatureButton(
                modifier = Modifier.weight(1f),
                title = title,
                icon = icon,
                onClick = onClick
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AnimeFeatureButton(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        color = ClickableSurfaceDefaults.color(
            color = MaterialTheme.colorScheme.surface,
            focusedColor = MaterialTheme.colorScheme.surface,
            pressedColor = MaterialTheme.colorScheme.surface
        ),
        contentColor = ClickableSurfaceDefaults.contentColor(
            color = MaterialTheme.colorScheme.onSurface,
            focusedColor = MaterialTheme.colorScheme.onSurface,
            pressedColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = ClickableSurfaceDefaults.shape(shape = MaterialTheme.shapes.large),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(width = 3.dp, color = Color.White),
                shape = MaterialTheme.shapes.large
            )
        ),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = icon, contentDescription = null)
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun AnimeFeedVideoRow(
    modifier: Modifier = Modifier,
    data: List<AnimeFeedData.FeedItem.FeedSubItem>
) {
    val context = LocalContext.current
    TvLazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        data.forEachIndexed { index, feedItem ->
            val cardModifier = if (index == data.lastIndex) {
                Modifier.onPreviewKeyEvent {
                    when (it.key) {
                        Key.DirectionRight -> return@onPreviewKeyEvent true
                    }
                    false
                }
            } else {
                Modifier
            }

            item {
                SeasonCard(
                    modifier = cardModifier,
                    coverHeight = 180.dp,
                    data = SeasonCardData(
                        seasonId = feedItem.seasonId ?: 0,
                        title = feedItem.title,
                        subTitle = feedItem.subTitle,
                        cover = feedItem.cover.resizedImageUrl(ImageSize.SeasonCoverThumbnail),
                        rating = feedItem.rating ?: ""
                    ),
                    onClick = {
                        SeasonInfoActivity.actionStart(
                            context = context,
                            seasonId = feedItem.seasonId
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun AnimeFeedRankRow(
    modifier: Modifier = Modifier,
    data: List<AnimeFeedData.FeedItem.FeedSubItem>
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .height(300.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            // light theme color: Color(250, 222, 214)
                            Color(20, 18, 17),
                            Color(20, 18, 17).copy(alpha = 0.298f)
                        )
                    )
                )
        ) {}
        BoxWithConstraints {
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .offset(x = (-1 * (0.25 * 1.6 * this.maxHeight.value)).dp)
                    .graphicsLayer { alpha = 0.99f }
                    .drawWithContent {
                        val colors = listOf(
                            Color.Black,
                            Color.Transparent
                        )
                        drawContent()
                        drawRect(
                            brush = Brush.horizontalGradient(colors),
                            blendMode = BlendMode.DstIn
                        )
                        drawRect(
                            brush = Brush.verticalGradient(colors),
                            blendMode = BlendMode.DstIn
                        )
                    },
                model = data.first().cover,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                alpha = 1f
            )
        }
        Row(
            modifier = Modifier
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(240.dp)
                    .padding(32.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = data.first().title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Text(
                    text = data.first().subTitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }

            TvLazyRow(
                modifier = modifier,
                contentPadding = PaddingValues(horizontal = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                data.first().subItems?.forEachIndexed { index, feedItem ->
                    val cardModifier = if (index == data.first().subItems?.lastIndex) {
                        Modifier.onPreviewKeyEvent {
                            when (it.nativeKeyEvent.keyCode) {
                                KeyEvent.KEYCODE_DPAD_RIGHT -> return@onPreviewKeyEvent true
                            }
                            false
                        }
                    } else {
                        Modifier
                    }

                    item {
                        SeasonCard(
                            modifier = cardModifier,
                            coverHeight = 180.dp,
                            data = SeasonCardData(
                                seasonId = feedItem.seasonId ?: 0,
                                title = feedItem.title,
                                subTitle = feedItem.subTitle,
                                cover = feedItem.cover.resizedImageUrl(ImageSize.SeasonCoverThumbnail),
                                rating = feedItem.rating ?: ""
                            ),
                            onClick = {
                                SeasonInfoActivity.actionStart(
                                    context = context,
                                    seasonId = feedItem.seasonId
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = "id:tv_1080p")
@Composable
fun AnimeFeedRankRowPreview() {
    val data = listOf(
        AnimeFeedData.FeedItem.FeedSubItem(
            cardStyle = "rank",
            rankId = 126,
            cover = "http://i0.hdslb.com/bfs/archive/aae451dabf64ead2e983f92be76039a8ba233ade.png",
            title = "热门热血番剧榜",
            subTitle = "每小时更新",
            report = AnimeFeedData.FeedItem.FeedSubItem.Report(),
            subItems = List(8) {
                AnimeFeedData.FeedItem.FeedSubItem(
                    cardStyle = "v_card",
                    rankId = 0,
                    cover = "https://i0.hdslb.com/bfs/bangumi/image/f610305ad3922bee9d51748ab38da0c54e785b44.png",
                    hover = Hover(
                        img = "http://i0.hdslb.com/bfs/archive/aae451dabf64ead2e983f92be76039a8ba233ade.png",
                        text = listOf("漫画改", "热血", "更新至第6话")
                    ),
                    title = "解雇后走上人生巅峰",
                    subTitle = "被解雇的暗黑士兵慢生活的第二人生",
                    report = AnimeFeedData.FeedItem.FeedSubItem.Report()
                )
            }
        )
    )
    BVTheme {
        AnimeFeedRankRow(data = data)
    }
}