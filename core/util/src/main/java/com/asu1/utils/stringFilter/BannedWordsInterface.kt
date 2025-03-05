package com.asu1.utils.stringFilter

import android.content.Context
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

interface BannedWordsProvider {
    val jsonFileId: Int // Resource ID for JSON file
    var bannedWords: PersistentList<String>

    // Load words from a JSON file
    fun initializeWords(context: Context) {
        val inputStream = context.resources.openRawResource(jsonFileId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val jsonText = reader.readText()
        reader.close()

        val jsonArray = JSONArray(jsonText)
        bannedWords = (0 until jsonArray.length()).map { jsonArray.getString(it) }.toPersistentList()
    }

    // Check if the given word is in the banned list
    fun isBannedWord(word: String): Boolean {
        return bannedWords.any { word.lowercase().contains(it.lowercase()) }
    }
}

interface BannedWordsProviderEn : BannedWordsProvider

interface BannedWordsProviderKo : BannedWordsProvider
