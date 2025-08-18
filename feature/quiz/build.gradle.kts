plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.asu1.quiz"
}

dependencies {
    api(project(":core:quizModel"))
    api(project(":core:resource"))
    api(project(":core:util"))
    api(project(":core:imageColor"))
    api(project(":core:quizCardModel"))
    api(project(":core:colorModel"))
    api(project(":core:userDataModels"))

    implementation(project(":repository:network"))

    api(project(":domain:userDataUseCase"))
    api(project(":domain:appDataUseCase"))

    implementation(project(":feature:customComposable"))
    implementation(project(":feature:activityNavigation"))

    implementation(libs.uuid.creator)
    implementation(libs.foundation.layout)
    implementation(libs.navigation.compose)
    implementation(libs.compose.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.ui.tooling)
    implementation(libs.hilt.navigation.compose)
    api(libs.material3)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.material.icons.core)
    implementation(libs.material.icons.extended)
    implementation(libs.calendar.compose)
    implementation(libs.youtubeplayer.core)
    implementation(libs.reorderable)
    implementation(libs.collections.immutable)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.material.kolor)
    implementation (libs.compose.charts)
    implementation(libs.runtime.saveable)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}