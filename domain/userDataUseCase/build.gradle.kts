plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.asu1.userdatausecase"
}

dependencies {

    api(project(":core:userDataModels"))
    api(project(":core:resource"))
    implementation(project(":core:util"))
    ksp(libs.hilt.compiler)
    api(project(":repository:userData"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}