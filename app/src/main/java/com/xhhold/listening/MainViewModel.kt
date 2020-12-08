package com.xhhold.listening

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import com.xhhold.media.MusicHelper

class MainViewModel @ViewModelInject constructor(
    application: Application,
) : AndroidViewModel(application) {
    val musicHelper = MusicHelper(application)
}