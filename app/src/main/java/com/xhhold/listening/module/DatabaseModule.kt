package com.xhhold.listening.module

import android.content.Context
import com.xhhold.media.dao.*
import com.xhhold.media.database.MusicDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MusicDatabase {
        return MusicDatabase.getInstance(context)
    }

    @Provides
    fun provideAlbumDao(database: MusicDatabase): AlbumDao = database.albumDao()

    @Provides
    fun provideArtistDao(database: MusicDatabase): ArtistDao = database.artistDao()

    @Provides
    fun provideMusicDao(database: MusicDatabase): MusicDao = database.musicDao()

    @Provides
    fun providePlaylistDao(database: MusicDatabase): PlaylistDao = database.playlistDao()

    @Provides
    fun provideNowPlaylistDao(database: MusicDatabase): NowPlaylistDao = database.nowPlaylistDao()
}