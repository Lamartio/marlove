package io.lamart.mocky.domain

import kotlinx.serialization.Serializable

@Serializable
data class ArtCollection(
    val artObjects: List<ArtObject>,
) {

    @Serializable
    data class ArtObject(
        val hasImage: Boolean,
        val headerImage: HeaderImage,
        val id: String,
        val links: Links,
        val longTitle: String,
        val objectNumber: String,
        val permitDownload: Boolean,
        val principalOrFirstMaker: String,
        val productionPlaces: List<String>,
        val showImage: Boolean,
        val title: String,
        val webImage: WebImage?,
    )

    @Serializable
    data class HeaderImage(
        val guid: String,
        val height: Int,
        val offsetPercentageX: Int,
        val offsetPercentageY: Int,
        val url: String,
        val width: Int
    )

    @Serializable
    data class Links(
        val self: String,
        val web: String
    )

    @Serializable
    data class WebImage(
        val guid: String,
        val height: Int,
        val offsetPercentageX: Int,
        val offsetPercentageY: Int,
        val url: String,
        val width: Int
    )
}