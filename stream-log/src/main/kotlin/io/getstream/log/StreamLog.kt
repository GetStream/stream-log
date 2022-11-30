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

import io.getstream.log.Priority.ASSERT
import io.getstream.log.Priority.DEBUG
import io.getstream.log.Priority.ERROR
import io.getstream.log.Priority.INFO
import io.getstream.log.Priority.VERBOSE
import io.getstream.log.Priority.WARN

/**
 * API for sending log output.
 *
 * Generally, you should use the [StreamLog.v], [StreamLog.d],
 * [StreamLog.i], [StreamLog.w], and [StreamLog.e] methods to write logs.
 *
 * The order in terms of verbosity, from least to most is [ERROR], [WARN], [INFO], [DEBUG], [VERBOSE].
 *
 */
@Suppress("TooManyFunctions")
public object StreamLog {

    /**
     * Let you know if the internal StreamLogger instance used for logs has been initialized or it is using the
     * default one
     */
    public var defaultLoggerOverridden: Boolean = false
        private set

    /**
     * [StreamLogger] implementation to be used.
     */
    @PublishedApi
    internal var internalLogger: StreamLogger = ErrorStreamLogger
        private set(value) {
            defaultLoggerOverridden = true
            field = value
        }

    /**
     * [IsLoggableValidator] implementation to be used.
     */
    @PublishedApi
    internal var internalValidator: IsLoggableValidator = IsLoggableValidator { priority, _ ->
        priority.level >= ERROR.level
    }
        private set

    /**
     * Sets a [StreamLogger] implementation to be used.
     */
    @JvmStatic
    public fun setLogger(logger: StreamLogger) {
        internalLogger = logger
    }

    /**
     * Sets a [IsLoggableValidator] implementation to be used.
     */
    @JvmStatic
    public fun setValidator(validator: IsLoggableValidator) {
        internalValidator = validator
    }

    /**
     * Returns a tagged logger.
     *
     * @return [TaggedLogger] Tagged logger.
     */
    @JvmStatic
    public fun getLogger(tag: String): TaggedLogger = TaggedLogger(tag, internalLogger, internalValidator)

    /**
     * Send a [ERROR] log message.
     *
     * @param tag Used to identify the source of a log message.
     * @param throwable An exception to log.
     * @param message The function returning a message you would like logged.
     */
    @JvmStatic
    public inline fun e(tag: String, throwable: Throwable, message: () -> String) {
        if (internalValidator.isLoggable(ERROR, tag)) {
            internalLogger.log(ERROR, tag, message(), throwable)
        }
    }

    /**
     * Send a [ERROR] log message.
     *
     * @param tag Used to identify the source of a log message.
     * @param message The function returning a message you would like logged.
     */
    @JvmStatic
    public inline fun e(tag: String, message: () -> String) {
        if (internalValidator.isLoggable(ERROR, tag)) {
            internalLogger.log(ERROR, tag, message())
        }
    }

    /**
     * Send a [WARN] log message.
     *
     * @param tag Used to identify the source of a log message.
     * @param message The function returning a message you would like logged.
     */
    @JvmStatic
    public inline fun w(tag: String, message: () -> String) {
        if (internalValidator.isLoggable(WARN, tag)) {
            internalLogger.log(WARN, tag, message())
        }
    }

    /**
     * Send a [INFO] log message.
     *
     * @param tag Used to identify the source of a log message.
     * @param message The function returning a message you would like logged.
     */
    @JvmStatic
    public inline fun i(tag: String, message: () -> String) {
        if (internalValidator.isLoggable(INFO, tag)) {
            internalLogger.log(INFO, tag, message())
        }
    }

    /**
     * Send a [DEBUG] log message.
     *
     * @param tag Used to identify the source of a log message.
     * @param message The function returning a message you would like logged.
     */
    @JvmStatic
    public inline fun d(tag: String, message: () -> String) {
        if (internalValidator.isLoggable(DEBUG, tag)) {
            internalLogger.log(DEBUG, tag, message())
        }
    }

    /**
     * Send a [VERBOSE] log message.
     *
     * @param tag Used to identify the source of a log message.
     * @param message The function returning a message you would like logged.
     */
    @JvmStatic
    public inline fun v(tag: String, message: () -> String) {
        if (internalValidator.isLoggable(VERBOSE, tag)) {
            internalLogger.log(VERBOSE, tag, message())
        }
    }

    /**
     * Send a [ASSERT] log message.
     *
     * @param tag Used to identify the source of a log message.
     * @param message The function returning a message you would like logged.
     */
    @JvmStatic
    public inline fun a(tag: String, message: () -> String) {
        if (internalValidator.isLoggable(ASSERT, tag)) {
            internalLogger.log(ASSERT, tag, message())
        }
    }

    /**
     * Send a log message according to the [priority].
     *
     * @param priority The priority/type of this log message.
     * @param tag Used to identify the source of a log message.
     * @param message The function returning a message you would like logged.
     * @param throwable An exception to log.
     */
    @JvmStatic
    public inline fun log(priority: Priority, tag: String, throwable: Throwable? = null, message: () -> String) {
        if (throwable != null) {
            e(tag, throwable, message)
        } else {
            when (priority) {
                VERBOSE -> v(tag, message)
                DEBUG -> d(tag, message)
                INFO -> i(tag, message)
                WARN -> w(tag, message)
                ERROR -> e(tag, message)
                ASSERT -> a(tag, message)
            }
        }
    }
}

/**
 * Validates if message can be logged.
 */
public fun interface IsLoggableValidator {

    /**
     * Validates [priority] and [tag] of a message you would like logged.
     *
     * @param priority The priority/type of a log message.
     * @param tag Used to identify the source of a log message.
     */
    public fun isLoggable(priority: Priority, tag: String): Boolean
}

/**
 * Represents a tagged logger.
 */
public class TaggedLogger(
    @PublishedApi internal val tag: String,
    @PublishedApi internal val delegate: StreamLogger,
    @PublishedApi internal var validator: IsLoggableValidator,
) {

    /**
     * Send a [ERROR] log message.
     *
     * @param throwable An exception to log.
     * @param message The function returning a message you would like logged.
     */
    public inline fun e(throwable: Throwable, message: () -> String) {
        if (validator.isLoggable(ERROR, tag)) {
            delegate.log(ERROR, tag, message(), throwable)
        }
    }

    /**
     * Send a [ERROR] log message.
     *
     * @param message The function returning a message you would like logged.
     */
    public inline fun e(message: () -> String) {
        if (validator.isLoggable(ERROR, tag)) {
            delegate.log(ERROR, tag, message())
        }
    }

    /**
     * Send a [WARN] log message.
     *
     * @param message The function returning a message you would like logged.
     */
    public inline fun w(message: () -> String) {
        if (validator.isLoggable(WARN, tag)) {
            delegate.log(WARN, tag, message())
        }
    }

    /**
     * Send a [INFO] log message.
     *
     * @param message The function returning a message you would like logged.
     */
    public inline fun i(message: () -> String) {
        if (validator.isLoggable(INFO, tag)) {
            delegate.log(INFO, tag, message())
        }
    }

    /**
     * Send a [DEBUG] log message.
     *
     * @param message The function returning a message you would like logged.
     */
    public inline fun d(message: () -> String) {
        if (validator.isLoggable(DEBUG, tag)) {
            delegate.log(DEBUG, tag, message())
        }
    }

    /**
     * Send a [VERBOSE] log message.
     *
     * @param message The function returning a message you would like logged.
     */
    public inline fun v(message: () -> String) {
        if (validator.isLoggable(VERBOSE, tag)) {
            delegate.log(VERBOSE, tag, message())
        }
    }
}
