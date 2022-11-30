import io.getstream.log.Configuration
import io.getstream.log.Dependencies
import io.getstream.log.Versions

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = Configuration.compileSdk

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

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.ANDROIDX_COMPOSE_COMPILER
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // stream-log
    implementation(project(":stream-log"))
    implementation(project(":stream-log-file"))
    implementation(project(":stream-log-android"))
    implementation(project(":stream-log-android-init"))

    // compose
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeUiTooling)
    implementation(Dependencies.composeActivity)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeRuntime)

    implementation(Dependencies.materialComponents)

    testImplementation(Dependencies.junit4)
    androidTestImplementation(Dependencies.androidxTestJunit)
    androidTestImplementation(Dependencies.espressoCore)

    detektPlugins(Dependencies.detektFormatting)
}
