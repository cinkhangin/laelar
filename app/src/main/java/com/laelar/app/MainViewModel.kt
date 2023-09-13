package com.laelar.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.laelar.core.assets.Books
import com.laelar.core.database.DataManager
import com.laelar.core.models.Version
import com.naulian.anhance.PrefStore
import com.naulian.anhance.readStringAsset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private val context = getApplication<App>()

    private val mutableLoading = MutableStateFlow(true)
    val loading = mutableLoading.asStateFlow()

    val books get() = Books.list()
    private val prefStore = PrefStore(context)

    fun checkData() {
        viewModelScope.launch {
            val versionData = context.readStringAsset("lesson_version")
            val version = Json.decodeFromString<Version>(versionData)

            val current = prefStore.readInt("version", -1)
            if (version.code > current) {
                DataManager.cleanUp(context) {
                    prefStore.writeInt("version", version.code)
                    mutableLoading.value = false
                    this.cancel()
                }
                return@launch
            }

            mutableLoading.value = false
            this.cancel()
        }
    }
}