package com.jolpai.room.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jolpai.room.database.WordDatabase
import com.jolpai.room.entity.Word
import com.jolpai.room.repository.WordRepository
import kotlinx.coroutines.launch

class WordViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: WordRepository

    val allWord : LiveData<List<Word>>

    init {
        val wordsDao = WordDatabase.getDatabase(application).wordDao()
        repository = WordRepository(wordsDao)
        allWord = repository.allWords



    }

    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }

}