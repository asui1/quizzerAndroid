plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.asu1.userdatausecase"
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.hilt.android)
    implementation (libs.retrofit)
    implementation(project(":core:userDataModels"))
    implementation(project(":core:resource"))
    implementation(project(":core:util"))
    ksp(libs.hilt.compiler)
    implementation(project(":repository:userData"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}