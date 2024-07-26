@Composable
fun Modifier.easySharedElement(key: Any): Modifier {
    with(LocalSharedTransitionScope.current) {
        return this@easySharedElement.sharedElement(
            state = rememberSharedContentState(key),
            animatedVisibilityScope = LocalAnimatedVisibilityScope.current,
        )
    }
}
