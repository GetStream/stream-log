@file:Suppress("RedundantVisibilityModifier")

package io.getstream.log

object Versions {
    internal const val ANDROID_GRADLE_PLUGIN = "7.3.1"
    internal const val ANDROIDX_ANNOTATIONS = "1.5.0"
    internal const val ANDROID_JUNIT5_GRADLE_PLUGIN = "1.8.2.1"
    internal const val ANDROIDX_ACTIVITY_COMPOSE = "1.6.1"
    internal const val ANDROIDX_COMPOSE = "1.3.1"
    public const val ANDROIDX_COMPOSE_COMPILER = "1.3.2"
    internal const val ANDROIDX_TEST_JUNIT = "1.1.4"
    internal const val ANDROIDX_KTX = "1.9.0"
    internal const val DETEKT_PLUGIN = "1.21.0"
    internal const val DOKKA = "1.8.0"
    internal const val DOKKASAURUS = "0.1.10"
    internal const val GRADLE_NEXUS_PUBLISH_PLUGIN = "1.1.0"
    internal const val GRADLE_VERSIONS_PLUGIN = "0.44.0"
    internal const val JUNIT4 = "4.13.2"
    internal const val JUNIT5 = "5.9.1"
    internal const val KOTLIN = "1.7.20"
    internal const val GITVERSIONER = "0.5.0"
    internal const val KOTLIN_BINARY_VALIDATOR = "0.12.1"
    internal const val MATERIAL_COMPONENTS = "1.7.0"
    internal const val MOCKITO_KOTLIN = "4.0.0"
    internal const val MOCKITO = "4.9.0"
    internal const val SPOTLESS = "6.7.2"
    internal const val TEST_PARAMETER_INJECTOR = "1.10"
}

object Dependencies {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}"
    const val androidJunit5GradlePlugin =
        "de.mannodermaus.gradle.plugins:android-junit5:${Versions.ANDROID_JUNIT5_GRADLE_PLUGIN}"
    const val androidxAnnotations = "androidx.annotation:annotation:${Versions.ANDROIDX_ANNOTATIONS}"
    const val androidxCoreKtx = "androidx.core:core-ktx:${Versions.ANDROIDX_KTX}"
    const val androidxTestJunit = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST_JUNIT}"
    const val composeRuntime= "androidx.compose.runtime:runtime:${Versions.ANDROIDX_COMPOSE}"
    const val composeUi = "androidx.compose.ui:ui:${Versions.ANDROIDX_COMPOSE}"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.ANDROIDX_COMPOSE}"
    const val composeFoundation = "androidx.compose.foundation:foundation:${Versions.ANDROIDX_COMPOSE}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.ANDROIDX_COMPOSE}"
    const val composeActivity = "androidx.activity:activity-compose:${Versions.ANDROIDX_ACTIVITY_COMPOSE}"
    const val detektPlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.DETEKT_PLUGIN}"
    const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.DETEKT_PLUGIN}"
    const val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.DOKKA}"
    const val dokkasaurus = "io.getstream:dokkasaurus:${Versions.DOKKASAURUS}"
    const val junit4 = "junit:junit:${Versions.JUNIT4}"
    const val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT5}"
    const val junitJupiterParams = "org.junit.jupiter:junit-jupiter-params:${Versions.JUNIT5}"
    const val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT5}"
    const val junitVintageEngine = "org.junit.vintage:junit-vintage-engine:${Versions.JUNIT5}"
    const val kotlinBinaryValidator =
        "org.jetbrains.kotlinx:binary-compatibility-validator:${Versions.KOTLIN_BINARY_VALIDATOR}"
    const val materialComponents = "com.google.android.material:material:${Versions.MATERIAL_COMPONENTS}"
    const val gitversionerPlugin = "com.pascalwelsch.gitversioner:gitversioner:${Versions.GITVERSIONER}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val gradleVersionsPlugin = "com.github.ben-manes:gradle-versions-plugin:${Versions.GRADLE_VERSIONS_PLUGIN}"
    const val gradleNexusPublishPlugin = "io.github.gradle-nexus:publish-plugin:${Versions.GRADLE_NEXUS_PUBLISH_PLUGIN}"
    const val mockito = "org.mockito:mockito-core:${Versions.MOCKITO}"
    const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:${Versions.MOCKITO_KOTLIN}"
    const val spotlessGradlePlugin = "com.diffplug.spotless:spotless-plugin-gradle:${Versions.SPOTLESS}"
    const val testParameterInjector =
        "com.google.testparameterinjector:test-parameter-injector:${Versions.TEST_PARAMETER_INJECTOR}"

    @JvmStatic
    fun isNonStable(version: String): Boolean = isStable(version).not()

    @JvmStatic
    fun isStable(version: String): Boolean = ("^[0-9.-]+$").toRegex().matches(version)
}
