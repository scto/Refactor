// Refactor/spotless.gradle.kts

spotless {
    // Konfiguration für Kotlin-Dateien (*.kt)
    kotlin {
        target("src/**/*.kt")
        // Nutze ktlint zur Formatierung mit der empfohlenen Version
        ktlint("1.2.1").userData(mapOf("android" to "true"))
        // Fügt einen Lizenz-Header hinzu (optional, aber gute Praxis)
        licenseHeader(
            """// Copyright (C) 2024 The Android Open Source Project
               |//
               |// Licensed under the Apache License, Version 2.0 (the "License");
               |// you may not use this file except in compliance with the License.
               |// You may obtain a copy of the License at
               |//
               |//      http://www.apache.org/licenses/LICENSE-2.0
               |//
               |// Unless required by applicable law or agreed to in writing, software
               |// distributed under the License is distributed on an "AS IS" BASIS,
               |// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
               |// See the License for the specific language governing permissions and
               |// limitations under the License.""".trimMargin()
        )
        trimTrailingWhitespace()
        endWithNewline()
    }

    // Konfiguration für Gradle Kotlin Skripte (*.kts)
    kotlinGradle {
        target("*.kts", "**/*.kts")
        ktlint("1.2.1")
        trimTrailingWhitespace()
        endWithNewline()
    }

    // Konfiguration für XML-Dateien (Layouts, Manifest, etc.)
    format("xml") {
        target("src/**/*.xml", "*.xml")
        // Standard XML-Formatierung mit Einrückung
        prettier(mapOf("parser" to "xml", "tabWidth" to 4))
        trimTrailingWhitespace()
        endWithNewline()
    }
}
