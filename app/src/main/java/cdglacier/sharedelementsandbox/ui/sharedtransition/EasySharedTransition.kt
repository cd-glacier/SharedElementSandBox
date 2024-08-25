package cdglacier.sharedelementsandbox.ui.sharedtransition

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// SharedTransitionScope出なくても使えるようにしたModifier.sharedElement
// SharedTransitionScopeProvider, AnimatedVisibilityScopeProviderの中で呼び出さないとcrashする
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
