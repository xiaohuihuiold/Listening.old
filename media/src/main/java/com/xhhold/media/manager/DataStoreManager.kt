package com.xhhold.media.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import com.xhhold.media.datastore.MusicPlayerData
import com.xhhold.media.datastore.MusicPlayerStore

object DataStoreManager {

    lateinit var musicPlayerDataStore: DataStore<MusicPlayerData>

    fun init(context: Context) {
        context.apply {
            musicPlayerDataStore = createDataStore(fileName = "music_player", MusicPlayerStore)
        }
    }
}