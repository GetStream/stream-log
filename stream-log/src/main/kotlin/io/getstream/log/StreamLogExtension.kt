/*
 * Copyright 2022 Stream.IO, Inc. All Rights Reserved.
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

/**
 * Low-level logging call extension.
 *
 * @param priority The priority/type of this log message. [Priority.DEBUG] is default.
 * @param tag Used to identify the source of a log message.
 * @param message The message you would like logged.
 * @param throwable An exception to log.
 *
 * @see Priority
 *
 * ```
 * class MainViewModel {
 *
 *   fun fetchNetworkItems() {
 *     streamLog { "Fetching network items.." }
 *
 *     val networkItem = repository.networkItems()
 *
 *     streamLog { "Network Items: $networkItem" }
 *   }
 * }
 * ```
 */
public inline fun Any.streamLog(
    priority: Priority = Priority.DEBUG,
    tag: String? = null,
    throwable: Throwable? = null,
    message: () -> String,
) {
    val tagOrCaller = tag ?: outerClassSimpleTagName()
    StreamLog.log(priority, tagOrCaller, throwable, message)
}

/**
 * Return [TaggedLogger] lazily.
 *
 * @param tag Used to identify the source of a log message.
 *
 * ```
 * class MainController {
 *
 *   val logger by taggedLogger()
 *
 *   fun onItemClicked() {
 *     logger.d { "item clicked" }
 *   }
 * }
 */
public fun Any.taggedLogger(
    tag: String? = null,
): Lazy<TaggedLogger> {
    val tagOrCaller = tag ?: outerClassSimpleTagName()
    return lazy { StreamLog.getLogger(tagOrCaller) }
}

@PublishedApi
internal fun Any.outerClassSimpleTagName(): String {
    val javaClass = this::class.java
    val fullClassName = javaClass.name
    val outerClassName = fullClassName.substringBefore('$')
    val simplerOuterClassName = outerClassName.substringAfterLast('.')
    return if (simplerOuterClassName.isEmpty()) {
        fullClassName
    } else {
        simplerOuterClassName.removeSuffix("Kt")
    }
}
