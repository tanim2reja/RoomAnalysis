package com.jolpai.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="word_table")
data class Word (@PrimaryKey
                 @ColumnInfo(name="id")
                 val id: Int,

                 @ColumnInfo(name="word")
                 val word: String,

                 @ColumnInfo(name="meaning")
                 val meaning: String)