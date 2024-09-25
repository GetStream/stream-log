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
package io.getstream.log.android

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.Build
import android.util.Log
import androidx.annotation.ChecksSdkIntAtLeast
import io.getstream.log.Priority
import io.getstream.log.StreamLog
import io.getstream.log.StreamLogger
import io.getstream.log.helper.stringify

/**
 * The [StreamLogger] implementation for Android.
 *
 * This logger traces the current thread on Android and following the Android log priorities.
 *
 * @property maxTagLength The maximum length size of the tag.
 */
public class AndroidStreamLogger constructor(
  private val maxTagLength: Int = DEFAULT_MAX_TAG_LENGTH,
) : StreamLogger {

  override fun log(priority: Priority, tag: String, message: String, throwable: Throwable?) {

    val androidPriority = priority.toAndroidPriority()
    val androidTag = tag.takeIf { it.length > maxTagLength && !isNougatOrHigher() }
      ?.substring(0, maxTagLength)
      ?: tag

    val thread = Thread.currentThread().run { "$name:$id" }
    val composed = "($thread) $message"
    val finalMessage = throwable?.let {
      "$composed\n${it.stringify()}"
    } ?: composed

    Log.println(androidPriority, androidTag, finalMessage)
  }

  private fun Priority.toAndroidPriority(): Int {
    return when (this) {
      Priority.VERBOSE -> Log.VERBOSE
      Priority.DEBUG -> Log.DEBUG
      Priority.INFO -> Log.INFO
      Priority.WARN -> Log.WARN
      Priority.ERROR -> Log.ERROR
      Priority.ASSERT -> Log.ASSERT
      else -> Log.ERROR
    }
  }

  @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
  private fun isNougatOrHigher() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

  public companion object {
    private val Application.isDebuggableApp: Boolean
      get() = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    /**
     * Install a new [AndroidStreamLogger] if the application is debuggable.
     *
     * @param application Application.
     * @param minPriority The minimum [Priority] to show up log messages.
     * @param maxTagLength The maximum length size of the tag.
     */
    public fun installOnDebuggableApp(
      application: Application,
      minPriority: Priority = Priority.DEBUG,
      maxTagLength: Int = DEFAULT_MAX_TAG_LENGTH,
    ) {
      if (!StreamLog.isInstalled && application.isDebuggableApp) {
        StreamLog.setValidator { priority, _ -> priority.level >= minPriority.level }
        StreamLog.install(AndroidStreamLogger(maxTagLength = maxTagLength))
      }
    }

    /**
     * Install a new [AndroidStreamLogger].
     *
     * @param minPriority The minimum [Priority] to show up log messages.
     * @param maxTagLength The maximum length size of the tag.
     */
    public fun install(minPriority: Priority = Priority.DEBUG, maxTagLength: Int = DEFAULT_MAX_TAG_LENGTH) {
      StreamLog.setValidator { priority, _ -> priority.level >= minPriority.level }
      StreamLog.install(AndroidStreamLogger(maxTagLength = maxTagLength))
    }

    internal const val DEFAULT_MAX_TAG_LENGTH = 23
  }
}
