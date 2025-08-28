package com.github.scto.refactor.selfupdate

import android.content.Context
import android.content.pm.PackageManager

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

import kotlinx.serialization.json.Json

import timber.log.Timber

internal class UpdateChecker(private val context: Context) {

    // Ktor HttpClient-Instanz
    private val client: HttpClient by lazy {
        HttpClient(CIO) {
            // Plugin für die JSON-Serialisierung
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true // Ignoriert unbekannte Felder in der JSON-Antwort
                    prettyPrint = true
                })
            }
            // Plugin für das Logging von Anfragen und Antworten
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.tag("Ktor").d(message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    }

    /**
     * Ruft das neueste Release von GitHub ab.
     */
    suspend fun getLatestRelease(user: String, repo: String): GitHubRelease? {
        return try {
            val releases: List<GitHubRelease> = client.get("https://api.github.com/repos/$user/$repo/releases").body()
            releases.firstOrNull() // Das erste Element in der Liste ist das Neueste
        } catch (e: Exception) {
            Timber.e(e, "Fehler beim Abrufen der Releases von GitHub.")
            null
        }
    }

    /**
     * Gibt die aktuelle Version der App zurück.
     */
    fun getCurrentAppVersion(): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            // KORREKTUR: Fügt einen Fallback-Wert hinzu, falls versionName null ist.
            packageInfo.versionName ?: "0.0.0"
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e, "Paketinformationen konnten nicht abgerufen werden.")
            "0.0.0"
        }
    }
}

/*
package com.github.scto.refactor.selfupdate

import android.content.Context
import com.github.scto.refactor.data.local.UserPreferencesRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.flow.first

class UpdateChecker(
    private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend fun checkForUpdate(): GitHubRelease? {
        val client = HttpClient(Android) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
                    },
                )
            }
        }

        return try {
            val prefs = userPreferencesRepository.userPreferences.first()
            val owner = prefs.githubOwner
            val repo = prefs.githubRepo
            val url = "https://api.github.com/repos/$owner/$repo/releases/latest"

            val release: GitHubRelease = client.get(url)
            val currentVersion =
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            if (release.tagName != currentVersion) {
                release
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            client.close()
        }
    }
}
*/