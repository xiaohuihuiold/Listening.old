package com.xhhold.listening.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.xhhold.listening.fragment.HomeAlbumFragment
import com.xhhold.listening.fragment.HomeArtistFragment
import com.xhhold.listening.fragment.HomeNowPlaylistFragment
import com.xhhold.listening.fragment.HomePlaylistFragment

class HomeFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeNowPlaylistFragment()
            1 -> HomePlaylistFragment()
            2 -> HomeAlbumFragment()
            else -> HomeArtistFragment()
        }
    }
}