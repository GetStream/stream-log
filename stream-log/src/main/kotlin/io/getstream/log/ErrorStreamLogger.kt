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

import io.getstream.log.Priority.ASSERT
import io.getstream.log.Priority.ERROR
import io.getstream.log.kotlin.KotlinStreamLogger

/**
 * An [StreamLogger] implementation only log Error logs.
 */
internal object ErrorStreamLogger : KotlinStreamLogger() {

    override fun log(priority: Priority, tag: String, message: String, throwable: Throwable?) {
        when (priority) {
            ERROR, ASSERT -> super.log(priority, tag, message, throwable)
            else -> { /* NO-OP */ }
        }
    }
}
