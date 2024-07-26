@Composable
private fun GridView(...) {
    LazyVerticalGrid(...) {
        items(photos.size) { page ->
            AnimatedVisibility(
                visible = !isOpenedDetailPage || page != pagerState.settledPage,
            ) {
                AnimatedVisibilityScopeProvider(this) {
                    AsyncImage(
                        photos[page].thumbnail.toString(),
                        modifier = Modifier
                            .easySharedElement(
                                key = photos[page],
                            )
                    )
                }
                ...
