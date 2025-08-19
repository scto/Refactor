package com.github.scto.refactor.core.processors

import timber.log.Timber

class XmlToComposeProcessor : GeminiProcessor {
    override suspend fun process(input: GeminiProcessor.ProcessorInput) {
        Timber.tag("RefactorProcessor").d("Starting XmlToCompose process...")
        Timber.tag("RefactorProcessor").d("XmlToCompose process complete.")
    }
}
