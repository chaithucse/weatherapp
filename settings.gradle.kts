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

rootProject.name = "WeatherApp"
include(":app")
include(":core:data")
include(":core:network")
include(":feature:weatherinfo")
include(":feature:search")
include(":core:designsystem")
