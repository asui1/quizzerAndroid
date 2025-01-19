pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "quizzerAndroid"
include(":app")
include(":macrobenchmark")
include(":microbenchmark")
include(":core:quizModels")
include(":core:utils")
include(":feature:musicui")
include(":feature:quizcard")
include(":core:quizcardmodel")
include(":feature:customdialogs")
include(":feature:pageIndicator")
include(":feature:customButtons")
include(":core:resources")
include(":feature:toastManager")
