plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.asu1.searchsuggestion"
}

dependencies {
    implementation(project(":core:util"))
    api(project(":core:quizCardModel"))
    implementation(project(":core:resource"))

    api(project(":domain:appDataUseCase"))
    api(project(":repository:appData"))
    implementation(project(":feature:quizCard"))
    implementation(project(":feature:activityNavigation"))

    implementation(libs.foundation.layout)
    implementation(libs.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.foundation.android)
    implementation(libs.material.icons.core)
    implementation(libs.material3)
    implementation(libs.ui.tooling.preview)
    implementation(libs.ui.tooling)
    implementation(libs.collections.immutable)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}