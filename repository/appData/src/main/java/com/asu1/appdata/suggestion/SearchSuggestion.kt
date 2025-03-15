package com.asu1.appdata.suggestion

import androidx.room.*

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