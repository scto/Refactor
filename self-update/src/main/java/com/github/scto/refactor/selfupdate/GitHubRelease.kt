package com.github.scto.refactor.selfupdate

import com.google.gson.annotations.SerializedName

data class GitHubRelease(
    @SerializedName("tag_name") val tagName: String,
    @SerializedName("assets") val assets: List<Asset>
) {
    data class Asset(
        @SerializedName("browser_download_url") val downloadUrl: String,
        @SerializedName("name") val name: String
    )
}
