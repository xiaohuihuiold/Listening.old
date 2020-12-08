package com.xhhold.media.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.xhhold.media.entity.NowPlaylist
import com.xhhold.media.entity.NowPlaylistWithMusicAndAlbumAndArtists

@Dao
interface NowPlaylistDao : BaseDao<NowPlaylist> {

    @Transaction
    @Query("SELECT * FROM now_playlist")
    fun getPlayingMusicWithAlbumAndArtists(): List<NowPlaylistWithMusicAndAlbumAndArtists>

    @Query("DELETE FROM now_playlist")
    fun deleteAll(): Int

    @Query("DELETE FROM now_playlist WHERE music_id = :musicId")
    fun deleteMusicById(musicId: Long): Int
}