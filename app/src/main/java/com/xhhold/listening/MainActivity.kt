package com.xhhold.listening

import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.xhhold.listening.databinding.ActivityMainBinding
import com.xhhold.media.drawable.TextDrawable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_main)
        binding.callback = object : Callback {
            override fun previous() {
                viewViewModel.musicHelper.skipToPrevious()
            }

            override fun play() {
                viewViewModel.musicHelper.play()
            }

            override fun pause() {
                viewViewModel.musicHelper.pause()
            }

            override fun next() {
                viewViewModel.musicHelper.skipToNext()
            }
        }
        binding.executePendingBindings()
        viewViewModel.musicHelper.playState.observe(this, {
            binding.isPlaying = it?.state == PlaybackStateCompat.STATE_PLAYING
            binding.executePendingBindings()
        })
        viewViewModel.musicHelper.currentMusic.observe(this, {
            binding.music = it
            Glide
                .with(binding.root.context)
                .load(it?.music?.cover ?: it?.album?.cover ?: it?.artist?.cover ?: "")
                .placeholder(TextDrawable(it?.music?.title ?: ""))
                .centerCrop()
                .into(binding.cover)
            binding.executePendingBindings()
        })
    }

    override fun onStart() {
        super.onStart()
        viewViewModel.musicHelper.connect()
    }

    override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        viewViewModel.musicHelper.disconnect()
    }

    interface Callback {
        fun previous()
        fun play()
        fun pause()
        fun next()
    }

}