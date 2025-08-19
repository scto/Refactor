package com.github.scto.refactor.core.processors

import timber.log.Timber

class GradleGroovyToKtsProcessor : GeminiProcessor {
    override suspend fun process(input: GeminiProcessor.ProcessorInput) {
        Timber.tag("RefactorProcessor").d("Starting GradleGroovyToKts process...")
        Timber.tag("RefactorProcessor").d("GradleGroovyToKts process complete.")
    }
}
