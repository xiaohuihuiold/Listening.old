package com.xhhold.listening.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xhhold.listening.MainViewModel
import com.xhhold.listening.adapter.NowPlaylistRecyclerViewAdapter
import com.xhhold.listening.databinding.FragmentHomeNowPlaylistBinding
import com.xhhold.listening.viewmodel.HomeNowPlaylistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeNowPlaylistFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: HomeNowPlaylistViewModel by viewModels()

    private lateinit var binding: FragmentHomeNowPlaylistBinding
    private lateinit var adapter: NowPlaylistRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeNowPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = NowPlaylistRecyclerViewAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }
}