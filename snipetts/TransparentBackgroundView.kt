@Composable
fun TransparentBackgroundViewScreen(
    photos: List<Photo>,
) {
    var isOpenedDetailView by remember { mutableStateOf(false) }
    GridView(...)
    AnimatedVisibility(
        visible = isOpenedDetailView,
    ) {
        AnimatedVisibilityScopeProvider(this) {
            DetailView(...)
        }
    }
}
