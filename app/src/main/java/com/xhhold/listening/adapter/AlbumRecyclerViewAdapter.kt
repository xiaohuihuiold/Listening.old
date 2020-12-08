package com.xhhold.listening.adapter

import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xhhold.listening.databinding.ListItemAlbumBinding
import com.xhhold.media.drawable.TextDrawable

class AlbumRecyclerViewAdapter :
    ListAdapter<MediaBrowserCompat.MediaItem, AlbumRecyclerViewAdapter.AlbumRecyclerViewHolder>(
        MediaItemListDiffCallback()
    ) {

    var itemClick: OnItemClick? = null

    fun setOnItemClick(onItemClick: OnItemClick) {
        itemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumRecyclerViewHolder {
        return AlbumRecyclerViewHolder(
            ListItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlbumRecyclerViewHolder, position: Int) {
        holder.bind(getItem(position), position, itemClick)
    }

    class AlbumRecyclerViewHolder(
        private val binding: ListItemAlbumBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MediaBrowserCompat.MediaItem, position: Int, itemClick: OnItemClick?) {
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

    fun interface OnItemClick {
        fun onItem(id: String)
    }
}