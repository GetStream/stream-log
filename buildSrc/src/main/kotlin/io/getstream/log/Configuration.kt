package io.getstream.log

object Configuration {
    const val compileSdk = 35
    const val targetSdk = 35
    const val sampleTargetSdk = 35
    const val minSdk = 21
    const val majorVersion = 1
    const val minorVersion = 3
    const val patchVersion = 4
    const val versionName = "$majorVersion.$minorVersion.$patchVersion"
    const val snapshotVersionName = "$majorVersion.$minorVersion.${patchVersion + 1}-SNAPSHOT"
    const val artifactGroup = "io.getstream"
}
