package com.github.scto.refactor.core.gemini.di

// import com.github.scto.refactor.core.gemini.processors.DependencyAnalysisProcessor
import com.github.scto.refactor.core.gemini.arch.Processor
import com.github.scto.refactor.core.gemini.core.RefactoringOrchestrator
import com.github.scto.refactor.core.gemini.processors.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeminiModule {

    @Provides
    @Singleton
    fun provideRefactoringProcessors(
        dependencyAnalysisProcessor: DependencyAnalysisProcessor,
        gradleProcessor: GradleProcessor,
        hardcodedStringProcessor: HardcodedStringProcessor,
        javaToKotlinProcessor: JavaToKotlinProcessor,
        kotlinStyleProcessor: KotlinStyleProcessor,
        manifestProcessor: ManifestProcessor,
        packageNameProcessor: PackageNameProcessor,
        selfModernizationProcessor: SelfModernizationProcessor,
        svgToAvdProcessor: SvgToAvdProcessor,
        themeProcessor: ThemeProcessor,
        xmlToComposeProcessor: XmlToComposeProcessor,
        codeAnalysisProcessor: CodeAnalysisProcessor,
        // Fügen Sie hier weitere Prozessoren hinzu
    ): List<Processor> {
        // Explizite Typdeklaration, um den Type Mismatch zu beheben
        return listOf(
            dependencyAnalysisProcessor,
            gradleProcessor,
            hardcodedStringProcessor,
            javaToKotlinProcessor,
            kotlinStyleProcessor,
            manifestProcessor,
            packageNameProcessor,
            selfModernizationProcessor,
            svgToAvdProcessor,
            themeProcessor,
            xmlToComposeProcessor,
            codeAnalysisProcessor,
        )
    }

    @Provides
    @Singleton
    fun provideRefactoringOrchestrator(
        processors: List<@JvmSuppressWildcards Processor>
    ): RefactoringOrchestrator {
        return RefactoringOrchestrator(processors)
    }
    /*
    @Provides
    @ViewModelScoped
    fun provideRefactoringOrchestrator(
        processors: List<@JvmSuppressWildcards Processor>
    ): RefactoringOrchestrator {
        return RefactoringOrchestrator(processors)
    }
    */
}
