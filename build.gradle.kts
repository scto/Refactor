plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    alias(libs.plugins.spotless)
	//alias(libs.plugins.spotless.gradle)
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("${layout.buildDirectory}/**/*.kt")
            targetExclude("bin/**/*.kt")

            ktlint()
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
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