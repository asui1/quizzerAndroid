plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.asu1.appdata"
}

dependencies {

    api(libs.hilt.android)
    implementation (libs.retrofit)
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.core)
    api(project(":repository:network"))
    api(project(":core:appDataModels"))
    implementation(project(":core:util"))
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}