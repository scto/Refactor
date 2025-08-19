package com.github.scto.refactor.core.processors

import timber.log.Timber

class SelfModernizerProcessor : GeminiProcessor {
    override suspend fun process(input: GeminiProcessor.ProcessorInput) {
        Timber.tag("RefactorProcessor").d("Starting SelfModernizer process...")
        Timber.tag("RefactorProcessor").d("SelfModernizer process complete.")
    }
}
