package com.xhhold.media.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.xhhold.media.entity.Album
import com.xhhold.media.entity.AlbumWithCounts
import com.xhhold.media.entity.AlbumWithMusicAndArtists

@Dao
interface AlbumDao : BaseDao<Album> {

    @Query("SELECT album.*,COUNT(album.id) AS counts FROM album LEFT JOIN music ON music.album_id = album.id GROUP BY album.id")
    fun getAllAlbumWithCounts(): List<AlbumWithCounts>

    @Query("SELECT album.*,COUNT(album.id) AS counts FROM album LEFT JOIN music ON music.album_id = album.id GROUP BY album.id HAVING album.id = :albumId")
    fun getAlbumWithCountsById(albumId: Long): AlbumWithCounts?

    @Transaction
    @Query("SELECT * FROM album WHERE album.id = :albumId")
    fun getAlbumWithMusicAndArtistsById(albumId: Long): AlbumWithMusicAndArtists?

    @Query("SELECT * FROM album WHERE `key` = :albumKey")
    fun getAlbumByKey(albumKey: String): Album?

}