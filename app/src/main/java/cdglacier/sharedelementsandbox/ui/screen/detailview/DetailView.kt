package cdglacier.sharedelementsandbox.ui.screen.detailview

import android.os.Parcelable
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.pager.HorizontalPager
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cdglacier.sharedelementsandbox.ui.sharedtransition.LocalSharedTransitionScope
import cdglacier.sharedelementsandbox.ui.sharedtransition.easySharedElement
import cdglacier.sharedelementsandbox.unsplash.Photo
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.math.abs

@Serializable
@Parcelize
data class DetailView(
    val page: Int,
) : Parcelable

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailViewScreen(
    lazyGridState: LazyGridState,
    initialPage: Int,
    photos: List<Photo>,
    dismiss: () -> Unit,
) {
    val dismissOffsetY = 100.dp

    val pagerState = rememberPagerState(initialPage = initialPage) { photos.size }
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        // PagerでGridViewで表示している領域外にまで行ってしまうとShared Element Transitionが起こらないので位置を同期している
        lazyGridState.scrollToItem(pagerState.currentPage)
    }

    HorizontalPager(
        state = pagerState,
        // 遷移中はHorizontalPagerを動かせないようにする.
        // そこまで待ちたくないよって場合は、Modifier.sharedElementのboundsTransformをいじって遷移アニメーションのスピードをあげてあげるほうが良い気がします
        userScrollEnabled = !LocalSharedTransitionScope.current.isTransitionActive,
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
                                        // サイズを少し小さくしながら移動させる
                                        scope.launch {
                                            listOf(
                                                async { offsetX.snapTo(offsetX.value + dragAmountX) },
                                                async { offsetY.snapTo(offsetY.value + dragAmountY) },
                                                async { scale.animateTo(0.8f) },
                                            ).awaitAll()
                                        }
                                    }

                                    SwipeDirection.HORIZONTAL -> {
                                        // Pagerが動くように
                                        change.consume()
                                    }

                                    null -> {}
                                }
                            },
                            onDragEnd = {
                                detectedSwipeDirection = null
                                if (offsetY.value.toDp() > dismissOffsetY) {
                                    // 閾値を超えるとdismissする
                                    dismiss()
                                } else {
                                    // 閾値を超えなかった場合、元の位置に戻す
                                    scope.launch {
                                        listOf(
                                            async { offsetX.animateTo(0f) },
                                            async { offsetY.animateTo(0f) },
                                            async { scale.animateTo(1f) },
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
                        // original画像のキャッシュがない時、Placeholderが遷移アニメーションをしてしまう
                        // それを防ぐためにキャッシュがない時は、サムネイル画像を表示している
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
                        .easySharedElement(key = photo)
                        .fillMaxWidth()
                )
            }
        }
    }
}

private enum class SwipeDirection {
    VERTICAL,
    HORIZONTAL
}
