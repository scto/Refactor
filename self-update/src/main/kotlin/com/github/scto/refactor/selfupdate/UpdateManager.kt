package com.github.scto.refactor.selfupdate

import android.content.Context
import timber.log.Timber

class UpdateManager(private val context: Context) {

    private val updateChecker = UpdateChecker(context)
    private val updateDownloader = UpdateDownloader(context)

    suspend fun checkForUpdates(githubUser: String, githubRepo: String) {
        Timber.d("Suche nach Updates für $githubUser/$githubRepo")
        try {
            val latestRelease = updateChecker.getLatestRelease(githubUser, githubRepo)
            if (latestRelease == null) {
                Timber.w("Keine Release-Informationen gefunden.")
                return
            }

            val currentVersion = updateChecker.getCurrentAppVersion()
            // Entfernt das "v"-Präfix aus dem Tag-Namen, um eine saubere Versionsnummer zu erhalten
            val latestVersion = latestRelease.tagName.removePrefix("v")
            Timber.d("Aktuelle Version: $currentVersion, Neueste Version: $latestVersion")

            if (isNewerVersion(latestVersion, currentVersion)) {
                Timber.i("Neue Version verfügbar: $latestVersion")
                val apkAsset = latestRelease.assets.find { it.name.endsWith(".apk") }
                if (apkAsset != null) {
                    updateDownloader.downloadAndInstall(apkAsset.downloadUrl, apkAsset.name)
                } else {
                    Timber.e("Keine APK im neuesten Release gefunden.")
                }
            } else {
                Timber.i("Die App ist auf dem neuesten Stand.")
            }
        } catch (e: Exception) {
            Timber.e(e, "Fehler bei der Update-Prüfung.")
        }
    }

    /**
     * Vergleicht zwei Versions-Strings.
     *
     * @return true, wenn latestVersion neuer ist als currentVersion.
     */
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
        return false // Versionen sind identisch
    }
}
