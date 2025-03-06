package com.asu1.appdata.suggestion

import androidx.room.*

@Fts4 // Enables Full-Text Search
@Entity(tableName = "search_suggestions")
data class SearchSuggestion(
    @PrimaryKey
    @ColumnInfo(name = "rowid") // 🔹 Maps `rowid` as an implicit primary key
    val id: Int = 0,
    @ColumnInfo(name = "query") val query: String,
    @ColumnInfo(name = "priority") val priority: Int = 0
)
