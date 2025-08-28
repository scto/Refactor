package com.github.scto.refactor.selfupdate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Annotations für Kotlinx Serialization hinzugefügt
@Serializable
data class GitHubRelease(
    @SerialName("tag_name") val tagName: String,
    @SerialName("assets") val assets: List<Asset>,
) {
    @Serializable
    data class Asset(
        @SerialName("browser_download_url") val downloadUrl: String,
        @SerialName("name") val name: String,
    )
}
