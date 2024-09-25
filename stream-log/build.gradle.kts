import io.getstream.log.Configuration

plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.multiplatform.get().pluginId)
    id(libs.plugins.nexus.plugin.get().pluginId)
}

mavenPublishing {
    val artifactId = "stream-log"
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

kotlin {
    androidTarget { publishLibraryVariants("release") }
    jvm("desktop")

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    macosX64()
    macosArm64()

    @Suppress("OPT_IN_USAGE")
    applyHierarchyTemplate {
        common {
            group("jvm") {
                withAndroidTarget()
                withJvm()
            }
            group("skia") {
                withJvm()
                group("apple") {
                    group("ios") {
                        withIosX64()
                        withIosArm64()
                        withIosSimulatorArm64()
                    }
                    group("macos") {
                        withMacosX64()
                        withMacosArm64()
                    }
                }
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.datetime)
            }
        }
    }

    explicitApi()
}

android {
    compileSdk = Configuration.compileSdk
    namespace = "io.getstream.log"
    defaultConfig {
        minSdk = Configuration.minSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
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