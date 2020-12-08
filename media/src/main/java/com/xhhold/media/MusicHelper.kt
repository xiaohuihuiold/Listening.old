package com.xhhold.media

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.MutableLiveData
import com.xhhold.media.database.MusicDatabase
import com.xhhold.media.entity.*
import com.xhhold.media.ext.getData
import com.xhhold.media.ext.toSchemaRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MusicHelper(private val context: Context) {
    val isConnected = MutableLiveData<Boolean?>()
    val playState = MutableLiveData<PlaybackStateCompat?>()
    val currentMusic = MutableLiveData<MusicWithAlbumAndArtist?>()
    var currentMetadata: MediaMetadataCompat? = null

    val playlist = MutableLiveData<List<PlaylistWithCounts>>()
    val albums = MutableLiveData<List<AlbumWithCounts>>()
    val artists = MutableLiveData<List<ArtistWithCounts>>()
    val allMusic = MutableLiveData<List<MusicWithAlbumAndArtist>>()
    val favoriteMusic = MutableLiveData<List<MusicWithAlbumAndArtist>>()
    val nowPlaylist = MutableLiveData<List<NowPlaylistWithMusicAndAlbumAndArtists>>()

    private val musicJob = SupervisorJob()
    private val musicScope = CoroutineScope(Dispatchers.Main + musicJob)
    private var mediaController: MediaControllerCompat? = null
    private val mediaBrowser = MediaBrowserCompat(
        context, ComponentName(context, MusicService::class.java), ConnectionCallback(), null
    )

    fun connect() {
        if (isConnected.value != true) {
            mediaBrowser.connect()
        }
    }

    fun disconnect() {
        if (isConnected.value == true) {
            mediaBrowser.disconnect()
        }
        musicJob.cancel()
    }

    fun scan() {
        mediaController?.sendCommand(MediaSessionListener.Command.SCAN, null, null)
    }

    fun play() {
        mediaController?.transportControls?.play()
    }

    fun playFromId(parentId: String) {
        playFromId(parentId, null)
    }

    fun playFromId(parentId: String, childId: String?) {
        mediaController?.transportControls?.playFromMediaId(
            parentId,
            Bundle().apply {
                putString(
                    MediaSessionListener.Command.PLAY_PARAMS_CHILD_ID,
                    childId
                )
            },
        )
    }

    fun pause() {
        mediaController?.transportControls?.pause()
    }

    fun stop() {
        mediaController?.transportControls?.stop()
    }

    fun skipToPrevious() {
        mediaController?.transportControls?.skipToPrevious()
    }

    fun skipToNext() {
        mediaController?.transportControls?.skipToNext()
    }

    fun seekTo(duration: Long) {
        mediaController?.transportControls?.seekTo(duration)
    }

    fun refreshAll() {
        refreshMusic()
        refreshPlaylist()
        refreshAlbums()
        refreshArtists()
        refreshAllMusic()
        refreshFavoriteMusic()
        refreshNowPlaylist()
    }

    fun refreshMusic() {
        subscribe("${MusicSchema.SCHEMA_MUSIC}:${currentMetadata?.description?.mediaId}") {
            if (it.isEmpty()) {
                currentMusic.value = null
            } else {
                currentMusic.value =
                    it.first().description.extras!!.getData<MusicWithAlbumAndArtist>()
            }
        }
    }

    fun refreshPlaylist() {
        subscribe(MusicSchema.SCHEMA_PLAYLIST.toSchemaRoot()) { list ->
            playlist.value = list.map { it.description.extras!!.getData<PlaylistWithCounts>()!! }
        }
    }

    fun refreshAlbums() {
        subscribe(MusicSchema.SCHEMA_ALBUM.toSchemaRoot()) { list ->
            albums.value = list.map { it.description.extras!!.getData<AlbumWithCounts>()!! }
        }
    }

    fun refreshArtists() {
        subscribe(MusicSchema.SCHEMA_ARTIST.toSchemaRoot()) { list ->
            artists.value = list.map { it.description.extras!!.getData<ArtistWithCounts>()!! }
        }
    }

    fun refreshAllMusic() {
        subscribe(MusicSchema.SCHEMA_ALL_MUSIC.toSchemaRoot()) { list ->
            allMusic.value =
                list.map { it.description.extras!!.getData<MusicWithAlbumAndArtist>()!! }
        }
    }

    fun refreshFavoriteMusic() {
        subscribe(MusicSchema.SCHEMA_FAVORITE_MUSIC.toSchemaRoot()) { list ->
            favoriteMusic.value =
                list.map { it.description.extras!!.getData<MusicWithAlbumAndArtist>()!! }
        }
    }

    fun refreshNowPlaylist() {
        subscribe(MusicSchema.SCHEMA_NOW_PLAYLIST.toSchemaRoot()) { list ->
            nowPlaylist.value =
                list.map { it.description.extras!!.getData<NowPlaylistWithMusicAndAlbumAndArtists>()!! }
        }
    }

    fun fetchMusicBySchemaId(schemaId: String): MutableLiveData<List<MusicWithAlbumAndArtist>> {
        val liveData = MutableLiveData<List<MusicWithAlbumAndArtist>>()
        subscribe(schemaId) { list ->
            liveData.value =
                list.map { it.description.extras!!.getData<MusicWithAlbumAndArtist>()!! }
        }
        return liveData
    }

    private fun subscribe(
        schema: String,
        callback: (MutableList<MediaBrowserCompat.MediaItem>) -> Unit
    ) {
        mediaBrowser.unsubscribe(schema)
        mediaBrowser.subscribe(schema, object : MediaBrowserCompat.SubscriptionCallback() {
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                callback(children)
            }
        })
    }

    inner class ControllerCallback : MediaControllerCompat.Callback() {

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            currentMetadata = metadata
            refreshMusic()
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            playState.value = state
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            when (event) {
                MediaSessionListener.Command.SCAN_CALLBACK -> {
                    refreshAll()
                }
                MediaSessionListener.Command.REFRESH_NOW_PLAYLIST -> {
                    refreshNowPlaylist()
                }
            }
        }
    }

    inner class ConnectionCallback : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            isConnected.value = true
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken)
            mediaController?.registerCallback(ControllerCallback().apply {
                onMetadataChanged(mediaController?.metadata)
                onPlaybackStateChanged(mediaController?.playbackState)
            })
        }

        override fun onConnectionFailed() {
            isConnected.value = false
        }

        override fun onConnectionSuspended() {
            isConnected.value = false
        }
    }
}