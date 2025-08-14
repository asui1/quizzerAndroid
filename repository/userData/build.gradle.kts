plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.asu1.userdata"
}

dependencies {

    api(libs.hilt.android)
    implementation (libs.retrofit)
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.core)
    api(project(":core:userDataModels"))
    api(project(":core:resource"))
    api(project(":repository:network"))
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}