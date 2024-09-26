import io.getstream.log.Configuration

plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.nexus.plugin.get().pluginId)
}

mavenPublishing {
    val artifactId = "stream-log-android-file"
    coordinates(
        Configuration.artifactGroup,
        artifactId,
        Configuration.versionName
    )

    pom {
        name.set(artifactId)
        description.set("A lightweight and extensible logger library for Kotlin and Android.")
    }
}

android {
    compileSdk = Configuration.compileSdk
    namespace = "io.getstream.log.android.file"

    defaultConfig {
        minSdk = Configuration.minSdk
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
   api(project(":stream-log"))
   implementation(project(":stream-log-file"))

   implementation(libs.androidx.core.ktx)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

tasks.withType<JavaCompile>().configureEach {
    this.targetCompatibility = libs.versions.jvmTarget.get()
    this.sourceCompatibility = libs.versions.jvmTarget.get()
}