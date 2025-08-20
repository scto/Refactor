plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    alias(libs.plugins.spotless) // Spotless Plugin anwenden
}

// Wende die separate Konfigurationsdatei an
apply(from = "spotless.gradle.kts")
