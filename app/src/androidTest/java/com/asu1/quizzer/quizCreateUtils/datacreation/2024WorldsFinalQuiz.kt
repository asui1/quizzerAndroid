package com.asu1.quizzer.quizCreateUtils.datacreation

import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.models.quiz.QuizData
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.quizzer.quizCreateUtils.QuizBundle
import com.asu1.quizzer.quizCreateUtils.quizTheme.hexColor
import com.asu1.quizzer.test.R
import java.time.LocalDate

val worlds24T1Ko = listOf(
    MultipleChoiceQuiz(
        options = mutableListOf(
            "15승 3패",
            "14승 4패",
            "13승 5패",
            "13승 4패",
            "12승 5패",
        ),
        correctFlags = mutableListOf(false, false, false, true, false),
        question = "T1의 2024 월드 챔피언쉽 세트 승패는?"
    ),

    ReorderQuiz(
        answers = mutableListOf(
            "최후의 전투",
            "페이커의 결단력",
            "신의 이니시에이팅",
            "여전히 죽지 않는 페이커의 사일러스",
            "엘크의 협곡을 가르는 애쉬 궁 저격"
        ),
        question = "2024 월즈에서 선정된 Top5 명장면을 순서대로 나열하세요."
    ),

    MultipleChoiceQuiz(
        options = mutableListOf(
            "4대1 상황에서 제우스를 구하기 위해 갈리오 궁을 사용함",
            "오너의 뽀삐가 아리의 텔을 끊음",
            "페이커가 엘크를 마무리함",
            "제우스가 점멸을 통해 환상적인 핑퐁을 함",
            "페이커가 마지막까지 버텨내며 트리플 킬을 달성함"
        ),
        correctFlags = mutableListOf(true, false, true, false, false),
        question = "5세트 마지막 장면에 대한 설명으로 옳은 것은?"
    ),

    ReorderQuiz(
        answers = mutableListOf(
            "구마유시와 케리아가 죽음",
            "빈이 퇴로를 끊는 텔레포트를 사용함",
            "페이커가 점멸 도발로 나이트를 뭄",
            "제우스의 점멸 배치기로 나이트를 처치함",
            "오너가 슌을 처치함",
            "페이커의 박치기로 엘크를 처치함",
        ),
        question = "5세트 구마유시와 케리아가 죽은 뒤 T1이 환상적인 3:4한타로 대승을 했는데 이 때 일을 순서대로 나열하세요."
    ),

    ReorderQuiz(
        answers = mutableListOf(
            "BLG가 미드를 철거하고 후퇴함",
            "페이커가 라칸을 돌파함",
            "페이커가 세주아니 궁을 피함",
            "케리아의 점멸 궁이 상대를 덮음",
            "오너의 점멸 궁으로 호응함",
            "엘크가 사망함",
            "나이트가 사망함"
        ),
        question = "4세트 페이커가 미드에서 이니시를 걸었을 때 일을 순서대로 나열하세요."
    ),

    ConnectItemsQuiz(
        answers = mutableListOf(
            "엘크를 솔로킬 함",
            "상대를 돌파하여 적 원딜 미드를 잡아냄",
            "도망가는 상대를 끝까지 따라가 스노우볼을 극대화함",
            "빈을 솔로킬 함"
        ),
        connectionAnswerIndex = mutableListOf(1, 3, 0, 2),
        connectionAnswers = mutableListOf("세주아니 궁", "직스 궁", "나르 궁", "라칸 궁"),
        question = "4세트 페이커의 슈퍼플레이와 그 때 사용한 궁을 연결하세요."
    ),

    MultipleChoiceQuiz(
        options = mutableListOf(
            "제우스가 슌을 2번 연달아 에어본 시키며 킬을 땄다.",
            "오너가 파고들어 엘크를 처치했다.",
            "페이커가 럼블 궁을 훔처 상대 진영을 갈랐다.",
            "구마유시가 럼블 궁과 갈리오 궁을 흡수하며 상대를 빨아들였다.",
            "케리아의 궁이 3인을 맞추며 한타를 결정지었다."
        ),
        correctFlags = mutableListOf(true, true, false, true, false),
        question = "2세트 마지막 한타에 대한 설명으로 옳은 것들은?"
    ),

    ReorderQuiz(
        answers = mutableListOf("엘크와 나이트를 기절시킴", "엘크와 나이트를 에어본시킴", "라칸의 W를 끊음", "갈리오 궁을 끊음", "갈리오를 잡음"),
        question = "2세트는 8분대에 예술적인 탑 다이브가 펼쳐졌는데 플레이를 순서대로 나열하세요."
    ),

    MultipleChoiceQuiz(
        options = mutableListOf("제우스가 귀환하려던 온와 엘크를 붙잡았다.", "오너의 세주아니 궁이 깔끔하게 들어갔다.",
            "제우스의 나르가 점멸 궁으로 둘을 기절시켰다.", "페이커가 텔로 넘어와 적을 처치했다.", "구마유시의 케틀 궁이 도망가던 적을 마무리했다."),
        correctFlags = mutableListOf(false, true, false, false, true),
        question = "1세트는 12분 즈음 제우스의 슈퍼플레이로 역전의 발판을 마련할 뻔 했었는데, 이 상황에 대한 것들 중 틀린 것들은?"
    ),

    DateSelectionQuiz(
        centerDate = LocalDate.of(2024, 11, 1),
        answerDate = mutableSetOf(LocalDate.of(2024, 11, 2)),
        question = "결승전 경기날은 언제인가요?"
    ),
)

val t1WorldsQuizDataKo = QuizData(
    title       = "2024 LOL 월챔 결승전 퀴즈",
    description = "T1이 5번째 우승을 차지하며 역사를 썼던 2024년 월드 챔피언쉽 결승전에 대한 퀴즈입니다.",
    tags        = setOf("롤", "2024 월즈", "T1", "월드 챔피언쉽", "리그 오브 레전드")
    // `image` remains the default empty bitmap; inject via test‐builder if needed
)

// 3) build your QuizTheme
val t1WorldsQuizThemeKo = QuizTheme(
    backgroundImage = ImageColor(
        color         = hexColor("FFe4002b"),  // primaryColor
        color2        = hexColor("ff4f99b3"),  // backgroundColorFilter
        colorGradient = hexColor("ffe4002b"),  // effectColor
        state         = ImageColorState.COLOR
    ),
    questionTextStyle = listOf(0, 0, 0),
    bodyTextStyle     = listOf(3, 0, 0),
    answerTextStyle   = listOf(1, 6, 0),
    colorScheme = com.asu1.resources.LightColorScheme.copy(
        primary            = hexColor("FFe4002b"),
        onPrimary          = hexColor("ffffd700"),
        background         = hexColor("ff4f99b3"),
        secondaryContainer = hexColor("ffe4002b")
    )
)

@Suppress("unused")
val t1WorldsQuizBundleKo = QuizBundle(
    data       = t1WorldsQuizDataKo,
    theme      = t1WorldsQuizThemeKo,
    quizzes    = worlds24T1Ko,
    titleImage = R.drawable.t1_final
)