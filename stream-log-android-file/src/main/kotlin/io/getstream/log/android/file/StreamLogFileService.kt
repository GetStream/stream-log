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

package io.getstream.log.android.file

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

private const val ACTION_SHARE = "io.getstream.log.android.SHARE"
private const val ACTION_CLEAR = "io.getstream.log.android.CLEAR"

/**
 * The service handles adb commands to share/clear the log file.
 *
 * You can share the runtime logging messages following the methods below:
 *
 * 1. adb shell am start-foreground-service -a io.getstream.log.android.CLEAR
 * 2. adb shell am start-foreground-service -a io.getstream.log.android.SHARE
 * 3. adb shell am stopservice -a io.getstream.log.android.SHARE
 */
public class StreamLogFileService : Service() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        val channelId = "io.getstream.log.android.file"
        val channelName = "Stream Log File Service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.stream_logo)
            .setContentTitle("Stream Log File Service")
            .setContentText("Starting Stream Log File Service...")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)

        val notification = notificationBuilder.build()

        startForeground(1004, notification)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SHARE -> StreamLogFileManager.share()
            ACTION_CLEAR -> StreamLogFileManager.clear()
        }
        return super.onStartCommand(intent, flags, startId)
    }
}
