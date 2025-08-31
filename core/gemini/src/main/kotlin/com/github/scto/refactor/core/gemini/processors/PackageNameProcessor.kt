package com.github.scto.refactor.core.gemini.processors // KORRIGIERT

import com.github.scto.refactor.core.gemini.arch.Processor
import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.ui.RefactoringOption
import java.io.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

object PackageNameProcessor : Processor {
    override val id = RefactoringOption.RENAME_PACKAGE
    override val name: String = "Paketnamen-Refactoring"
    override val description: String =
        "Benennt Paketnamen in allen relevanten Dateien und Verzeichnissen um."

    override fun process(config: RefactoringConfig): Flow<String> = flow {
        if (config.oldPackageName.isBlank() || config.newPackageName.isBlank()) {
            emit("FEHLER: Alter und neuer Paketname dürfen nicht leer sein. Überspringe.")
            Timber.tag("PackageNameProcessor")
                .d("FEHLER: Alter und neuer Paketname dürfen nicht leer sein. Überspringe.")
            return@flow
        }

        emit("Starte Umbenennung von '${config.oldPackageName}' zu '${config.newPackageName}'...")
        Timber.tag("PackageNameProcessor")
            .d("Starte Umbenennung von '${config.oldPackageName}' zu '${config.newPackageName}'...")
        val oldPath = config.oldPackageName.replace('.', File.separatorChar)
        val newPath = config.newPackageName.replace('.', File.separatorChar)

        config.projectRoot.walkTopDown().forEach { file ->
            if (file.isFile && file.extension in listOf("java", "kt", "xml", "gradle", "kts")) {
                val content = file.readText()
                if (content.contains(config.oldPackageName)) {
                    file.writeText(content.replace(config.oldPackageName, config.newPackageName))
                    emit("  -> Inhalt von ${file.name} aktualisiert.")
                    Timber.tag("PackageNameProcessor").d("Inhalt von ${file.name} aktualisiert.")
                }
            }
        }

        emit("Benenne Verzeichnisstruktur um...")
        Timber.tag("PackageNameProcessor").d("Benenne Verzeichnisstruktur um...")
        val sourceDirs =
            config.projectRoot.walkBottomUp().filter { it.isDirectory && it.path.contains(oldPath) }
        sourceDirs.forEach { dir ->
            val newDir = File(dir.path.replace(oldPath, newPath))
            if (dir.renameTo(newDir)) {
                emit("  -> Verzeichnis umbenannt: ${dir.name} -> ${newDir.name}")
                Timber.tag("PackageNameProcessor")
                    .d("Verzeichnis umbenannt: ${dir.name} -> ${newDir.name}")
            }
        }
    }
}
