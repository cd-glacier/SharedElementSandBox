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