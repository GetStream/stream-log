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

public class AppleStreamLogger : KotlinStreamLogger(), StreamLogger {

  public override val now: () -> LocalDateTime =
    { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }

  override fun log(priority: Priority, tag: String, message: String, throwable: Throwable?) {
    val now = now.invoke()
    val thread = platformThread.run { "$name:$id" }
    val composed = "$now ($thread) [${priority.stringify()}/$tag]: $message"
    val finalMessage = throwable?.let {
      "$composed\n${it.stringify()}"
    } ?: composed
    when (priority) {
      Priority.ERROR, Priority.ASSERT -> printlnError(finalMessage)
      else -> println(finalMessage)
    }
  }
}
