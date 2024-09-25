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
package io.getstream.log.kotlin

import io.getstream.log.Priority
import io.getstream.log.Priority.ASSERT
import io.getstream.log.Priority.ERROR
import io.getstream.log.StreamLogger
import io.getstream.log.helper.stringify
import io.getstream.log.platformThread
import io.getstream.log.printlnError
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * The [StreamLogger] implementation for kotlin projects. Mainly used in Unit Tests.
 */
public open class KotlinStreamLogger(
  private val now: () -> LocalDateTime = { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) },
) : StreamLogger {

  override fun log(priority: Priority, tag: String, message: String, throwable: Throwable?) {
    val now = now.invoke()
    val thread = platformThread.run { "$name:$id" }
    val composed = "$now ($thread) [${priority.stringify()}/$tag]: $message"
    val finalMessage = throwable?.let {
      "$composed\n${it.stringify()}"
    } ?: composed
    when (priority) {
      ERROR, ASSERT -> printlnError(finalMessage)
      else -> println(finalMessage)
    }
  }
}
