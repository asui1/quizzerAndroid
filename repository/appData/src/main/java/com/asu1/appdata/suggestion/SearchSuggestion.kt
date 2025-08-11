package com.asu1.appdata.suggestion

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Fts4
@Entity(tableName = "search_suggestions")
data class SearchSuggestion(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Int = 0,
    @ColumnInfo(name = "query")
    val query: String,
    @ColumnInfo(name = "priority")
    val priority: Int = 0,
    @ColumnInfo(name = "lang")
    val lang: String
)
