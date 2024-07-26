package cdglacier.sharedelementsandbox.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

@Serializable
object Home

@Composable
fun HomeScreen(
    onClickSimpleSharedElementExampleButton: () -> Unit,
    onClickSimpleSharedBoundsExampleButton: () -> Unit,
    onClickListWithAnimatedVisibilityButton: () -> Unit,
    onClickVisualJumpWithModifierOrderButton: () -> Unit,
    onClickCallerManagedVisibilityButton: () -> Unit,
    onClickAnimationWithBoundsTransformButton: () -> Unit,
    onClickResizeModeButton: () -> Unit,
    onClickSkipToLookAheadSizeButton: () -> Unit,
    onClickRenderInSharedTransitionScopeOverlayButton: () -> Unit,
    onClickPlaceHolderSizeButton: () -> Unit,
    onClickCoilCacheKeyButton: () -> Unit,
    onClickFontSizeButton: () -> Unit,
    onClickGridViewButton: () -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = innerPadding,
        ) {
            item {
                Button(
                    onClick = onClickSimpleSharedElementExampleButton,
                ) {
                    Text("Simple SharedElement Example")
                }
            }
            item {
                Button(
                    onClick = onClickSimpleSharedBoundsExampleButton,
                ) {
                    Text("Simple SharedBounds Example")
                }
            }
            item {
                Button(
                    onClick = onClickListWithAnimatedVisibilityButton,
                ) {
                    Text("List with AnimatedVisibility")
                }
            }
            item {
                Button(
                    onClick = onClickVisualJumpWithModifierOrderButton,
                ) {
                    Text("Visual Jump Example")
                }
            }
            item {
                Button(
                    onClick = onClickCallerManagedVisibilityButton,
                ) {
                    Text("With CallerManagedVisibility")
                }
            }
            item {
                Button(
                    onClick = onClickAnimationWithBoundsTransformButton,
                ) {
                    Text("Animation with BoundsTransform")
                }
            }
            item {
                Button(
                    onClick = onClickResizeModeButton,
                ) {
                    Text("Compare ResizeMode")
                }
            }
            item {
                Button(
                    onClick = onClickSkipToLookAheadSizeButton,
                ) {
                    Text("Compare skipToLookAheadSize modifier")
                }
            }
            item {
                Button(
                    onClick = onClickRenderInSharedTransitionScopeOverlayButton,
                ) {
                    Text("Compare renderInSharedTransitionScopeOverlay modifier")
                }
            }
            item {
                Button(
                    onClick = onClickPlaceHolderSizeButton,
                ) {
                    Text("Compare PlaceHolderSize Parameter")
                }
            }
            item {
                Button(
                    onClick = onClickCoilCacheKeyButton,
                ) {
                    Text("Compare Coil placeholderMemoryCacheKey")
                }
            }
            item {
                Button(
                    onClick = onClickFontSizeButton
                ) {
                    Text("Compare Font Size ResizeMode")
                }
            }
            item {
                Button(
                    onClick = onClickGridViewButton
                ) {
                    Text("GridView")
                }
            }
        }
    }
}