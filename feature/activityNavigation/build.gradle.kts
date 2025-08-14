plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.asu1.toastmanager"
}

dependencies {
    api(project(":core:quizModel"))
    implementation(project(":core:util"))
    api(project(":domain:userDataUseCase"))

    implementation(libs.foundation.layout)
    api(libs.material3)
    implementation(libs.compose.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.ui.tooling)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.work.runtime.ktx)
    implementation(libs.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}