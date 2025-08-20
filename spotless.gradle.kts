// Refactor/spotless.gradle.kts

spotless {
    // Konfiguration für Kotlin-Dateien (*.kt)
    kotlin {
        target("**/src/**/*.kt")
        ktlint("1.2.1").userData(mapOf("android" to "true"))
        // Korrekte Anwendung des Lizenz-Headers für Kotlin
        licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        trimTrailingWhitespace()
        endWithNewline()
    }

    // Konfiguration für Gradle Kotlin Skripte (*.gradle.kts)
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint("1.2.1")
        licenseHeaderFile(rootProject.file("spotless/copyright.kts"))
        trimTrailingWhitespace()
        endWithNewline()
    }

    // Konfiguration für XML-Dateien (Layouts, Manifest, etc.)
    format("xml") {
        target("**/src/**/*.xml")
        prettier(mapOf("parser" to "xml", "tabWidth" to 4))
        // WICHTIG: Der Delimiter wird hier hinzugefügt, um XML-Kommentare korrekt zu erkennen
        licenseHeaderFile(rootProject.file("spotless/copyright.xml"), "<!--(?s).*?-->")
        trimTrailingWhitespace()
        endWithNewline()
    }

    // Konfiguration für Java-Dateien (falls vorhanden)
    java {
        target("**/src/**/*.java")
        googleJavaFormat("1.17.0")
        licenseHeaderFile(rootProject.file("spotless/copyright.java"))
        trimTrailingWhitespace()
        endWithNewline()
    }
}
