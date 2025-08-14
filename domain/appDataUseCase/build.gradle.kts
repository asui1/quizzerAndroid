plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.asu1.appdatausecase"
}

dependencies {
    api(project(":core:quizCardModel"))
    api(project(":core:appDataModels"))
    api(project(":core:userDataModels"))
    implementation(project(":core:util"))
    api(project(":core:quizModel"))

    implementation(project(":repository:network"))
    api(project(":repository:quizData"))
    api(project(":repository:appData"))

    implementation (libs.retrofit)
    implementation(libs.datastore.core)
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}