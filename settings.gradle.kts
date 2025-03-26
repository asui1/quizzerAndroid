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
include(":core:quizModel")
include(":core:util")
include(":feature:musicUi")
include(":feature:quizCard")
include(":core:quizCardModel")
include(":feature:customComposable")
include(":core:resource")
include(":feature:activityNavigation")
include(":domain:imagecolor")
include(":core:imageColor")
include(":repository:userData")
include(":domain:userDataUseCase")
include(":core:userDataModels")
include(":repository:network")
include(":core:scoreCardModel")
include(":core:colorModel")
include(":feature:permissionRequest")
include(":repository:appData")
include(":domain:appDataUseCase")
include(":core:appDataModels")
include(":feature:search")
include(":feature:quiz")
include(":feature:splashPage")
include(":feature:mainPage")
include(":repository:quizData")
