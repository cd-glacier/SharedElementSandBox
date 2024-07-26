package cdglacier.sharedelementsandbox.unsplash

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val PhotoNavType = object : NavType<Photo>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Photo? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Photo::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }

    override fun put(bundle: Bundle, key: String, value: Photo) = bundle.putParcelable(key, value)

    override fun parseValue(value: String): Photo = Json.decodeFromString(value)

    override fun serializeAsValue(value: Photo): String = Json.encodeToString(value)
}

@Serializable
@Parcelize
data class Photo(
    val id: String,
) : Parcelable {
    val thumbnail: Uri
        get() = Uri.Builder()
            .scheme("https")
            .authority("images.unsplash.com")
            .appendPath(id)
            .appendQueryParameter("w", "400")
            .appendQueryParameter("q", "60")
            .build()

    val original: Uri
        get() = Uri.Builder()
            .scheme("https")
            .authority("images.unsplash.com")
            .path(id)
            .build()
}

val photos = listOf(
    Photo(id = "photo-1722669932944-b12f703ffd28"),
    Photo(id = "photo-1722623853927-76deff057f8f"),
    Photo(id = "photo-1612828898781-9c53031d0420"),
    Photo(id = "photo-1721819506270-af50411fc152"),
    Photo(id = "photo-1721367629962-bbebd724203e"),
    Photo(id = "photo-1719739183158-c1f8f455511e"),
    Photo(id = "photo-1499856871958-5b9627545d1a"),
    Photo(id = "photo-1547636032-71f4904ed26b"),
    Photo(id = "photo-1607321740491-40e204947580"),
    Photo(id = "photo-1547756536-cde3673fa2e5"),
    Photo(id = "photo-1564499504739-bc4fc2ae8cba"),
    Photo(id = "photo-1586890443582-f7c9cb412c82"),
    Photo(id = "photo-1723489569903-e0d6bb38c04f"),
    Photo(id = "photo-1719931626206-d3874333fd00"),
    Photo(id = "photo-1723366086577-7d12151985a8"),
    Photo(id = "photo-1723231763164-f9df1b7fa159"),
    Photo(id = "photo-1723121245386-b2137fced28f"),
    Photo(id = "photo-1704580615544-ffb922e61f27"),
    Photo(id = "photo-1551535740-74735c5af161"),
    Photo(id = "photo-1515724684585-ec93c008b8b5"),
    Photo(id = "photo-1707329195495-6270c111e5c3"),
    Photo(id = "photo-1722194006398-f9e0c1f8311c"),
    Photo(id = "photo-1722168494271-a77b772c6ce1"),
    Photo(id = "photo-1722113515300-bd539edc0c67"),
    Photo(id = "photo-1721990570392-e4ff4f442b4c"),
)

