package com.xhhold.media.work

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.xhhold.media.ext.openFileDescriptorRead
import com.xhhold.media.manager.FileManager
import com.xhhold.media.MusicFactory
import com.xhhold.media.database.MusicDatabase
import com.xhhold.media.repository.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class ScanWorker constructor(
    appContext: Context,
    workerParams: WorkerParameters,
) :
    Worker(appContext, workerParams) {

    private val musicDatabase = MusicDatabase.getInstance(applicationContext)
    private val musicRepository = MusicRepository(musicDatabase)

    companion object {
        private const val TAG = "ScanWork"
    }

    override fun doWork(): Result {
        Log.i(TAG, "doWork")
        val contentResolver = applicationContext.contentResolver
        val sources = arrayListOf<String>()

        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Audio.Media._ID),
            null,
            null,
            null
        )?.apply {
            val columnIndex = getColumnIndex(MediaStore.Audio.Media._ID)
            while (moveToNext()) {
                val uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    getLong(columnIndex)
                )
                sources.add(uri.toString())
            }
            close()
        }

        sources.forEachIndexed { index: Int, source: String ->
            runBlocking {
                Log.i(TAG, "${index + 1}/${sources.size}: $source")
                val fileDescriptor = source.openFileDescriptorRead(contentResolver)
                    ?: return@runBlocking
                val music = MusicFactory.decodeFromPath(
                    source,
                    fileDescriptor.fileDescriptor
                ) { md5: String, bytes: ByteArray? ->
                    if (bytes == null) {
                        return@decodeFromPath null
                    }
                    FileManager.saveAlbumImageCache(md5, bytes)?.toString()
                }
                fileDescriptor.close()
                if (music != null) {
                    musicRepository.addMusic(music)
                }
            }
        }

        return Result.success()
    }
}