package com.asu1.quizzer.datacreation

import com.asu1.quizzer.TestQuiz1
import com.asu1.quizzer.TestQuiz3
import com.asu1.quizzer.TestQuiz4
import com.asu1.quizzer.allInOneForTest
import com.asu1.quizzer.model.BodyType

val iuquizzes2 = listOf(
    TestQuiz1(
        answers = mutableListOf("새우깡", "건빵", "초코칩", "와플", "감자칩"),
        ans = mutableListOf(false, true, false, false, false),
        question = "아이유가 가장 좋아하는 과자는?"
    ),
    TestQuiz1(
        point = 6,
        answers = mutableListOf(
            "눈물에 젖는 날들도 있겠지",
            "고통에 무너진 날들도 있겠지",
            "바람에 흔들린 날들도 있겠지",
            "삶에게 지는 날들도 있겠지",
            "슬픔에 잠긴 날들도 있겠지"
        ),
        ans = mutableListOf(false, false, false, true, false),
        bodyType = BodyType.TEXT("그럼에도 여전히 가끔은\n___________________________\n또 다시 헤매일지라도 돌아오는 길을 알아"),
        question = "다음 빈칸에 들어갈 가사는?"
    ),
    TestQuiz1(
        answers = mutableListOf("Shopper", "홀씨", "관객이 될게", "정거장", "Shh.."),
        ans = mutableListOf(false, false, false, true, false),
        question = "아이유의 가장 최근 앨범의 수록곡이 아닌 노래는?"
    ),
    TestQuiz1(
        point = 4,
        answers = mutableListOf("IUtv", "이지금 [IU Official]", "아이유의 시간", "IU Vlog", "이지은의 일상"),
        ans = mutableListOf(false, true, false, false, false),
        question = "아이유의 유튜브 채널 이름은?"
    ),
    TestQuiz3(
        point = 6,
        answers = mutableListOf(
            "Lost and Found",
            "Real",
            "Last Fantasy",
            "Modern Times",
            "꽃갈피",
            "Chat-shire"
        ),
        question = "아이유의 앨범들을 발매 순서대로 나열하세요."
    ),
    TestQuiz1(
        point = 5,
        answers = mutableListOf("GOD", "에이브릴 라빈", "태양", "머라이어 캐리", "코린 베일리 레이"),
        ans = mutableListOf(false, false, false, false, true),
        question = "아이유의 롤모델은?"
    ),
    TestQuiz4(
        point = 6,
        connectionAnswers = mutableListOf("2010년", "2015년", "2011년", "2017년", "2013년"),
        connectionAnswerIndex = mutableListOf(1, 0, 4, 2, 2),
        answers = mutableListOf("스물셋", "좋은 날", "분홍신", "팔레트", "너랑 나"),
        question = "다음 노래들을 발매된 연도와 연결하세요."
    ),
    TestQuiz1(
        point = 5,
        answers = mutableListOf("아이와 나의 바다", "밤편지", "에필로그", "이름에게", "무릎", "드라마"),
        ans = mutableListOf(true, false, true, false, false, true),
        question = "아이유가 2021년에 발표한 곡을 모두 고르세요"
    ),
    TestQuiz1(
        point = 4,
        answers = mutableListOf(
            "아이유 참 좋다",
            "네 마음 참 좋다",
            "네 이름 부른다",
            "네 모습 참 좋다",
            "네 모습 그립다"
        ),
        ans = mutableListOf(false, false, false, true, false),
        bodyType = BodyType.TEXT( "손틈새로 비치는\n____________\n손끝으로 돌리며 시곗바늘아 달려봐"),
        question = "다음 빈칸에 들어갈 내용은?"
    ),
    TestQuiz1(
        point = 4,
        answers = mutableListOf(
            "Chat-Shire",
            "The Golden Hour",
            "Love, Poem",
            "dlwlrma",
            "24 Steps: One, Two, Three, Four"
        ),
        ans = mutableListOf(false, true, false, false, false),
        question = "아이유의 2022년 콘서트 투어 이름은?"
    ),
)

val iutestdata2 = allInOneForTest(
    title = "아이유에 대해 얼마나 알고 있나요?",
    description = "아이유의 팬 그리고 음악 활동에 대한 문제들입니다. 얼마나 알고 있나요?",
    tags = setOf("아이유", "유애나", "가수", "앨범", "팬"),
    titleImage = com.asu1.quizzer.test.R.drawable.iu_au2,
    colorInt = 3,
    quizzes = iuquizzes2,
    bodyImages = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    bodyYoutubeLinks = listOf("", "", "", "", "", "", "", "", "", ""),
    primaryColor = "ff69548d",
    backgroundColorFilter = "ffebdcff",
    effectColor = "ff240e45",
    textColor = "ffd4bbfc",
)