package com.asu1.utils

fun normalizeKoreanSuffixPrecisely(text: String): String {
    val suffixes = listOf("들", "마다", "씩", "모두", "각각", "조차", "마저")
    return text.split(Regex("\\s+")).joinToString(" ") { word ->
        var processedWord = word
        suffixes.forEach { suffix ->
            if (processedWord.endsWith(suffix) && processedWord.length > suffix.length) {
                processedWord = processedWord.removeSuffix(suffix)
            }
        }
        processedWord
    }
}

fun normalizeMixedInputPrecisely(text: String): String {
    return normalizeKoreanSuffixPrecisely(
        text.lowercase()
            .replace(Regex("[\\p{Punct}]"), "") // 특수문자 제거 (공백은 유지)
            .replace(Regex("[ㄱ-ㅎㅏ-ㅣ]"), "") // 고립 자모 제거
            .trim()
    ).replace(Regex("\\s+"), "") // 최종 공백 제거
}
