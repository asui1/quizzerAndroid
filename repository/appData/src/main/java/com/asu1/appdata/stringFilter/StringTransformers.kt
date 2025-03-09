package com.asu1.appdata.stringFilter

fun generateInAppropriateWordVariants(input: String): List<String>{
    // Inappropriate words는 3가지를 체크해야함.
    // 1. 원본,
    // 2. 특수문자를 변형한 것,
    // 3. 특수문자를 제거한 것..

    val noSpaces = input.replace(" ", "")

    val normalized = noSpaces.map { normalizationMap[it] ?: it }.joinToString("")
    val onlyLetters = noSpaces.filter { isKoreanLetter(it) || it == '\n' }

    return listOf(noSpaces, normalized, onlyLetters)
}

fun isKoreanLetter(c: Char): Boolean {
    return c.isLetter() || c in '\u3131'..'\u3163' // Includes Jamo (ㄱ-ㅣ)
}

val normalizationMap = mapOf(
    //English normalization
    '@' to 'a', '1' to 'I', '!' to 'I', '0' to 'O', '$' to 'S',
    '3' to 'E', '5' to 'S', '7' to 'T',

    // Variants of "시"
    '씨' to '시', '쉬' to '시', '씌' to '시', '슈' to '시', '쓔' to '시', '쉽' to '시', '쓉' to '시',

    // Variants of "발"
    '바' to '발', '빠' to '발', '빡' to '발', '빨' to '발', '뻘' to '발', '파' to '발', '팔' to '발', '펄' to '발',

    // Variants of "개", "새"
    '게' to  '개', '세' to '새', '쉐' to '새', '쉑' to '새', '끼' to '기',

)
