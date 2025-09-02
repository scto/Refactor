/*
 * Copyright 2025, S.C.T.O
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.scto.refactor.core.gemini.model

/**
 * Represents a structured result from an analysis processor.
 *
 * @param processorName The name of the processor that generated this result.
 * @param finding The title or core finding.
 * @param details A more detailed description and potential solutions.
 */
data class AnalysisResult(val processorName: String, val finding: String, val details: String)
