package com.github.scto.refactor.core.gemini.model

/**
 * Represents a structured result from an analysis processor.
 *
 * @param processorName The name of the processor that generated this result.
 * @param finding The title or core finding.
 * @param details A more detailed description and potential solutions.
 */
data class AnalysisResult(val processorName: String, val finding: String, val details: String)
