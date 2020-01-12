package com.jolpai.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.jolpai.room.entity.Word


@Dao
interface WordDao {

    @Query("select * from word_table order by word asc")
    fun getAscendingWords() : LiveData<List<Word>>

    @Query("select * from word_table order by word desc")
    fun getDescendingWords() : List<Word>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word:Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()

    @RawQuery
    fun getWords(query: SupportSQLiteQuery?): List<Word>?
}