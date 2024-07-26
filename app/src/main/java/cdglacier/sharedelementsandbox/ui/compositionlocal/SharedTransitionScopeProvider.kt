package cdglacier.sharedelementsandbox.ui.compositionlocal

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope> {
    error("Not Initialized SharedTransitionScope")
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScopeProvider(
    content: @Composable () -> Unit,
) {
    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.easySharedElement(key: Any, zIndexOverlay: Float = 0f): Modifier {
    with(LocalSharedTransitionScope.current) {
        return this@easySharedElement.sharedElement(
            state = rememberSharedContentState(key),
            animatedVisibilityScope = LocalAnimatedVisibilityScope.current,
            zIndexInOverlay = zIndexOverlay,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.easySharedBounds(key: Any): Modifier {
    with(LocalSharedTransitionScope.current) {
        return this@easySharedBounds.sharedBounds(
            rememberSharedContentState(key),
            animatedVisibilityScope = LocalAnimatedVisibilityScope.current,
        )
    }
}

