plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    alias(libs.plugins.spotless) // Spotless Plugin anwenden
}

// build.gradle.kts (Projektebene)
subprojects {
    apply(plugin = "com.diffplug.spotless")
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        // Kotlin-Dateien formatieren
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt") // Build-Verzeichnis ausschließen

            // Wählen Sie einen Formatter. ktlint ist eine beliebte Wahl.
            ktlint().setEditorConfigPath(rootProject.file(".editorconfig"))

            // Optional: Lizenz-Header hinzufügen
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        }

        // Gradle-Kotlin-Skriptdateien formatieren
        kotlinGradle {
            target("*.gradle.kts")
            ktlint()
			
			// Optional: Lizenz-Header hinzufügen
            licenseHeaderFile(rootProject.file("spotless/copyright.kts"))
        }

        // Java-Dateien formatieren (optional)
        java {
            target("**/*.java")
            googleJavaFormat().aosp() // Verwendet den Google-Java-Stil für AOSP
            removeUnusedImports()
            trimTrailingWhitespace()
			
			// Optional: Lizenz-Header hinzufügen
            licenseHeaderFile(rootProject.file("spotless/copyright.java"))
        }
		
		// Konfiguration für XML-Dateien (Layouts, Manifest, etc.)
        format("xml") {
            target("src/**/*.xml", "*.xml")
            // Standard XML-Formatierung mit Einrückung
            prettier(mapOf("parser" to "xml", "tabWidth" to 4))
            trimTrailingWhitespace()
            endWithNewline()
			
			// Optional: Lizenz-Header hinzufügen
            licenseHeaderFile(rootProject.file("spotless/copyright.xml"))
        }
    }
}

// Wende die separate Konfigurationsdatei an
//apply(from = "spotless.gradle.kts")
