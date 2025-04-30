package com.asu1.appdata.stringFilter

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StringFilterRepository @Inject constructor(
    private val adminWordDao: AdminWordDao,
    private val inappropriateWordDao: InappropriateWordDao,
) {
    // ✅ In-memory cache for faster lookups
    private var cachedInappropriateWords: List<String>? = null
    private var cachedAdminWords: List<String>? = null

    // ✅ Load words into cache only when needed
    private suspend fun loadInappropriateWords(): List<String> {
        return cachedInappropriateWords ?: inappropriateWordDao.getAllWords().also {
            cachedInappropriateWords = it
        }
    }

    private suspend fun loadAdminWords(): List<String> {
        return cachedAdminWords ?: adminWordDao.getAllWords().also {
            cachedAdminWords = it
        }
    }

    suspend fun containsInappropriateWord(text: String): Boolean {
        val inappropriateWordsToCheck = loadInappropriateWords()
        val textVariants = generateInAppropriateWordVariants(text)

        return textVariants.any { variant ->
            inappropriateWordsToCheck.any { word -> variant.contains(word, ignoreCase = true) }
        }
    }

    suspend fun containsAdminWord(text: String): Boolean {
        val adminWordsToCheck = loadAdminWords()
        return adminWordsToCheck.any { word -> text.contains(word, ignoreCase = true) }
    }

//    suspend fun refreshCache() {
//        cachedInappropriateWords = inappropriateWordDao.getAllWords()
//        cachedAdminWords = adminWordDao.getAllWords()
//    }
}
