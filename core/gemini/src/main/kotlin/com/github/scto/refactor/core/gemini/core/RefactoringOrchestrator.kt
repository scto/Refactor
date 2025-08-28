package com.github.scto.refactor.core.gemini.core

import com.github.scto.refactor.core.gemini.arch.Processor
import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.ui.RefactoringOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RefactoringOrchestrator(private val processors: List<Processor>) {
    fun start(config: RefactoringConfig): Flow<String> = flow {
        emit("Refactoring-Prozess gestartet...")

        val activeOptions = config.getActiveOptions()
        if (activeOptions.isEmpty()) {
            emit("Keine Refactoring-Optionen ausgewählt. Prozess beendet.")
            return@flow
        }

        processors.forEach { processor ->
            if (processor.id in activeOptions) {
                emit("--- Starte: ${processor.name} ---")
                processor.process(config).collect { logMessage -> emit(logMessage) }
                emit("--- Beendet: ${processor.name} ---")
            }
        }
    }

    private fun RefactoringConfig.getActiveOptions(): Set<RefactoringOption> {
        return buildSet {
            if (analyzeDependencies) add(RefactoringOption.ANALYZE_DEPENDENCIES)
            if (refactorGradle) add(RefactoringOption.REFACTOR_GRADLE)
            if (extractStrings) add(RefactoringOption.EXTRACT_STRINGS)
            if (refactorJavaToKotlin) add(RefactoringOption.REFACTOR_JAVA_TO_KOTLIN)
            if (analyzeKotlinStyle) add(RefactoringOption.ANALYZE_KOTLIN_STYLE)
            if (refactorManifest) add(RefactoringOption.REFACTOR_MANIFEST)
            if (renamePackage) add(RefactoringOption.RENAME_PACKAGE)
            if (selfModernize) add(RefactoringOption.SELF_MODERNIZE)
            if (convertSvgToAvd) add(RefactoringOption.CONVERT_SVG_TO_AVD)
            if (refactorThemes) add(RefactoringOption.REFACTOR_THEMES)
            // KORRIGIERT: An die neuen Enum-Namen angepasst
            if (xmlToCompose) add(RefactoringOption.XML_TO_COMPOSE)
            if (codeAnalysis) add(RefactoringOption.CODE_ANALYSIS)
        }
    }
}
