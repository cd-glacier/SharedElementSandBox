package cdglacier.sharedelementsandbox.ui.screen.gridview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import cdglacier.sharedelementsandbox.ui.sharedtransition.easySharedElement
import cdglacier.sharedelementsandbox.unsplash.Photo
import coil.compose.AsyncImage
import kotlinx.serialization.Serializable

@Serializable
object GridView

@Composable
fun GridViewScreen(
    lazyGridState: LazyGridState,
    photos: List<Photo>,
    onClickPhoto: (Int) -> Unit,
) {
    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(3),
    ) {
        items(photos.size) { index ->
            AsyncImage(
                photos[index].thumbnail.toString(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .easySharedElement(key = photos[index])
                    .clickable { onClickPhoto(index) }
                    .aspectRatio(1f),
            )
        }
    }
}