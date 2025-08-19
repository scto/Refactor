package com.github.scto.refactor.selfupdate

import android.content.Context
import android.content.pm.PackageManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

internal class UpdateChecker(private val context: Context) {

    private val gitHubService: GitHubUpdateService by lazy {
        val logging = HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubUpdateService::class.java)
    }

    suspend fun getLatestRelease(user: String, repo: String): GitHubRelease? {
        return try {
            val releases = gitHubService.getReleases(user, repo)
            releases.firstOrNull() // The first one is the latest
        } catch (e: Exception) {
            Timber.e(e, "Failed to fetch releases from GitHub.")
            null
        }
    }

    fun getCurrentAppVersion(): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e, "Could not get package info.")
            "0.0.0"
        }
    }
}
