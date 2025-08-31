package com.github.scto.refactor.core.gemini.processors

import com.github.scto.refactor.core.gemini.arch.Processor
import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.ui.RefactoringOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object HardcodedStringProcessor : Processor {
    override val id = RefactoringOption.EXTRACT_STRINGS
    override val name: String = "Analyse für hartcodierte Strings"
    override val description: String =
        "Findet hartcodierte Texte, die in `strings.xml` extrahiert werden sollten."

    // VERBESSERT: Regex ist nun spezifischer, um False-Positives zu vermeiden.
    private val xmlStringRegex = Regex("""android:text\s*=\s*"([^@\n]{5,})"""")
    private val kotlinStringRegex = Regex("\"(.{8,})\"") // Sucht nach längeren Strings.
    private val commonFalsePositives =
        listOf("import", "@", "Timber", "Log.", "http://", "https://", "TAG", "=")

    override fun process(config: RefactoringConfig): Flow<String> = flow {
        emit("Starte Suche nach hartcodierten Strings...")
        val projectFiles =
            config.projectRoot
                .walk()
                .filter { it.isFile && (it.extension == "kt" || it.extension == "xml") }
                .toList()
        var issuesFound = 0

        projectFiles.forEach { file ->
            when (file.extension) {
                "xml" -> {
                    file.readLines().forEach { line ->
                        xmlStringRegex.findAll(line).forEach { match ->
                            issuesFound++
                            val hardcodedString = match.groupValues[1]
                            emit(
                                "  -> VORSCHLAG in ${file.name}: Hartcodierter Text gefunden: \"$hardcodedString\"."
                            )
                            emit(
                                "     Extrahiere dies in `strings.xml`: <string name=\"example_name\">$hardcodedString</string>"
                            )
                        }
                    }
                }
                "kt" -> {
                    file.readLines().forEachIndexed { index, line ->
                        // VERBESSERT: Zusätzliche Filter, um irrelevante Zeilen zu ignorieren.
                        val trimmedLine = line.trim()
                        if (commonFalsePositives.none { trimmedLine.contains(it) }) {
                            kotlinStringRegex.findAll(line).forEach { match ->
                                issuesFound++
                                val hardcodedString = match.groupValues[1]
                                emit(
                                    "  -> VORSCHLAG in ${file.name} (Zeile ${index + 1}): Potenziell hartcodierter String: \"$hardcodedString\"."
                                )
                                emit(
                                    "     Prüfen, ob `context.getString(R.string.example_name)` verwendet werden sollte."
                                )
                            }
                        }
                    }
                }
            }
        }
        if (issuesFound == 0) {
            emit("Keine offensichtlichen hartcodierten Strings gefunden.")
        } else {
            emit("$issuesFound potenzielle Probleme mit hartcodierten Strings gefunden.")
        }
    }
}
