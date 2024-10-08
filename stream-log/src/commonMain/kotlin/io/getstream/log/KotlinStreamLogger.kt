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
package io.getstream.log

import kotlinx.datetime.LocalDateTime

/**
 * KotlinStreamLogger the abstraction class of that implements [StreamLogger], and the highest-level of platform-specific
 * stream loggers.
 */
public abstract class KotlinStreamLogger : StreamLogger {
  public abstract val now: () -> LocalDateTime

  abstract override fun log(priority: Priority, tag: String, message: String, throwable: Throwable?)

  public abstract fun install(minPriority: Priority = Priority.DEBUG, maxTagLength: Int)

  public companion object {

    /**
     * Install the platform-specific stream logger.
     *
     * Android: AndroidStreamLogger
     * iOS/macOS: AppleStreamLogger
     * Jvm: JvmStreamLogger
     *
     * @param minPriority The min priority/type of a log message.
     * @param maxTagLength The max length size of the tag.
     */
    public fun installPlatformStreamLogger(
      minPriority: Priority = Priority.DEBUG,
      maxTagLength: Int = DEFAULT_MAX_TAG_LENGTH,
    ) {
      platformStreamLogger(maxTagLength = maxTagLength).install(minPriority, maxTagLength)
    }

    private const val DEFAULT_MAX_TAG_LENGTH = 23
  }
}
