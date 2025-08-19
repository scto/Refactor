package com.github.scto.refactor.core.processors

import java.io.File

interface GeminiProcessor {
    data class ProcessorInput(
        val projectPath: File,
        val oldPackageName: String?,
        val newPackageName: String?,
        val apiKey: String
    )
        
    suspend fun process(input: ProcessorInput)
}
