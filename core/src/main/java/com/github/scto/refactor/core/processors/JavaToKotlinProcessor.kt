package com.github.scto.refactor.core.processors

import timber.log.Timber

class JavaToKotlinProcessor : GeminiProcessor {
    override suspend fun process(input: GeminiProcessor.ProcessorInput) {
        Timber.tag("RefactorProcessor").d("Starting JavaToKotlin process...")
        Timber.tag("RefactorProcessor").d("JavaToKotlin process complete.")
    }
}
