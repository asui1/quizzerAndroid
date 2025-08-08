plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.asu1.appdata"
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.hilt.android)
    implementation (libs.retrofit)
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.core)
    implementation(project(":core:userDataModels"))
    implementation(project(":core:resource"))
    implementation(project(":repository:network"))
    implementation(project(":core:appDataModels"))
    implementation(project(":core:util"))
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}