<h1 align="center">Stream Log</h1></br>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
 <a href="https://github.com/GetStream/stream-log/actions/workflows/build.yml"><img alt="Build Status" src="https://github.com/GetStream/whatsapp-clone-compose/actions/workflows/build.yml/badge.svg"/></a>
  <a href="https://getstream.io?utm_source=Github&utm_medium=Github_Repo_Content_Ad&utm_content=Developer&utm_campaign=Github_Dec2022_StreamLog&utm_term=DevRelOss"><img src="https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/HayesGordon/e7f3c4587859c17f3e593fd3ff5b13f4/raw/11d9d9385c9f34374ede25f6471dc743b977a914/badge.json" alt="Stream Feeds"></a>
</p><br>

<p align="center">
📠 Stream Log is a lightweight and extensible logger library for Kotlin and Android.
</p>

<img align="right" width="90px" src="https://user-images.githubusercontent.com/24237865/178630165-76855349-ac04-4474-8bcf-8eb5f8c41095.png"/>

## Stream Log

**Stream Log** is a lightweight logger and a pure Kotlin module to utilize this library on your Kotlin projects.

[![Maven Central](https://img.shields.io/maven-central/v/io.getstream/stream-log.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.skydoves%22%20AND%20a:%22retrofit-adapters-result%22)

Add the dependency below into your **module**'s `build.gradle` file:

```gradle
dependencies {
    implementation("io.getstream:stream-log:$versions")
}
```

### StreamLog

`StreamLog` is a primary log manager, which allows you to install your loggers and print log messages. First, you need to install a `StreamLogger` on `StreamLog`. In the Kotlin project, you can install `KotlinStreamLogger` by default, which is a simple logger for Kotlin as seen below:

```kotlin
// install `KotlinStreamLogger`. You only need to do this once.
StreamLog.install(KotlinStreamLogger())
```

Now, you can print log messages simply like the below:

```kotlin
streamLog { "This is a log messages" }
streamLog(priority = Priority.INFO, tag = "Tag") { "This is a log messages" }
StreamLog.d(tag = "Tag") { "This is a log message" }
```

Then you will get the log messages below:

```diff
+  D  2022-12-02 15:42:49'044 (main:2) [D/MessageRepository]: This is a log message!
+  I  2022-12-02 15:42:49'044 (main:2) [I/Tag]: This is a log message!
+  D  2022-12-02 15:42:49'044 (main:2) [D/Tag]: This is a log message!
```

Also, you can get the logger, which is installed on `StreamLog` like the below:

```kotlin
val logger = StreamLog.getLogger("Tag")
logger.d { "This is a log message" }

// Getting a tagged logger lazily.
val logger by taggedLogger()
val logger by taggedLogger(tag = "Tag")
```

>**Note**: If you don't specify the `tag` parameter, the tag value will be a class name that is logging currently.

### StreamLogger

`StreamLogger` is a low-level logger interface that can be installed/uninstalled on `StreamLog`. You can create your own logger by extending the `StreamLogger` interface like the below:

```kotlin
public class MyStreamLogger() : StreamLogger {

    override fun log(priority: Priority, tag: String, message: String, throwable: Throwable?) {
        
        // do something here
        ..

        println(message)
    }
}
```

You can also extend the `KotlinStreamLogger` and customize the behaviors like the below:

```kotlin
object ErrorStreamLogger : KotlinStreamLogger() {

    override fun log(priority: Priority, tag: String, message: String, throwable: Throwable?) {
        when (priority) {
            ERROR, ASSERT -> super.log(priority, tag, message, throwable)
            else -> { /* NO-OP */ }
        }
    }
}
```

### CompositeStreamLogger

You can separate roles and behaviors for each different logger and composite the loggers into a single logger with `CompositeStreamLogger`.

```kotlin
val fileLogger = FileStreamLogger(fileLoggerConfig)
val androidLogger = AndroidStreamLogger()
val compositeLogger = CompositeStreamLogger(androidLogger, fileLogger)
StreamLog.install(compositeLogger)
```

### Validator

The validator decides whether the log messages should be printed or not. You can set a validator to set the behaviors of your logger.

```kotlin
// Show log messages if the log priority is DEBUG or more than DEBUG.
StreamLog.setValidator { priority, tag ->
    priority.level >= Priority.DEBUG.level
}

// Show log messages if the tag contains a "main" string.
StreamLog.setValidator { priority, tag ->
    tag.contains("main")
}
```

## Stream Log File

**Stream Log File** is an extended library for persisting the log messages into an external file.

[![Maven Central](https://img.shields.io/maven-central/v/io.getstream/stream-log-file.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.skydoves%22%20AND%20a:%22retrofit-adapters-result%22)

Add the dependency below into your **module**'s `build.gradle` file:

```gradle
dependencies {
    implementation("io.getstream:stream-log-file:$versions")
}
```

### FileStreamLogger

You can persist the log messages that are triggered on runtime with `FileStreamLogger`. To persist your log messages into a file, you should use `FileStreamLogger` with `CompositeStreamLogger` like the example below:

```kotlin
val fileLoggerConfig = FileStreamLogger.Config(
    filesDir = context.filesDir, // an internal file directory
    externalFilesDir = context.getExternalFilesDir(null), // an external file directory. This is an optional.
    app = FileStreamLogger.Config.App( // application information.
        versionCode = context.getVersionCode(),
        versionName = context.getVersionName()
    ),
    device = FileStreamLogger.Config.Device( // device information
        model = "%s %s".format(Build.MANUFACTURER, Build.DEVICE),
        androidApiLevel = Build.VERSION.SDK_INT
    )
)
val fileLogger = FileStreamLogger(fileLoggerConfig)
val kotlinLogger = KotlinStreamLogger()
val compositeLogger = CompositeStreamLogger(kotlinLogger, fileLogger)

StreamLog.install(compositeLogger)
```

Then you will get the result file below:

```

```

## Stream Log Android

**Stream Log Android** is a simple Android logger on top of the **Stream Log**.

[![Maven Central](https://img.shields.io/maven-central/v/io.getstream/stream-log-android.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.skydoves%22%20AND%20a:%22retrofit-adapters-result%22)

Add the dependency below into your **module**'s `build.gradle` file:

```gradle
dependencies {
    implementation("io.getstream:stream-log-android:$versions")
}
```

### AndroidStreamLogger

You can simply install a logger for Android with `AndroidStreamLogger` like the below:

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidStreamLogger.installOnDebuggableApp(this)
    }
}
```

>**Note**: We'd recommend you install the logger only once in your application class.

Now, you can print log messages simply like the below:

```kotlin
streamLog { "This is a log messages" }
streamLog(priority = Priority.INFO, tag = "Tag") { "This is a log messages" }
StreamLog.d(tag = "Tag") { "This is a log message" }
```

Then you will get the log messages below:

```diff
+  D  2022-12-02 15:42:49'044 (main:2) [D/MessageRepository]: This is a log message!
+  I  2022-12-02 15:42:49'044 (main:2) [I/Tag]: This is a log message!
+  D  2022-12-02 15:42:49'044 (main:2) [D/Tag]: This is a log message!
```

You also can get the logger, which is installed on `StreamLog` like the below:

```kotlin
val logger = StreamLog.getLogger("Tag")
logger.d { "This is a log message" }

// Getting a tagged logger lazily.
val logger by taggedLogger()
val logger by taggedLogger(tag = "Tag")
```

>**Note**: If you don't specify the `tag` parameter, the tag value will be a class name that is logging currently. In Jetpack Compose, the tag will be the scope's name of Composable functions.

## Stream Log Android File

Stream Log Android 

[![Maven Central](https://img.shields.io/maven-central/v/io.getstream/stream-log-android.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.skydoves%22%20AND%20a:%22retrofit-adapters-result%22)

Add the dependency below into your **module**'s `build.gradle` file:

```gradle
dependencies {
    implementation("io.getstream:stream-log-android-init:$versions")
}
```

## Stream Log BOM

The **Stream Log** Bill of Materials (BOM) lets you manage all of your **Stream Log** library versions by specifying only the BOM’s version.

[![Maven Central](https://img.shields.io/maven-central/v/io.getstream/stream-log-android.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.skydoves%22%20AND%20a:%22retrofit-adapters-result%22)

```gradle
dependencies {
    implementation("io.getstream:stream-log-bom:$versions")

    implementation("io.getstream:stream-log")
    implementation("io.getstream:stream-log-file")
    implementation("io.getstream:stream-log-android")
    implementation("io.getstream:stream-log-android-file")
}
```

<a href="https://getstream.io/chat/compose/tutorial/?utm_source=Github&utm_campaign=Devrel_oss&utm_medium=StreamLog"><img src="https://user-images.githubusercontent.com/24237865/146505581-a79e8f7d-6eda-4611-b41a-d60f0189e7d4.jpeg" align="right" /></a>

## Find this library useful? :heart:

Support it by joining __[stargazers](https://github.com/getStream/stream-log/stargazers)__ for this repository. :star: <br>
Also, follow **[Stream](https://twitter.com/getstream_io)** on Twitter for our next creations!

# License
```xml
Copyright 2022 Stream.IO, Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
