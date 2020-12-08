package com.xhhold.listening.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.xhhold.listening.MainViewModel
import com.xhhold.listening.adapter.PlaylistRecyclerViewAdapter
import com.xhhold.listening.databinding.FragmentHomeNowPlaylistBinding
import com.xhhold.listening.viewmodel.HomePlaylistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePlaylistFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: HomePlaylistViewModel by viewModels()

    private lateinit var binding: FragmentHomeNowPlaylistBinding
    private lateinit var adapter: PlaylistRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeNowPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = PlaylistRecyclerViewAdapter()
        binding.recyclerView.adapter = adapter
    }

}