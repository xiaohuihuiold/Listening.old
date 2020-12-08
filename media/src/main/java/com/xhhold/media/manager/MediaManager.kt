package com.xhhold.media.manager

import android.content.Context

object MediaManager {
    fun init(context: Context) {
        FileManager.init(context)
        DataStoreManager.init(context)
    }
}