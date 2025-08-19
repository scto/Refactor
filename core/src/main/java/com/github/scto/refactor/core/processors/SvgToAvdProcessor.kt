package com.github.scto.refactor.core.processors

import timber.log.Timber

class SvgToAvdProcessor : GeminiProcessor {
    override suspend fun process(input: GeminiProcessor.ProcessorInput) {
        Timber.tag("RefactorProcessor").d("Starting SvgToAvd process...")
        Timber.tag("RefactorProcessor").d("SvgToAvd process complete.")
    }
}
