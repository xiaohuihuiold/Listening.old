package com.xhhold.listening.adapter

import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xhhold.listening.databinding.ListItemArtistBinding
import com.xhhold.media.drawable.TextDrawable
import com.xhhold.media.entity.ArtistWithCounts

class ArtistRecyclerViewAdapter :
    ListAdapter<MediaBrowserCompat.MediaItem, ArtistRecyclerViewAdapter.ArtistRecyclerViewHolder>(
        MediaItemListDiffCallback()
    ) {

    var itemClick: AlbumRecyclerViewAdapter.OnItemClick? = null

    fun setOnItemClick(onItemClick: AlbumRecyclerViewAdapter.OnItemClick) {
        itemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistRecyclerViewHolder {
        return ArtistRecyclerViewHolder(
            ListItemArtistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ArtistRecyclerViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), position, itemClick)
    }

    class ArtistRecyclerViewHolder(
        private val binding: ListItemArtistBinding
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