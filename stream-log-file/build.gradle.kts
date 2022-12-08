import io.getstream.log.Configuration
import io.getstream.log.Dependencies

plugins {
    kotlin("jvm")
}

rootProject.extra.apply {
    set("PUBLISH_GROUP_ID", Configuration.artifactGroup)
    set("PUBLISH_ARTIFACT_ID", "stream-log-file")
    set("PUBLISH_VERSION", rootProject.extra.get("rootVersionName"))
}

apply(from = "$rootDir/scripts/publish-module.gradle")

dependencies {
    testImplementation(Dependencies.junit4)
    detektPlugins(Dependencies.detektFormatting)
}

dependencies {
    api(project(":stream-log"))
    testImplementation(Dependencies.junit4)
    detektPlugins(Dependencies.detektFormatting)
}
