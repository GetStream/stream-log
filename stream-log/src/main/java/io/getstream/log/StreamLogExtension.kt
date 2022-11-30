/*
 * Copyright (c) 2014-2022 Stream.io Inc. All rights reserved.
 *
 * Licensed under the Stream License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://github.com/GetStream/stream-log/blob/main/LICENSE
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
