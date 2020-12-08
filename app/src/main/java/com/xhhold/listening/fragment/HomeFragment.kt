package com.xhhold.listening.fragment

import android.Manifest
import android.os.Bundle
import android.support.v4.media.session.MediaControllerCompat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.PermissionChecker
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.xhhold.listening.MainViewModel
import com.xhhold.listening.adapter.HomeFragmentAdapter
import com.xhhold.listening.databinding.FragmentHomeBinding
import com.xhhold.listening.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        const val TAG = "HomeFragment"
        const val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 0x01
    }

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.callback = object : Callback {
            override fun add() {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_READ_EXTERNAL_STORAGE
                )
            }

            override fun play() {
                mainViewModel.musicHelper.play()
            }
        }
        binding.pager.adapter = HomeFragmentAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = "正在播放"
                1 -> tab.text = "播放列表"
                2 -> tab.text = "专辑"
                3 -> tab.text = "艺术家"
            }
        }.attach()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.size == 1 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                    Log.i(javaClass.simpleName, "权限允许")
                    mainViewModel.musicHelper.scan()
                } else {
                    Log.i(javaClass.simpleName, "权限拒绝")
                }
            }
        }
    }

    interface Callback {
        fun add()
        fun play()
    }

}
