package cdglacier.sharedelementsandbox.ui.sharedtransition

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope> {
    error("Not Initialized SharedTransitionScope")
}

// contentにCompositionLocalを使ってSharedTransitionScopeを提供する
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
