plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.asu1.quizdata"
}

dependencies {
    api(project(":core:quizModel"))
    api(project(":repository:network"))
    api(project(":core:quizCardModel"))
    api(project(":core:userDataModels"))

    implementation(libs.bundles.retrofit.full)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}