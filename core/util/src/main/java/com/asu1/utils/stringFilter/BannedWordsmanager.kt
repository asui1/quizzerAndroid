package com.asu1.utils.stringFilter

import android.content.Context
import com.asu1.resources.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.regex.Pattern

fun containsKorean(text: String): Boolean {
    val koreanPattern = Pattern.compile("[가-힣]")
    return koreanPattern.matcher(text).find()
}

fun containsKorean(texts: List<String>): Boolean {
    val koreanPattern = Pattern.compile("[가-힣]")
    return texts.any { koreanPattern.matcher(it).find() }
}

class BannedWordsManager(private val context: Context) {
    private val filters: List<BannedWordsProvider> = listOf(
        AdminWordsEn(R.raw.admin_words_en),
        InappropriateWordsEn(R.raw.inappropriate_words_en),
        AdminWordsKo(R.raw.admin_words_ko),
        InappropriateWordsKo(R.raw.inappropriate_words_ko)
    )

    init {
        filters.forEach { it.initializeWords(context) }
    }

    suspend fun isBannedNickname(nickname: String): Boolean = coroutineScope {
        val isKorean = containsKorean(nickname)
        // Select relevant filters
        // Always include EN filters, and add KO filters only if necessary
        val relevantFilters = filters.filter {
            it is BannedWordsProviderEn || (isKorean && it is BannedWordsProviderKo)
        }

        // Run selected filters concurrently
        relevantFilters.map { filter ->
            async(Dispatchers.Default) { filter.isBannedWord(nickname) }
        }.firstOrNull { it.await() } != null
    }

    // Function to check if any text contains inappropriate words
    suspend fun checkInappropriateWords(texts: List<String>): Boolean? = coroutineScope {
        val isKorean = containsKorean(texts)

        // Select only inappropriate filters (_EN always runs, _KO runs only if Korean is detected)
        val relevantFilters = filters.filter {
            (it is InappropriateWordsEn) || (isKorean && it is InappropriateWordsKo)
        }

        // Run filters concurrently and return the first match
        relevantFilters.map { filter ->
            async(Dispatchers.Default) {
                texts.firstNotNullOfOrNull { text ->
                    filter.isBannedWord(text)
                }
            }
        }.firstOrNull { it.await() != null }?.await()
    }
}
