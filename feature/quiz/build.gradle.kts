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
    implementation(project(":core:quizModel"))
    implementation(project(":core:resource"))
    implementation(project(":core:util"))
    implementation(project(":core:imageColor"))
    implementation(project(":core:quizCardModel"))
    implementation(project(":core:colorModel"))
    implementation(project(":core:userDataModels"))

    implementation(project(":repository:network"))

    implementation(project(":domain:userDataUseCase"))
    implementation(project(":domain:appDataUseCase"))

    implementation(project(":feature:customComposable"))
    implementation(project(":feature:activityNavigation"))

    implementation(libs.core.ktx)
    implementation(libs.uuid.creator)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.core.ktx)
    implementation(libs.foundation.layout)
    implementation(libs.navigation.compose)
    implementation(libs.compose.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.ui.tooling)
    implementation(libs.material3)
    implementation(libs.material3)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.runtime.livedata)
    implementation(libs.material.icons.core)
    implementation(libs.material.icons.extended)
    implementation(libs.calendar.compose)
    implementation(libs.youtubeplayer.core)
    implementation(libs.reorderable)
    implementation(libs.collections.immutable)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.material.kolor)
    implementation (libs.compose.charts)
    implementation(libs.runtime.saveable)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}