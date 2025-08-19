package com.github.scto.refactor.core.processors

import timber.log.Timber

class CodeAnalysisProcessor : GeminiProcessor {
    override suspend fun process(input: GeminiProcessor.ProcessorInput) {
        Timber.tag("RefactorProcessor").d("Starting CodeAnalysis process...")
        Timber.tag("RefactorProcessor").d("CodeAnalysis process complete.")
    }
}
