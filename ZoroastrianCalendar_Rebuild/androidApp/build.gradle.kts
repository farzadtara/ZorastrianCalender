plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.farzadtara.zoroastrian.calendar"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.farzadtara.zoroastrian.calendar"
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
        versionCode = 1
        versionName = "0.1.0"
    }
}

dependencies {
    implementation(project(":sharedLogic"))
    implementation(project(":sharedUI"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.glance.appwidget)
}
