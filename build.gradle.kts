// Refactor/build.gradle.kts

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false

    // WICHTIG: Das Plugin hier mit "apply false" anwenden.
    // Das macht es für alle Module verfügbar, ohne es auf das Root-Projekt selbst anzuwenden.
    alias(libs.plugins.spotless) apply false
}

// Die Konfiguration für alle Subprojekte anwenden.
// "subprojects" stellt sicher, dass Spotless in jedem Modul (app, core, etc.) läuft.
subprojects {
    // Das Spotless-Plugin auf jedes einzelne Subprojekt anwenden
    apply(plugin = "com.diffplug.spotless")

    // Die Konfiguration für Spotless definieren
    spotless {
        // Konfiguration für Kotlin-Dateien (*.kt)
        kotlin {
            target("src/**/*.kt")
            ktlint("1.2.1").userData(mapOf("android" to "true"))
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
            trimTrailingWhitespace()
            endWithNewline()
        }

        // Konfiguration für Java-Dateien (falls vorhanden)
        java {
            target("src/**/*.java")
            googleJavaFormat("1.17.0")
            licenseHeaderFile(rootProject.file("spotless/copyright.java"))
            trimTrailingWhitespace()
            endWithNewline()
        }

        // Konfiguration für XML-Dateien
        format("xml") {
            target("src/**/*.xml")
            prettier(mapOf("parser" to "xml", "tabWidth" to 4))
            licenseHeaderFile(rootProject.file("spotless/copyright.xml"), "<!--(?s).*?-->")
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}

// Separate Konfiguration für die Gradle-Skripte im Root-Verzeichnis
apply(plugin = "com.diffplug.spotless")
spotless {
    kotlinGradle {
        target("*.gradle.kts", "**/*.gradle.kts")
        ktlint("1.2.1")
        licenseHeaderFile(rootProject.file("spotless/copyright.kts"))
        trimTrailingWhitespace()
        endWithNewline()
    }
}

/*
Save your working tree with 
git add -A,
then git commit -m "Checkpoint before spotless."

Run gradlew spotlessApply

View the changes with git diff

If you don't like what spotless did,
git reset --hard

If you'd like to remove the "checkpoint" commit,
git reset --soft head~1 will make the checkpoint commit "disappear" from history, but keeps the changes in your working directory.
*/

// build.gradle.kts (Projektebene)
/*
tasks.withType<com.diffplug.gradle.spotless.SpotlessApplyTask>().configureEach {
    mustRunAfter("clean")
}
tasks.named("preBuild") {
    dependsOn("spotlessApply")
}
*/