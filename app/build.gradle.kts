import io.getstream.log.Configuration

plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.compose.compiler.get().pluginId)
}

android {
    compileSdk = Configuration.compileSdk
    namespace = "io.getstream.log.sample"

    defaultConfig {
        applicationId = "io.getstream.log.sample"
        minSdk = Configuration.minSdk
        targetSdk = Configuration.targetSdk
        versionCode = 1
        versionName = Configuration.versionName
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // stream-log
    implementation(project(":stream-log"))

    // compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.animation)
}
