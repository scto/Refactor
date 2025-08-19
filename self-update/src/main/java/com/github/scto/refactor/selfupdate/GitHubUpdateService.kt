package com.github.scto.refactor.selfupdate

import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubUpdateService {
    @GET("repos/{user}/{repo}/releases")
    suspend fun getReleases(@Path("user") user: String, @Path("repo") repo: String): List<GitHubRelease>
}
