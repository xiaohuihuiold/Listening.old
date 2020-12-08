package com.xhhold.listening.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.xhhold.listening.MainViewModel
import com.xhhold.listening.adapter.AlbumRecyclerViewAdapter
import com.xhhold.listening.databinding.FragmentHomeAlbumBinding
import com.xhhold.listening.viewmodel.HomeAlbumViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeAlbumFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: HomeAlbumViewModel by viewModels()

    private lateinit var binding: FragmentHomeAlbumBinding
    private lateinit var adapter: AlbumRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = AlbumRecyclerViewAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
    }

}