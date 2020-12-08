package com.xhhold.listening.adapter

import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xhhold.listening.databinding.ListItemNowPlaylistBinding
import com.xhhold.media.drawable.TextDrawable

class PlaylistRecyclerViewAdapter :
    ListAdapter<MediaBrowserCompat.MediaItem, PlaylistRecyclerViewAdapter.NowPlaylistRecyclerViewHolder>(
        MediaItemListDiffCallback()
    ) {

    var itemClick: AlbumRecyclerViewAdapter.OnItemClick? = null

    fun setOnItemClick(onItemClick: AlbumRecyclerViewAdapter.OnItemClick) {
        itemClick = onItemClick
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NowPlaylistRecyclerViewHolder {
        return NowPlaylistRecyclerViewHolder(
            ListItemNowPlaylistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: NowPlaylistRecyclerViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), position, itemClick)
    }

    class NowPlaylistRecyclerViewHolder(
        private val binding: ListItemNowPlaylistBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: MediaBrowserCompat.MediaItem,
            position: Int,
            itemClick: AlbumRecyclerViewAdapter.OnItemClick?
        ) {
            Glide
                .with(binding.root.context)
                .load(item.description.iconUri)
                .placeholder(TextDrawable(item.description.title?.toString() ?: ""))
                .centerCrop()
                .into(binding.cover)
            binding.root.setOnClickListener {
                itemClick?.onItem(item.description.mediaId!!)
            }
            binding.apply {
                this.item = item
                executePendingBindings()
            }
        }
    }

}
