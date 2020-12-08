package com.xhhold.media

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.xhhold.media.database.MusicDatabase
import com.xhhold.media.entity.MusicWithAlbumAndArtist
import com.xhhold.media.ext.*
import com.xhhold.media.manager.DataStoreManager
import com.xhhold.media.player.DefaultMusicAdapter
import com.xhhold.media.player.MusicPlayer
import com.xhhold.media.player.MusicPlayerStateListener
import com.xhhold.media.repository.MusicRepository
import com.xhhold.media.work.ScanWorker
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.util.*

class MusicService : MediaBrowserServiceCompat() {

    lateinit var mediaSession: MediaSessionCompat
    lateinit var musicRepository: MusicRepository
    private lateinit var mediaSessionListener: MediaSessionListener
    private val serviceJob = SupervisorJob()
    val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    var currentMetadata: MediaMetadataCompat? = null

    companion object {
        const val TAG = "MusicService"
        const val BROWSER_ROOT = "root"
    }

    override fun onCreate() {
        super.onCreate()
        mediaSessionListener = MediaSessionListener(this)
        musicRepository = MusicRepository(MusicDatabase.getInstance(this))
        mediaSession = MediaSessionCompat(this, TAG).apply {
            setFlags(MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS)
            setSessionToken(sessionToken)
            setCallback(mediaSessionListener)
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopSelf()
    }

    override fun onDestroy() {
        serviceJob.cancel()
        mediaSessionListener.onStop()
        mediaSession.isActive = false
        mediaSession.release()
        musicRepository.close()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(BROWSER_ROOT, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        when (parentId) {
            BROWSER_ROOT -> result.sendResult(musicRepository.getRoot(applicationContext))
            else -> {
                result.detach()
                serviceScope.launch(Dispatchers.IO) {
                    result.sendResult(musicRepository.getMediaItem(parentId))
                }
            }
        }
    }
}