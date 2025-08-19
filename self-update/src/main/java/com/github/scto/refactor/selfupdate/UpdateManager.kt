package com.github.scto.refactor.selfupdate

import android.content.Context
import timber.log.Timber

class UpdateManager(private val context: Context) {

    private val updateChecker = UpdateChecker(context)
    private val updateDownloader = UpdateDownloader(context)

    suspend fun checkForUpdates(githubUser: String, githubRepo: String) {
        Timber.d("Checking for updates for $githubUser/$githubRepo")
        try {
            val latestRelease = updateChecker.getLatestRelease(githubUser, githubRepo)
            if (latestRelease == null) {
                Timber.w("No release information found.")
                return
            }

            val currentVersion = updateChecker.getCurrentAppVersion()
            val latestVersion = latestRelease.tagName.removePrefix("v")
            Timber.d("Current version: $currentVersion, Latest version: $latestVersion")

            if (isNewerVersion(latestVersion, currentVersion)) {
                Timber.i("New version available: $latestVersion")
                val apkAsset = latestRelease.assets.find { it.name.endsWith(".apk") }
                if (apkAsset != null) {
                    updateDownloader.downloadAndInstall(apkAsset.downloadUrl, apkAsset.name)
                } else {
                    Timber.e("No APK found in the latest release.")
                }
            } else {
                Timber.i("App is up to date.")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error checking for updates.")
        }
    }

    private fun isNewerVersion(latestVersion: String, currentVersion: String): Boolean {
        val latestParts = latestVersion.split(".").map { it.toIntOrNull() ?: 0 }
        val currentParts = currentVersion.split(".").map { it.toIntOrNull() ?: 0 }

        val maxParts = maxOf(latestParts.size, currentParts.size)

        for (i in 0 until maxParts) {
            val latestPart = latestParts.getOrElse(i) { 0 }
            val currentPart = currentParts.getOrElse(i) { 0 }
            if (latestPart > currentPart) return true
            if (latestPart < currentPart) return false
        }
        return false // Versions are identical
    }
}
