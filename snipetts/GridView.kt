@Composable
fun GridViewScreen(
    photos: List<Photo>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
    ) {
        items(photos.size) { index ->
            AsyncImage(
                photos[index].thumbnail.toString(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.aspectRatio(1f),
            )
        }
    }
}