import io.getstream.log.Configuration
import io.getstream.log.Dependencies

plugins {
    kotlin("jvm")
}

rootProject.extra.apply {
    set("PUBLISH_GROUP_ID", Configuration.artifactGroup)
    set("PUBLISH_ARTIFACT_ID", "stream-log")
    set("PUBLISH_VERSION", rootProject.extra.get("rootVersionName"))
}

apply(from = "$rootDir/scripts/publish-module.gradle")

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


dependencies {
    testImplementation(Dependencies.junit4)
    detektPlugins(Dependencies.detektFormatting)
}
