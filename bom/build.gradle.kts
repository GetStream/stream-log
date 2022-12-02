import com.github.skydoves.landscapist.Configuration

plugins {
  kotlin("jvm")
}

rootProject.extra.apply {
  set("PUBLISH_GROUP_ID", Configuration.artifactGroup)
  set("PUBLISH_ARTIFACT_ID", "stream-log-bom")
  set("PUBLISH_VERSION", rootProject.extra.get("rootVersionName"))
}

dependencies {
  constraints {
    api(project(":stream-log"))
    api(project(":stream-log-file"))
    api(project(":stream-log-android"))
    api(project(":stream-log-android-file"))
  }
}

apply(from ="${rootDir}/scripts/publish-module.gradle")

