package com.xhhold.listening.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel

class HomeViewModel @ViewModelInject constructor(
    application: Application
) : AndroidViewModel(application) {
}