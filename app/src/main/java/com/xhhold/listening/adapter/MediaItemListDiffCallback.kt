package com.xhhold.listening.adapter

import android.support.v4.media.MediaBrowserCompat
import androidx.recyclerview.widget.DiffUtil

class MediaItemListDiffCallback : DiffUtil.ItemCallback<MediaBrowserCompat.MediaItem>() {
    override fun areItemsTheSame(
        oldItem: MediaBrowserCompat.MediaItem,
        newItem: MediaBrowserCompat.MediaItem
    ): Boolean {
        return oldItem.description.mediaId == oldItem.description.mediaId
    }

    override fun areContentsTheSame(
        oldItem: MediaBrowserCompat.MediaItem,
        newItem: MediaBrowserCompat.MediaItem
    ): Boolean {
        return oldItem.description.mediaId == oldItem.description.mediaId && newItem.description.mediaUri == oldItem.description.mediaUri
    }
}