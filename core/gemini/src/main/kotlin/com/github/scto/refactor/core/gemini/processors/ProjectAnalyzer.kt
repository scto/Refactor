package com.github.scto.refactor.core.gemini.processors // KORRIGIERT

import java.io.File

object ProjectAnalyzer {
    fun findSubmodules(projectRoot: File): List<String> {
        val settingsFile = File(projectRoot, "settings.gradle")
        val settingsKtsFile = File(projectRoot, "settings.gradle.kts")

        val fileToParse =
            when {
                settingsKtsFile.exists() -> settingsKtsFile
                settingsFile.exists() -> settingsFile
                else -> return emptyList()
            }

        val moduleRegex = Regex("""include\s*\(?['"]:(.*?)['"]\)?'""")
        return fileToParse.readLines().mapNotNull { line ->
            moduleRegex.find(line)?.groupValues?.get(1)
        }
    }
}
