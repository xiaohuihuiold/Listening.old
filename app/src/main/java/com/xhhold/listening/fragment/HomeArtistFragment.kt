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
import com.xhhold.listening.adapter.ArtistRecyclerViewAdapter
import com.xhhold.listening.databinding.FragmentHomeArtistBinding
import com.xhhold.listening.viewmodel.HomeArtistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeArtistFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: HomeArtistViewModel by viewModels()

    private lateinit var binding: FragmentHomeArtistBinding
    private lateinit var adapter: ArtistRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ArtistRecyclerViewAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
    }

}