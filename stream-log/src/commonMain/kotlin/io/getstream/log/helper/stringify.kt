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
package io.getstream.log.helper

import io.getstream.log.PlatformThread
import io.getstream.log.Priority

public fun PlatformThread.stringify(): String {
  return "$name:$id"
}

public fun Priority.stringify(): String = when (this) {
  Priority.VERBOSE -> "V"
  Priority.DEBUG -> "D"
  Priority.INFO -> "I"
  Priority.WARN -> "W"
  Priority.ERROR -> "E"
  Priority.ASSERT -> "E"
}

public fun Throwable.stringify(): String {
  // Don't replace this with Log.getStackTraceString() - it hides
  // UnknownHostException, which is not what we want.
  this.printStackTrace()
  return this.stackTraceToString()
}
