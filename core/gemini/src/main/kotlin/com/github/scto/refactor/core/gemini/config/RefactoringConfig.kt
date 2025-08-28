package com.github.scto.refactor.core.gemini.config

import java.io.File

data class RefactoringConfig(
    val projectPath: String,
    val oldPackageName: String,
    val newPackageName: String,
    val analyzeDependencies: Boolean = false,
    val refactorGradle: Boolean = false,
    val extractStrings: Boolean = false,
    val refactorJavaToKotlin: Boolean = false,
    val analyzeKotlinStyle: Boolean = false,
    val refactorManifest: Boolean = false,
    val renamePackage: Boolean = false,
    val selfModernize: Boolean = false,
    val convertSvgToAvd: Boolean = false,
    val refactorThemes: Boolean = false,
    val xmlToCompose: Boolean = false,
    val codeAnalysis: Boolean = false
) {
    val projectRoot: File by lazy {
        if (projectPath.isBlank()) {
            throw IllegalArgumentException("Projektpfad darf nicht leer sein.")
        }
        File(projectPath)
    }
}