pluginManagement {
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
    ":core",
    ":data:local",
    ":features:git",
    ":ui:home",
    ":ui:onboarding",
    ":ui:settings",
    ":self-update"
)