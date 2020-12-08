package com.xhhold.listening.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import com.xhhold.media.repository.MusicRepository

class HomeArtistViewModel @ViewModelInject constructor(
    application: Application
) : AndroidViewModel(application) {
}