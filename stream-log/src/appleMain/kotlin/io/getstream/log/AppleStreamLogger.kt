/*
 * Copyright (c) 2014-2024 Stream.io Inc. All rights reserved.
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
package io.getstream.log

import io.getstream.log.helper.stringify
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import platform.Foundation.NSLog
import platform.Foundation.NSThread

public class AppleStreamLogger(
  private val maxTagLength: Int = DEFAULT_MAX_TAG_LENGTH,
) : KotlinStreamLogger(), StreamLogger {

  public override val now: () -> LocalDateTime =
    { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }

  override fun log(priority: Priority, tag: String, message: String, throwable: Throwable?) {
    val appleTag = tag.takeIf { it.length > maxTagLength }
      ?.substring(0, maxTagLength)
      ?: tag

    val thread = NSThread.currentThread().run { "$name:$threadPriority" }
    val composed = "($thread) $message"
    val finalMessage = throwable?.let {
      "$composed\n${it.stringify()}"
    } ?: composed

    NSLog("[$priority] ($appleTag) $finalMessage")
  }

  public companion object {
    /**
     * Install a new [AppleStreamLogger].
     *
     * @param minPriority The minimum [Priority] to show up log messages.
     * @param maxTagLength The maximum length size of the tag.
     */
    public fun install(minPriority: Priority = Priority.DEBUG, maxTagLength: Int = DEFAULT_MAX_TAG_LENGTH) {
      StreamLog.setValidator { priority, _ -> priority.level >= minPriority.level }
      StreamLog.install(AppleStreamLogger(maxTagLength = maxTagLength))
    }

    internal const val DEFAULT_MAX_TAG_LENGTH = 23
  }
}