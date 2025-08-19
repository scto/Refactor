package com.github.scto.refactor.core.processors

import timber.log.Timber

class KotlinStyleCheckerProcessor : GeminiProcessor {
    override suspend fun process(input: GeminiProcessor.ProcessorInput) {
        Timber.tag("RefactorProcessor").d("Starting KotlinStyleChecker process...")
        Timber.tag("RefactorProcessor").d("KotlinStyleChecker process complete.")
    }
}
