package cdglacier.sharedelementsandbox.ui.screen.transparentbackground

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cdglacier.sharedelementsandbox.ui.compositionlocal.AnimatedVisibilityScopeProvider
import cdglacier.sharedelementsandbox.ui.compositionlocal.LocalSharedTransitionScope
import cdglacier.sharedelementsandbox.ui.compositionlocal.easySharedElement
import cdglacier.sharedelementsandbox.unsplash.Photo
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.math.abs

@Serializable
object TransparentBackgroundView

private enum class SwipeDirection {
    VERTICAL,
    HORIZONTAL
}

private data class PhotoSharedTransitionKey(
    val photo: Photo,
    val isTransitionEnabled: Boolean,
)

@Composable
fun TransparentBackgroundViewScreen(
    photos: List<Photo>,
) {
    var isOpenedDetailView by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val lazyGridState = rememberLazyGridState()
    val pagerState = rememberPagerState { photos.size }

    BackHandler(isOpenedDetailView) {
        isOpenedDetailView = false
    }

    LaunchedEffect(pagerState.settledPage) {
        lazyGridState.scrollToItem(pagerState.settledPage)
    }

    GridView(
        isOpenedDetailPage = isOpenedDetailView,
        lazyGridState = lazyGridState,
        pagerState = pagerState,
        photos = photos,
        onClickPhoto = { page ->
            scope.launch { pagerState.scrollToPage(page) }
            isOpenedDetailView = true
        }
    )

    AnimatedVisibility(
        visible = isOpenedDetailView,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        AnimatedVisibilityScopeProvider(this) {
            DetailView(
                pagerState = pagerState,
                photos = photos,
                dismiss = { isOpenedDetailView = false },
            )
        }
    }
}

@Composable
private fun GridView(
    isOpenedDetailPage: Boolean,
    lazyGridState: LazyGridState,
    pagerState: PagerState,
    photos: List<Photo>,
    onClickPhoto: (Int) -> Unit,
) {
    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(photos.size) { page ->
            AnimatedVisibility(
                visible = !isOpenedDetailPage || page != pagerState.settledPage,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                AnimatedVisibilityScopeProvider(this) {
                    AsyncImage(
                        photos[page].thumbnail.toString(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .easySharedElement(
                                PhotoSharedTransitionKey(
                                    photo = photos[page],
                                    isTransitionEnabled = (page == pagerState.settledPage && !pagerState.isScrollInProgress) || !isOpenedDetailPage,
                                ),
                            )
                            .clickable { onClickPhoto(page) }
                            .aspectRatio(1f),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailView(
    pagerState: PagerState,
    photos: List<Photo>,
    dismiss: () -> Unit,
) {
    val dismissOffsetY = 100.dp
    val scope = rememberCoroutineScope()

    val backgroundAlpha = remember { Animatable(1f) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = backgroundAlpha.value))
        )

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = !LocalSharedTransitionScope.current.isTransitionActive,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            var detectedSwipeDirection by remember { mutableStateOf<SwipeDirection?>(null) }
            val offsetX = remember { Animatable(0f) }
            val offsetY = remember { Animatable(0f) }
            val scale = remember { Animatable(1f) }

            photos[page].let { photo ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onVerticalDrag = { change, dragAmountY ->
                                    val dragAmountX = change.positionChange().x

                                    if (detectedSwipeDirection == null) {
                                        // 最初のスワイプが縦方向である、もしくは横方向の微スワイプである時、縦方向のスワイプとして判断
                                        detectedSwipeDirection =
                                            if (
                                                abs(dragAmountX) < abs(dragAmountY) ||
                                                (abs(dragAmountX).toDp() < 100.dp && abs(dragAmountY) > 0)
                                            ) {
                                                SwipeDirection.VERTICAL
                                            } else {
                                                SwipeDirection.HORIZONTAL
                                            }
                                    }

                                    when (detectedSwipeDirection) {
                                        SwipeDirection.VERTICAL -> {
                                            scope.launch {
                                                listOf(
                                                    async { offsetX.snapTo(offsetX.value + dragAmountX) },
                                                    async { offsetY.snapTo(offsetY.value + dragAmountY) },
                                                    async { scale.animateTo(0.8f) },
                                                    async { backgroundAlpha.animateTo(0f) },
                                                ).awaitAll()
                                            }
                                        }

                                        SwipeDirection.HORIZONTAL -> {
                                            change.consume()
                                        }

                                        null -> {}
                                    }
                                },
                                onDragEnd = {
                                    detectedSwipeDirection = null
                                    if (offsetY.value.toDp() > dismissOffsetY) {
                                        dismiss()
                                    } else {
                                        scope.launch {
                                            listOf(
                                                async { offsetX.animateTo(0f) },
                                                async { offsetY.animateTo(0f) },
                                                async { scale.animateTo(1f) },
                                                async { backgroundAlpha.animateTo(1f) },
                                            ).awaitAll()
                                        }
                                    }
                                }
                            )
                        },
                ) {
                    AsyncImage(
                        ImageRequest.Builder(LocalContext.current)
                            .data(photo.original.toString())
                            .placeholderMemoryCacheKey(photo.thumbnail.toString())
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .graphicsLayer {
                                translationX = offsetX.value
                                translationY = offsetY.value
                                scaleX = scale.value
                                scaleY = scale.value
                            }
                            .easySharedElement(
                                PhotoSharedTransitionKey(
                                    photo = photo,
                                    isTransitionEnabled = true,
                                ),
                                zIndexOverlay = if (pagerState.settledPage == page) 1f else 0f,
                            )
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}
