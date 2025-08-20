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
		
		// Konfiguration für XML-Dateien (Layouts, Manifest, etc.)
        format("xml") {
            target("src/**/*.xml", "*.xml")
            // Standard XML-Formatierung mit Einrückung
            prettier(mapOf("parser" to "xml", "tabWidth" to 4))
            trimTrailingWhitespace()
            endWithNewline()
			/* Some files have fixed header lines (e.g. <?xml version="1.0" ... in XMLs, or #!/bin/bash in bash scripts).
			   Comments cannot precede these, so the license header has to come after them, too.
               To define what lines to skip at the beginning of such files,
			   fill the skipLinesMatching option with a regular expression that matches them (e.g. .skipLinesMatching("^#!.+?\$") to skip shebangs).
            */
			skipLinesMatching("^#!.+?\$")
			// Optional: Lizenz-Header hinzufügen
            licenseHeaderFile(rootProject.file("spotless/copyright.xml"))
        }
		
		// Konfiguration für YAML/YML-Dateien (Github workflows.)
		yaml {
			target("**/*.yml")
			jackson()
		}
    }
}

// Wende die separate Konfigurationsdatei an
//apply(from = "spotless.gradle.kts")

// build.gradle.kts (Projektebene)
tasks.withType<com.diffplug.gradle.spotless.SpotlessApplyTask>().configureEach {
    mustRunAfter("clean")
}
tasks.named("preBuild") {
    dependsOn("spotlessApply")
}