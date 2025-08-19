package com.github.scto.refactor.core.processors

import timber.log.Timber

class ManifestProcessor : GeminiProcessor {
    override suspend fun process(input: GeminiProcessor.ProcessorInput) {
        Timber.tag("RefactorProcessor").d("Starting Manifest process...")
        Timber.tag("RefactorProcessor").d("Manifest process complete.")
    }
}
