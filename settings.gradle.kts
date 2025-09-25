pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // ✅ JitPack for MPAndroidChart
        maven("https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // ✅ JitPack again here
        maven("https://jitpack.io")
    }
}

rootProject.name = "FitPro"
include(":app")
