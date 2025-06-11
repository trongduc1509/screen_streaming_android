pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()

        maven("https://storage.googleapis.com/download.flutter.io")
    }
}

rootProject.name = "ScreenStreaming"
include(":app")
include(":appDependencies")
include(":domain")
include(":data")

val filePath = settingsDir.parentFile.toString() + "/screen_streaming_frame/.android/include_flutter.groovy"
apply(from = File(filePath))

include(":appCore")
include(":common")
