plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.asu1.network"
}

dependencies {
    api(project(":core:appDataModels"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    api(libs.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    api(project(":core:userDataModels"))
    api(project(":core:quizCardModel"))
    api(project(":core:quizModel"))
    api(project(":core:quizModel"))
    implementation(project(":core:resource"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}