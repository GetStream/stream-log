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
package io.getstream.log.android.file

import io.getstream.log.android.file.StreamLogFileManager.ClearManager
import io.getstream.log.android.file.StreamLogFileManager.ShareManager

/**
 * An entry point to share/clear a log file.
 */
public object StreamLogFileManager {

  private var shareManager: ShareManager = ShareManager { }
  private var clearManager: ClearManager = ClearManager { }

  public fun init(shareManager: ShareManager, clearManager: ClearManager) {
    StreamLogFileManager.shareManager = shareManager
    StreamLogFileManager.clearManager = clearManager
  }

  /**
   * Shares log file.
   */
  public fun share() {
    shareManager.share()
  }

  /**
   * Clears log file.
   */
  public fun clear() {
    clearManager.clear()
  }

  /**
   * Shares a log file.
   */
  public fun interface ShareManager {
    public fun share()
  }

  /**
   * Clears a log file.
   */
  public fun interface ClearManager {
    public fun clear()
  }
}
