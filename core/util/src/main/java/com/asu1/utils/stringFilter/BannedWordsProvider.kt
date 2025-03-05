package com.asu1.utils.stringFilter

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

// English Admin Words
class AdminWordsEn(override val jsonFileId: Int) : BannedWordsProviderEn {
    override var bannedWords: PersistentList<String> = persistentListOf()
}

// English Inappropriate Words
class InappropriateWordsEn(override val jsonFileId: Int) : BannedWordsProviderEn {
    override var bannedWords: PersistentList<String> = persistentListOf()
}

// Korean Admin Words
class AdminWordsKo(override val jsonFileId: Int) : BannedWordsProviderKo {
    override var bannedWords: PersistentList<String> = persistentListOf()
}

// Korean Inappropriate Words
class InappropriateWordsKo(override val jsonFileId: Int) : BannedWordsProviderKo {
    override var bannedWords: PersistentList<String> = persistentListOf()
}