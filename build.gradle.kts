plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    alias(libs.plugins.spotless) apply false
	//alias(libs.plugins.spotless.gradle)
}

subprojects {
    apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
    extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("**/build/**/*.kt")
            ktlint()

            // Look for the first line that doesn't have a block comment (assumed to be the license)
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"), "(^(?![\\/ ]\\*).*$)")
        }
        kotlinGradle {
            target("**/*.gradle.kts")
            targetExclude("**/build/**/*.gradle.kts")
            ktlint()

            // Look for the first line that doesn't have a block comment (assumed to be the license)
            licenseHeaderFile(rootProject.file("spotless/copyright.kts"), "(^(?![\\/ ]\\*).*$)")
        }
        java {
            target("**/*.java")
            targetExclude("**/build/**/*.java")

            // Use the default importOrder configuration
            importOrder()

            // Cleanthat will refactor your code, but it may break your style: apply it before your formatter
            cleanthat()

            // Use google-java-format
            googleJavaFormat()

            // Fix formatting of type annotations
            formatAnnotations()

            // Look for the first line that doesn't have a block comment (assumed to be the license)
            licenseHeaderFile(rootProject.file("spotless/copyright.java"), "(^(?![\\/ ]\\*).*$)")
        }
        groovyGradle {
            target("**/*.gradle")
            targetExclude("**/build/**/*.gradle")

            // Look for the first line that doesn't have a block comment (assumed to be the license)
            licenseHeaderFile(rootProject.file("spotless/copyright.gradle"), "(^(?![\\/ ]\\*).*$)")
        }
        format("xml") {
            target("**/*.xml")
            targetExclude("**/build/**/*.xml")

            // Look for the first XML tag that isn't a comment (<!--) or the xml declaration (<?xml)
            licenseHeaderFile(rootProject.file("spotless/copyright.xml"), "(<[^!?])")
        }
    }
    afterEvaluate {
        tasks.named("preBuild") {
            dependsOn("spotlessApply")
        }
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