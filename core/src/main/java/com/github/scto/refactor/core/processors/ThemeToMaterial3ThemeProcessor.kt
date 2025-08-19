package com.github.scto.refactor.core.processors

import timber.log.Timber

class ThemeToMaterial3ThemeProcessor : GeminiProcessor {
    override suspend fun process(input: GeminiProcessor.ProcessorInput) {
        Timber.tag("RefactorProcessor").d("Starting ThemeToMaterial3Theme process...")
        Timber.tag("RefactorProcessor").d("ThemeToMaterial3Theme process complete.")
    }
}
