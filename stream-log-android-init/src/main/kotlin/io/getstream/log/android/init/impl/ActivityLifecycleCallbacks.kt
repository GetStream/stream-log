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

package io.getstream.log.android.init.impl

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Stub/no-op implementations of all methods of [Application.ActivityLifecycleCallbacks].
 * Override this if you only care about a few of the available callback methods.
 */
internal abstract class ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, bunlde: Bundle?) { /* no-op */ }

    override fun onActivityStarted(activity: Activity) { /* no-op */ }

    override fun onActivityResumed(activity: Activity) { /* no-op */ }

    override fun onActivityPaused(activity: Activity) { /* no-op */ }

    override fun onActivityStopped(activity: Activity) { /* no-op */ }

    override fun onActivitySaveInstanceState(activity: Activity, bunlde: Bundle) { /* no-op */ }

    override fun onActivityDestroyed(activity: Activity) { /* no-op */ }
}
