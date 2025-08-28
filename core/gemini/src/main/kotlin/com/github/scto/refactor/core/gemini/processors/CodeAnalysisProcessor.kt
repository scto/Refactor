package com.github.scto.refactor.core.gemini.processors

import com.github.scto.refactor.core.gemini.arch.Processor
import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.ui.RefactoringOption

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object CodeAnalysisProcessor : Processor {
   // KORRIGIERT: ID an den neuen Enum-Namen angepasst.
   override val id = RefactoringOption.CODE_ANALYSIS
   override val name: String = "Statische Code-Analyse"
   override val description: String = "Führt eine statische Code-Analyse mit Gemini durch."

   override fun process(config: RefactoringConfig): Flow<String> = flow {
       emit("Starte statische Code-Analyse...")
       val filesToAnalyze = config.projectRoot.walk()
           .filter { it.isFile && (it.extension == "kt" || it.extension == "java") }
           .toList()

       if (filesToAnalyze.isEmpty()) {
           emit("Keine zu analysierenden Dateien gefunden.")
           return@flow
       }

       emit("${filesToAnalyze.size} Dateien werden analysiert...")

       filesToAnalyze.forEach { file ->
           emit("  -> Analysiere ${file.name}...")
           val content = file.readText()
           val result = GeminiProcessor.analyzeCode(content)

           result.fold(
               onSuccess = { analysis ->
                   emit("     VORSCHLAG für ${file.name}:\n$analysis\n--------------------")
               },
               onFailure = { error ->
                   emit("     FEHLER bei der Analyse von ${file.name}: ${error.localizedMessage}")
               }
           )
       }
   }
}