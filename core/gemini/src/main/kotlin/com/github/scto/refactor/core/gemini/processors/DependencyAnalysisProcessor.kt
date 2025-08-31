package com.github.scto.refactor.core.gemini.processors

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import org.json.JSONObject

import java.io.File
import javax.inject.Inject

import com.github.scto.refactor.core.gemini.arch.Processor
import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.ui.RefactoringOption

/** Analysiert die Abhängigkeiten des Projekts und schlägt Aktualisierungen vor. */
class DependencyAnalysisProcessor @Inject constructor() : Processor {

    // KORREKTUR: Der Typ von 'id' wurde von String zu RefactoringOption geändert.
    override val id: RefactoringOption = RefactoringOption.ANALYZE_DEPENDENCIES
    override val name: String = "Abhängigkeiten analysieren"
    override val description: String =
        "Sucht nach veralteten Abhängigkeiten und schlägt neue Versionen vor."

    /**
     * KORREKTUR: Die Funktion gibt jetzt einen Flow<String> zurück, um der Processor-Schnittstelle
     * zu entsprechen. Status-Updates und Ergebnisse werden mit 'emit' gesendet.
     */
    override fun process(config: RefactoringConfig): Flow<String> =
        flow {
                val buildGradleFile = File(config.projectPath, "build.gradle.kts")
                if (!buildGradleFile.exists()) {
                    emit("Fehler: build.gradle.kts nicht gefunden.")
                    return@flow
                }

                emit("Abhängigkeiten werden analysiert...")
                val content = buildGradleFile.readText()
                val dependencies = extractDependencies(content)
                var suggestionsFound = false

                dependencies.forEach { (group, name, version) ->
                    val latestVersion = fetchLatestVersion(group, name)
                    if (latestVersion != null && latestVersion != version) {
                        emit(
                            "Empfehlung: Aktualisieren Sie '$group:$name' von Version '$version' auf '$latestVersion'."
                        )
                        suggestionsFound = true
                    }
                }

                if (!suggestionsFound) {
                    emit("Analyse abgeschlossen: Alle Abhängigkeiten sind auf dem neuesten Stand.")
                } else {
                    emit("Analyse abgeschlossen.")
                }
            }
            .flowOn(Dispatchers.IO) // Führt den gesamten Flow auf einem Hintergrundthread aus.

    private fun extractDependencies(content: String): List<Triple<String, String, String>> {
        val dependencies = mutableListOf<Triple<String, String, String>>()
        val dependencyRegex = """implementation\("([^:]+):([^:]+):([^"]+)"\)""".toRegex()
        dependencyRegex.findAll(content).forEach { matchResult ->
            val (group, name, version) = matchResult.destructured
            dependencies.add(Triple(group, name, version))
        }
        return dependencies
    }

    private suspend fun fetchLatestVersion(group: String, name: String): String? {
        val client = HttpClient(CIO)
        return try {
            val url =
                "https://search.maven.org/solrsearch/select?q=g:\"$group\"+AND+a:\"$name\"&rows=1&wt=json"
            val response: HttpResponse = client.get(url)
            val jsonResponse = JSONObject(response.bodyAsText())
            val docs = jsonResponse.getJSONObject("response").getJSONArray("docs")
            if (docs.length() > 0) {
                docs.getJSONObject(0).getString("latestVersion")
            } else {
                null
            }
        } catch (e: Exception) {
            println("Fehler beim Abrufen der neuesten Version für $group:$name: ${e.message}")
            null
        } finally {
            client.close()
        }
    }
}
