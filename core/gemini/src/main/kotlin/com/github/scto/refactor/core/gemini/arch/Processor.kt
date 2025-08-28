package com.github.scto.refactor.core.gemini.arch

import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.ui.RefactoringOption

import kotlinx.coroutines.flow.Flow

interface Processor {
    val id: RefactoringOption
    val name: String
    val description: String
    fun process(config: RefactoringConfig): Flow<String>
}