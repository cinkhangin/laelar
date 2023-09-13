package com.laelar.app.welcome

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.laelar.app.App
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private val context = getApplication<App>()
}