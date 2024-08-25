package cdglacier.sharedelementsandbox.ui.sharedtransition

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAnimatedVisibilityScope = staticCompositionLocalOf<AnimatedVisibilityScope> {
    error("Not Initialized AnimatedVisibilityScope")
}

@Composable
fun AnimatedVisibilityScopeProvider(
    animatedVisibilityScope: AnimatedVisibilityScope,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAnimatedVisibilityScope provides animatedVisibilityScope,
    ) {
        content()
    }
}
