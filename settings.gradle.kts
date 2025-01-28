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
include(":feature:customDialog")
include(":feature:pageIndicator")
include(":feature:customButton")
include(":core:resource")
include(":feature:toastManager")
include(":feature:colorPicker")
include(":domain:imagecolor")
include(":core:imageColor")
include(":feature:customFlipper")
include(":feature:customDropdown")
include(":repository:userData")
include(":domain:userDataUseCase")
include(":core:userDataModels")
include(":repository:network")
include(":core:scoreCardModel")
include(":core:colorModel")
include(":feature:permissionRequest")
include(":core:BaseInterfaces")
