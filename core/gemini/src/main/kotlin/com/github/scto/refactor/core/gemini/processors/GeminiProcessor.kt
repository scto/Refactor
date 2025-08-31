package com.github.scto.refactor.core.gemini.processors

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig

import timber.log.Timber

import com.github.scto.refactor.core.gemini.BuildConfig

object GeminiProcessor {
    private val apiKey = BuildConfig.GEMINI_API_KEY.ifBlank { System.getenv("GEMINI_API_KEY") }

    private val generativeModel: GenerativeModel by lazy {
        if (apiKey.isNullOrBlank()) {
            throw IllegalStateException("API-Schlüssel für Gemini nicht gefunden.")
        }
        val config = generationConfig {
            temperature = 0.2f
            topK = 1
            topP = 1f
            maxOutputTokens = 8192
        }
        GenerativeModel(
            modelName = "gemini-1.5-pro-latest",
            apiKey = apiKey,
            generationConfig = config,
        )
    }

    private suspend fun ask(prompt: String): Result<String> {
        return try {
            val response = generativeModel.generateContent(prompt)
            val text = response.text ?: ""
            // VERBESSERT: Robusteres Entfernen von Markdown-Code-Blöcken
            val cleanedText =
                text.trim().replace(Regex("```[a-zA-Z]*\n?"), "").replace("```", "").trim()
            Result.success(cleanedText)
        } catch (e: Exception) {
            Timber.e(e, "Gemini API request failed.")
            Result.failure(e)
        }
    }

    // ... (restliche Prozessor-Methoden bleiben unverändert) ...

    suspend fun analyzeCode(fileContent: String): Result<String> {
        val prompt =
            """
                Sie sind ein erfahrener Code-Reviewer.
                Analysieren Sie den folgenden Code auf mögliche Fehler, Leistungsprobleme und Abweichungen von Best Practices.
                Fassen Sie Ihre Ergebnisse prägnant zusammen und geben Sie konkrete Vorschläge.
                
                Zu analysierender Code:
       ```
       $fileContent
       ```
       """
                .trimIndent()
        return ask(prompt)
    }

    suspend fun refactorGradleToKts(groovyCode: String): Result<String> {
        val prompt =
            "Du bist ein Gradle-Experte. Konvertiere das folgende Groovy-basierte Gradle-Skript in ein Kotlin DSL-Skript (.kts). Behalte alle Konfigurationen exakt bei. Gib NUR das reine Kotlin DSL zurück.\n\nGroovy Gradle Code:\n```groovy\n$groovyCode\n```"
        return ask(prompt)
    }

    suspend fun convertJavaToKotlin(javaCode: String): Result<String> {
        val prompt =
            "Du bist ein Experte für Kotlin. Konvertiere den folgenden Java-Code nach idiomatischem, modernem Kotlin. Gib NUR den reinen Kotlin-Code zurück.\n\nJava Code:\n```java\n$javaCode\n```"
        return ask(prompt)
    }

    suspend fun analyzeKotlinStyle(fileName: String, code: String): Result<String> {
        val prompt =
            "Analysiere den folgenden Kotlin-Code aus der Datei `$fileName`. Refaktorisiere ihn, um ihn idiomatischer, null-sicherer und lesbarer zu machen. Gib nur den refaktorisierten Code zurück.\n\nOriginal Code:\n```kotlin\n$code\n```"
        return ask(prompt)
    }

    suspend fun analyzeAndRefactorManifest(manifestXml: String): Result<String> {
        val prompt =
            "Analysiere die folgende `AndroidManifest.xml`-Datei. Korrigiere `android:exported`-Flags für Android 12+ und stelle sicher, dass die Datei wohlgeformt ist. Gib die korrigierte `AndroidManifest.xml` zurück. Gib NUR den XML-Code zurück.\n\nAndroidManifest.xml:\n```xml\n$manifestXml\n```"
        return ask(prompt)
    }

    suspend fun selfModernizer(files: List<Pair<String, String>>): Result<String> {
        val fileContentString =
            files.joinToString("\n\n---\n\n") { (name, content) -> "// Datei: $name\n$content" }
        val prompt =
            "Du bist ein Weltklasse-Softwarearchitekt. Analysiere die folgenden Dateien eines Android-Tools namens \"Refactor\". Deine Aufgabe ist eine Meta-Analyse: 1. Analysiere die Gesamtarchitektur. 2. Schlage die NÄCHSTE, wichtigste Refactoring-Funktion vor. 3. Finde Code-Smells. Gib eine kurze Zusammenfassung deiner drei wichtigsten Erkenntnisse zurück.\n\nProjekdateien:\n```\n$fileContentString\n```"
        return ask(prompt)
    }

    suspend fun convertSvgToAvd(svgContent: String): Result<String> {
        val prompt =
            """Du bist ein Experte für Android-Grafikformate. Konvertiere den folgenden SVG-Code in ein Android Vector Drawable (AVD) XML. Beachte dabei die folgenden Konvertierungsregeln:
       - Das root-Element muss `<vector>` sein.
       - SVG-Attribute wie `width` und `height` müssen zu `android:width` und `android:height` werden (in `dp`).
       - `viewBox` wird zu `android:viewportWidth` und `android:viewportHeight`.
       - SVG-Pfade (`<path>`) und deren `d` Attribut bleiben erhalten, werden aber zu `android:pathData`.
       - Farbfüllungen (`fill`) werden zu `android:fillColor`.
       - Linienfarben (`stroke`) werden zu `android:strokeColor`.
       - Linienstärken (`stroke-width`) werden zu `android:strokeWidth`.
       - `fill-rule` wird zu `android:fillType`.
       Gib NUR den reinen, wohlgeformten XML-Code für das Android Vector Drawable zurück, ohne Erklärungen.
                
       SVG-Code:
       ```xml
       $svgContent
       ```
       """
                .trimIndent()
        return ask(prompt)
    }

    suspend fun convertThemeToMaterial3(
        oldThemesXml: String,
        oldColorsXml: String,
    ): Result<String> {
        val prompt =
            "Du bist ein Android UI/UX-Experte. Konvertiere das folgende alte AppCompat-Theme und die dazugehörigen Farben in ein modernes Material 3 Theme. Parent-Theme soll `Theme.Material3.DayNight.NoActionBar` sein. Gib das Ergebnis als eine einzige, vollständige XML-Datei zurück.\n\nAlte `themes.xml`:\n```xml\n$oldThemesXml\n```\n\nAlte `colors.xml`:\n```xml\n$oldColorsXml\n```"
        return ask(prompt)
    }

    suspend fun convertXmlToCompose(xmlContent: String): Result<String> {
        val prompt =
            """
                Sie sind ein erfahrener Android-Entwickler mit Spezialisierung auf Jetpack Compose.
                Konvertieren Sie den folgenden Android-XML-Layoutcode in eine Jetpack Compose-Funktion.
                – Die generierte Composable-Funktion sollte „ConvertedLayout“ heißen.
                – Verwenden Sie moderne Material 3-Komponenten („androidx.compose.material3.*“).
                – Ordnen Sie Ansichtsattribute den entsprechenden Compose-Modifikatoren und -Parametern so genau wie möglich zu.
                – Erstellen Sie ein „@Preview“-Composable, um das Ergebnis anzuzeigen.
                – Geben Sie NUR den reinen, wohlgeformten Kotlin-Code für die Composable-Funktion und deren Vorschau zurück.
                
                XML-Layoutcode:
       ```xml
       $xmlContent
       ```
       """
                .trimIndent()
        return ask(prompt)
    }
}
