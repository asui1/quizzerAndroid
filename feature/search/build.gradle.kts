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
    implementation(project(":core:quizCardModel"))
    implementation(project(":core:resource"))

    implementation(project(":domain:appDataUseCase"))
    implementation(project(":repository:appData"))
    implementation(project(":repository:network"))
    implementation(project(":feature:quizCard"))
    implementation(project(":feature:activityNavigation"))

    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.foundation.layout)
    implementation(libs.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation (libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.foundation.android)
    implementation(libs.material.icons.core)
    implementation(libs.material.icons.extended)
    implementation(libs.material3)
    implementation(libs.ui.tooling.preview)
    implementation(libs.ui.tooling)
    implementation(libs.collections.immutable)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}