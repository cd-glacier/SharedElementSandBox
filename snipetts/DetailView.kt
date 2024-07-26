@Composable
fun DetailViewScreen(photo: Photo) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            ImageRequest.Builder(LocalContext.current)
                .data(photo.original.toString())
                .placeholderMemoryCacheKey(photo.thumbnail.toString())
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
