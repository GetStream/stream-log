<h1 align="center">Stream Log</h1></br>

<p align="center">
🛥 Stream Log is a lightweight and extensible logger library for Kotlin and Android.
</p><br>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
 <a href="https://github.com/GetStream/stream-log/actions/workflows/build.yml"><img alt="Build Status" src="https://github.com/GetStream/stream-log/actions/workflows/build.yml/badge.svg"/></a>
 <a href="https://androidweekly.net/issues/issue-548"><img alt="Android Weekly" src="https://skydoves.github.io/badges/android-weekly.svg"/></a>
  <a href="https://getstream.io?utm_source=Github&utm_medium=Github_Repo_Content_Ad&utm_content=Developer&utm_campaign=Github_Dec2022_StreamLog&utm_term=DevRelOss"><img src="https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/HayesGordon/e7f3c4587859c17f3e593fd3ff5b13f4/raw/11d9d9385c9f34374ede25f6471dc743b977a914/badge.json" alt="Stream Feeds"></a>
</p><br>

<p align="center">
  <a href="https://getstream.io/chat/sdk/android/"><img width="210px" alt="Logo" src="https://user-images.githubusercontent.com/24237865/205788244-57f7200c-7ed6-456a-927e-2594e3b311bf.png"/></a> <br>
</p>

## Why Stream Log?
**Stream Log** originated from [stream-chat-android](https://github.com/getStream/stream-chat-android), and it has already been verified by delivering to billions of global end-users across thousands of different apps. It's simple and easy to use. You can also record and extract the runtime log messages into an external `.txt` file and utilize it to trace your log messages.

<img align="right" width="90px" src="https://user-images.githubusercontent.com/24237865/178630165-76855349-ac04-4474-8bcf-8eb5f8c41095.png"/>

## Stream Log

**Stream Log** is a lightweight logger and a pure Kotlin module to utilize this library on your Kotlin projects.

[![Maven Central](https://img.shields.io/maven-central/v/io.getstream/stream-log.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.getstream%22%20AND%20a:%22stream-log%22)

Add the dependency below into your **module**'s `build.gradle` file:

```gradle
dependencies {
    implementation("io.getstream:stream-log:$version")
}
```

### StreamLog

`StreamLog` is a primary log manager, which allows you to install your loggers and print log messages. First, you need to install a `StreamLogger` on `StreamLog`. In the Kotlin project, you can install `KotlinStreamLogger` by default, which is a simple logger for Kotlin as seen below:

```kotlin
// install `KotlinStreamLogger`. You only need to do this once.
StreamLog.install(KotlinStreamLogger())

// change the log validator as your taste. 
StreamLog.setValidator { priority, _ ->
  priority.level >= Priority.DEBUG.level
}
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

<img align="right" width="90px" src="https://user-images.githubusercontent.com/24237865/178630165-76855349-ac04-4474-8bcf-8eb5f8c41095.png"/>

## Stream Log File

**Stream Log File** is an extension library for persisting the log messages into an external `.txt` file.

[![Maven Central](https://img.shields.io/maven-central/v/io.getstream/stream-log-file.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.getstream%22%20AND%20a:%22stream-log-file%22)

Add the dependency below into your **module**'s `build.gradle` file:

```gradle
dependencies {
    implementation("io.getstream:stream-log:$version")
    debugImplementation("io.getstream:stream-log-file:$version")
}
```

### FileStreamLogger

You can persist the log messages that are triggered on runtime with `FileStreamLogger`. To persist your log messages into a file, you should use `FileStreamLogger` with `CompositeStreamLogger` like the example below:

```kotlin
val fileLoggerConfig = FileStreamLogger.Config(
    filesDir = fileDirectory, // an internal file directory
    externalFilesDir = null, // an external file directory. This is an optional.
    app = FileStreamLogger.Config.App( // application information.
        versionCode = 1,
        versionName = "1.0.0"
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

Then you will get the result `.txt` file below:

```
======================================================================
Logs date time: 2022-12-02 21:08:35'288
Version code: 1
Version name: 1.0.0
API level: 10
Device: Stream's Mac
======================================================================
2022-11-30 13:02:29'918 D/              This is a log message
2022-11-30 13:04:08'577 D/              ChatViewModel initialized
2022-11-30 13:13:04'640 D/              ChatController initialized
```

<img align="right" width="140px" src="https://user-images.githubusercontent.com/24237865/205479526-5fa0b5f0-22df-4f02-ac0e-7a7a3e050cdb.png"/>

## Stream Log Android

**Stream Log Android** is a simple Android logger on top of the **Stream Log**.

[![Maven Central](https://img.shields.io/maven-central/v/io.getstream/stream-log-android.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.getstream%22%20AND%20a:%22stream-log-android%22)

Add the dependency below into your **module**'s `build.gradle` file:

```gradle
dependencies {
    implementation("io.getstream:stream-log-android:$version")
}
```

### AndroidStreamLogger

First, you need to install a logger for Android with `AndroidStreamLogger` like the below:

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // install AndroidStreamLogger.
        AndroidStreamLogger.installOnDebuggableApp(this)
        
        // change the log validator as your taste.
        StreamLog.setValidator { priority, _ -> priority.level >= Priority.VERBOSE.level }
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

<img align="right" width="140px" src="https://user-images.githubusercontent.com/24237865/205479526-5fa0b5f0-22df-4f02-ac0e-7a7a3e050cdb.png"/>

## Stream Log Android File

**Stream Log Android File** is an extension library for persisting your log messages into external `.txt` files. So you can record the runtime log messages into a `.txt` file, and it will help you to trace the log messages in many complex scenarios.

[![Maven Central](https://img.shields.io/maven-central/v/io.getstream/stream-log-android.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.getstream%22%20AND%20a:%22stream-log-android-file%22)

Add the dependency below into your **module**'s `build.gradle` file:

```gradle
dependencies {
    implementation("io.getstream:stream-log-android:$version")
    debugImplementation("io.getstream:stream-log-android-file:$version")
}
```

### AndroidStreamLogger

First, you need to install a logger for Android with `AndroidStreamLogger` like the below:

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

### Record Runtime Log Messages Into an External File

You don't need to do additional setup for this, because the `stream-log-android-file` dependency will execute all processes automatically. So let's extract the log messages into an external file following the command lines below on your terminal:

1. Build and run your project on your emulator or connect to your real device over Wi-Fi following the [Connect to a device over Wi-Fi](https://developer.android.com/studio/command-line/adb#connect-to-a-device-over-wi-fi) guidelines.
2. Enter in terminal: `adb shell am start-foreground-service -a io.getstream.log.android.CLEAR`
3. You should see the toast message `Logs are cleared!`.
4. Explore your app to record specific log messages.
5. Enter in terminal: `adb shell am start-foreground-service -a io.getstream.log.android.SHARE`
6. You should see a file-sharing dialog chooser in your device.
7. Share the log file via other applications, such as Google Cloud.
8. Exit recording log messages by enter in terminal: `adb shell am stopservice -a io.getstream.log.android.SHARE`

Then you will get the result `.txt` file below:

```
======================================================================
Logs date time: 2022-12-02 21:08:35'288
Version code: 1
Version name: 1.0.1
Android API level: 31
Device: samsung beyond1
======================================================================
2022-11-30 13:02:29'918 D/              main:2 [Main]: onCreate MainActivity
2022-11-30 13:13:06'656 D/              main:2 [Main]: Button clicked
2022-11-30 13:13:07'225 D/              main:2 [Main]: Button clicked
2022-11-30 13:13:07'439 D/              main:2 [Main]: Button clicked
2022-11-30 13:14:23'316 D/              main:2 [Main]: onCreate MainActivity
2022-11-30 13:14:24'296 D/              main:2 [Main]: Button clicked
2022-11-30 13:14:24'723 D/              main:2 [Main]: Button clicked
2022-11-30 16:36:39'102 D/              main:2 [MainActivity]: onCreate MainActivity
2022-11-30 16:42:48'987 D/              main:2 [BoxScopeInstance]: Button Clicked!
2022-11-30 16:42:49'873 D/              main:2 [BoxScopeInstance]: Button Clicked!
```

## Stream Log BOM

The **Stream Log** Bill of Materials (BOM) lets you manage all of your **Stream Log** library versions by specifying only the BOM’s version.

[![Maven Central](https://img.shields.io/maven-central/v/io.getstream/stream-log-android.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.skydoves%22%20AND%20a:%22retrofit-adapters-result%22)

```gradle
dependencies {
    implementation("io.getstream:stream-log-bom:$version")

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
