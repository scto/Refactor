package com.github.scto.refactor.core.gemini.processors

import com.github.scto.refactor.core.gemini.arch.Processor
import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.ui.RefactoringOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

object XmlToComposeProcessor : Processor {
   // KORRIGIERT: ID an den neuen Enum-Namen angepasst.
   override val id = RefactoringOption.XML_TO_COMPOSE
   override val name: String = "XML zu Compose Konverter"
   override val description: String = "Konvertiert XML-Layouts in Jetpack Compose."

   override fun process(config: RefactoringConfig): Flow<String> = flow {
       emit("Starte Konvertierung von XML-Layouts zu Jetpack Compose...")
       val layoutDir = config.projectRoot.resolve("app/src/main/res/layout")
       if (!layoutDir.exists() || !layoutDir.isDirectory) {
           emit("Kein 'layout'-Verzeichnis gefunden. Überspringe.")
           return@flow
       }

       val xmlFiles = layoutDir.listFiles { _, name -> name.endsWith(".xml") }
       if (xmlFiles.isNullOrEmpty()) {
           emit("Keine XML-Layout-Dateien gefunden.")
           return@flow
       }

       emit("${xmlFiles.size} Layout-Dateien gefunden.")
       val composeDir = config.projectRoot.resolve("app/src/main/java/com/github/scto/refactor/ui/converted").apply { mkdirs() }

       for (file in xmlFiles) {
           emit("  -> Verarbeite ${file.name}...")
           val xmlContent = file.readText()
           val result = GeminiProcessor.convertXmlToCompose(xmlContent)

           result.fold(
               onSuccess = { composeCode ->
                   val newFileName = file.nameWithoutExtension.replaceFirstChar { it.titlecase() } + "Screen.kt"
                   val newFile = File(composeDir, newFileName)
                   newFile.writeText("package com.github.scto.refactor.ui.converted\n\n$composeCode")
                   emit("     Erfolgreich nach ${newFile.name} konvertiert.")
               },
               onFailure = { error ->
                   emit("     FEHLER bei ${file.name}: ${error.localizedMessage}")
               }
           )
       }
   }
}