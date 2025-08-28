pluginManagement {
	//includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Refactor"
include(
    ":app",
    ":core:gemini",
    ":data:local",
    ":features:git",
    ":self-update",
    ":ui:home",
    ":ui:onboarding",
    ":ui:settings"
)

/*
include(
    ":app",
    ":core:gemini",
    ":data:local",
    ":features:git",
    ":self-update",
	":svg-converter",
    ":ui:home",
    ":ui:onboarding",
    ":ui:settings"
)
*/