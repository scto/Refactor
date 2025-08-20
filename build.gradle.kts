plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false

    // Wende das Spotless-Plugin direkt auf das Root-Projekt an.
    // Dadurch wird der `spotless`-Block unten verfügbar.
    alias(libs.plugins.spotless)
}

// Konfiguriere Spotless hier für das gesamte Projekt.
// Die Regeln werden automatisch auf alle Sub-Module (app, core, etc.) angewendet.
spotless {
    // Konfiguration für Kotlin-Dateien (*.kt)
    kotlin {
        // Wende die Regeln auf alle .kt-Dateien im gesamten Projekt an
        target("**/src/**/*.kt")
        ktlint("1.2.1").userData(mapOf("android" to "true"))
        licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        trimTrailingWhitespace()
        endWithNewline()
    }

    // Konfiguration für Gradle Kotlin Skripte (*.gradle.kts)
    kotlinGradle {
        // Wende die Regeln auf alle .gradle.kts-Dateien an
        target("**/*.gradle.kts")
        ktlint("1.2.1")
        licenseHeaderFile(rootProject.file("spotless/copyright.kts"))
        trimTrailingWhitespace()
        endWithNewline()
    }

    // Konfiguration für XML-Dateien
    format("xml") {
        target("**/src/**/*.xml")
        prettier(mapOf("parser" to "xml", "tabWidth" to 4))
        licenseHeaderFile(rootProject.file("spotless/copyright.xml"), "<!--(?s).*?-->")
        trimTrailingWhitespace()
        endWithNewline()
    }

    // Konfiguration für Java-Dateien
    java {
        target("**/src/**/*.java")
        googleJavaFormat("1.17.0")
        licenseHeaderFile(rootProject.file("spotless/copyright.java"))
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