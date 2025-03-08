package com.asu1.appdata.stringFilter

import androidx.room.*

@Fts4
@Entity(tableName = "admin_words")
data class AdminWord(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Int = 0,

    @ColumnInfo(name = "word")
    val word: String
)

@Fts4
@Entity(tableName = "inappropriate_words")
data class InappropriateWord(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Int = 0,

    @ColumnInfo(name = "word")
    val word: String
)
