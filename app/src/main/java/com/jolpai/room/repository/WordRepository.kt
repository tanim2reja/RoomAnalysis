package com.jolpai.room.repository

import androidx.lifecycle.LiveData
import com.jolpai.room.dao.WordDao
import com.jolpai.room.entity.Word

class WordRepository (private val wordDao : WordDao) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allWords: LiveData<List<Word>> = wordDao.getAscendingWords()

    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}