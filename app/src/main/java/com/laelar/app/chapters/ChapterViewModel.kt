package com.laelar.app.chapters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.laelar.app.App
import com.laelar.core.database.AppDatabase
import com.laelar.core.database.DataManager
import com.laelar.core.database.DataManager.loadChapter
import com.laelar.core.models.Block
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private val context = getApplication<App>()

    val chapter = DataManager.chapter

    private val database = AppDatabase.create(context)
    private val dao = database.dao()

    private var syncJob: Job? = null
    private var daoJob: Job? = null
    private var viewJob: Job? = null
    private var viewJob2: Job? = null

    fun loadChapter(chapterId: String) {
        DataManager.clearChapter()
        syncJob?.cancel()
        syncJob = viewModelScope.launch {
            loadChapter(dao, chapterId)
        }
    }

    fun learn(chapterId: String) {
        daoJob?.cancel()
        daoJob = viewModelScope.launch {
            DataManager.learn(dao, chapterId)
        }
    }

    fun unlearned(chapterId: String) {
        daoJob?.cancel()
        daoJob = viewModelScope.launch {
            DataManager.unLearned(dao, chapterId)
        }
    }

    fun view(chapterId: String) {
        viewJob?.cancel()
        viewJob = viewModelScope.launch {
            DataManager.view(dao, chapterId)
        }
    }

    fun viewBlocks(list: List<Block>) {
        viewJob2?.cancel()
        viewJob2 = viewModelScope.launch {
            DataManager.viewBlocks(dao, list)
        }
    }
}