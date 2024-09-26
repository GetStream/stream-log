/*
 * Copyright (c) 2014-2022 Stream.io Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.getstream.logging.file

import io.getstream.log.Priority
import io.getstream.log.StreamLogger
import io.getstream.log.helper.stringify
import io.getstream.log.platformThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.toLocalDateTime
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.SYSTEM
import okio.Source

private const val DEFAULT_SIZE: Int = 12 * 1024 * 1024

private const val SHAREABLE_FILE: String = "stream_log_%s.txt"
private const val INTERNAL_FILE_0: String = "internal_0.txt"
private const val INTERNAL_FILE_1: String = "internal_1.txt"

/**
 * The [StreamLogger] implementation with log file persistence.
 */
public class FileStreamLogger(
  private val config: Config,
) : StreamLogger {

  private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO) + SupervisorJob()

  private val timeFormat: DateTimeFormat<LocalDateTime> = LocalDateTime.Formats.ISO

  private val internalPath0: Path = FileSystem.SYSTEM.canonicalize("${config.filesDir}/$INTERNAL_FILE_0".toPath())
  private val internalPath1: Path = FileSystem.SYSTEM.canonicalize("${config.filesDir}/$INTERNAL_FILE_1".toPath())
  private val internalPaths: Array<Path> = arrayOf(internalPath0, internalPath1)

  private var currentPath: Path? = null

  override fun log(
    priority: Priority,
    tag: String,
    message: String,
    throwable: Throwable?,
  ) {
    scope.launch {
      initIfNeeded()
      swapFiles()

      currentPath?.let { path ->
        FileSystem.SYSTEM.write(path) {
          val formattedDateTime =
            timeFormat.format(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
          val formattedThread = platformThread.stringify()
          val formattedPriority = priority.stringify()
          val formatterPrefix = "$formattedDateTime $formattedPriority/$formattedThread [$tag]: "

          writeUtf8(formatterPrefix)
          writeUtf8(message)
          writeUtf8("\n")
          if (throwable != null) {
            writeUtf8("${throwable.printStackTrace()}")
          }
          flush()
        }
      }
    }
  }

  public fun share(callback: (Source) -> Unit) {
    scope.launch {
      generateShareableSource().getOrNull()?.also(callback)
    }
  }

  public fun clear(): Job = scope.launch {
    runCatching {
      FileSystem.SYSTEM.delete(internalPath0)
      FileSystem.SYSTEM.delete(internalPath1)
    }
  }

  private fun initIfNeeded() {
    if (currentPath == null) {
      val internalFile: Path = when {
        !FileSystem.SYSTEM.exists(internalPath0) || !FileSystem.SYSTEM.exists(internalPath1) -> internalPath0
        (FileSystem.SYSTEM.metadata(internalPath0).lastModifiedAtMillis ?: 0L) >=
          (FileSystem.SYSTEM.metadata(internalPath1).lastModifiedAtMillis ?: 0L) -> internalPath0

        else -> internalPath1
      }
      currentPath = internalFile
    }
  }

  private fun swapFiles(): Result<Unit> = runCatching {
    val path = currentPath ?: return@runCatching
    val curLen = FileSystem.SYSTEM.metadata(path).size ?: 0
    if (curLen >= config.maxLogSize / 2) {
      currentPath = if (currentPath === internalPath0) internalPath1 else internalPath0
      FileSystem.SYSTEM.write(currentPath!!) {
        writeUtf8("")
      }
    }
  }

  private fun generateShareableSource(): Result<Source> = runCatching {
    val localDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val formats =
      "${localDateTime.year}${localDateTime.month}${localDateTime.dayOfMonth}" +
        "${localDateTime.hour}${localDateTime.minute}__${localDateTime.second}"
    val filename = SHAREABLE_FILE.replace("%s", formats)
    val shareablePath = filename.toPath()
    FileSystem.SYSTEM.write(shareablePath) {
      writeUtf8(config.buildHeader())
      val paths = internalPaths.asSequence().filter { FileSystem.SYSTEM.exists(it) }
        .sortedBy { FileSystem.SYSTEM.metadata(it).lastModifiedAtMillis }.toList()
      paths.forEach { path ->
        writeAll(FileSystem.SYSTEM.source(path))
      }
    }
    FileSystem.SYSTEM.source(shareablePath)
  }

  private fun Config.buildHeader(): String = """
            |======================================================================
            |Logs date time: ${timeFormat.format(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))}
            |Version code: ${app.versionCode}
            |Version name: ${app.versionName}
            |API level: ${device.androidApiLevel}
            |Device: ${device.model}
            |======================================================================
            |
  """.trimMargin()

  public data class Config(
    val maxLogSize: Int = DEFAULT_SIZE,
    val filesDir: Path,
    val externalFilesDir: Path?,
    val app: App,
    val device: Device,
  ) {
    public data class App(
      val versionCode: Long,
      val versionName: String,
    )

    public data class Device(
      val model: String,
      val androidApiLevel: Int,
    )
  }
}
