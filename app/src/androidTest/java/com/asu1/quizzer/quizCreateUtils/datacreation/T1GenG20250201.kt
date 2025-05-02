package com.asu1.quizzer.quizCreateUtils.datacreation

import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.models.quiz.QuizData
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.quizzer.quizCreateUtils.QuizBundle
import com.asu1.quizzer.quizCreateUtils.quizTheme.hexColor
import com.asu1.quizzer.test.R

val t1GenQuiz20250201 = listOf(
    MultipleChoiceQuiz(
        options = mutableListOf(
            "T1, Gen.G 둘 다 3승 1패의 상황이었다.",
            "T1은 득실차 +5로 바론 그룹 1등이었다.",
            "Gen.G는 +4로 엘더 그룹 2등이었다.",
            "오너와 캐니언의 상대전적은 28:32로 캐니언의 약 우세였다.",
            "Gen.G가 진영선택권을 가지고 시작한 게임이었다."
        ),
        correctFlags = mutableListOf(true, false, true, false, false),
        question = "경기 시작 전 상황으로 옳은 것들을 고르세요."
    ),
    ReorderQuiz(
        answers = mutableListOf(
            "크산테",
            "바루스",
            "요네",
            "엘리스",
            "마오카이"
        ),
        question = "1세트 젠지의 픽들을 픽순대로 나열하세요."
    ),
    ConnectItemsQuiz(
        answers = mutableListOf(
            "2대1 다이브 압박으로 인해 cs를 디나이 당했다.",
            "적 진영의 레드를 카정하였다.",
            "다이브를 당해 퍼스트 블러드를 내주었다.",
            "환상적인 궁 심리전으로 킬과 상대 스펠을 뺏어내었다.",
        ),
        connectionAnswers = mutableListOf(
            "도란",
            "룰러",
            "오너",
            "기인",
        ),
        // 정답은 원래 순서와 동일하지만 문제 출제를 위해 그대로 사용한 예시
        connectionAnswerIndex = mutableListOf(0, 2, 3, 1),
        question = "1세트 초반 상황과 관련 깊은 선수를 연결하세요."
    ),
    ReorderQuiz(
        answers = mutableListOf(
            "바이 궁을 통해 쵸비를 먼저 끊어내었다.",
            "뽀삐 궁을 통해 상대 크산테를 날려 4대3 상황을 만들었다.",
            "벽궁을 통해 캐니언을 먼저 포커싱하였다.",
            "룰러를 제이스 Q와 E로 압박하였다.",
            "궁으로 날아온 카이사와 함께 적을 섬멸하였다.",
        ),
        question = "1세트는 아타칸 앞에서 T1의 한타로 기울어졌는데 일어난 일들을 순서대로 나열하세요."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf(
            "오너의 미드 갱에 캐니언이 역갱을 시도하였다.",
            "처음 2대2에서는 미드라이너끼리 교환되었다.",
            "캐니언이 발을 붙잡던 오너를 처치하였다.",
            "도란까지 내려와 겨우 2대2 킬교환을 성공시켰다.",
            "양 서포터가 같이 미드에 궁을 사용한 뒤 교전이 종료되었다."
        ),
        correctFlags = mutableListOf(true, false, false, true, false),
        question = "다음 중 2세트 13분경 미드 교전에 대해 틀린 것은?"
    ),
    ConnectItemsQuiz(
        answers = mutableListOf(
            "기인",
            "캐니언",
            "쵸비",
            "룰러",
            "듀로"
        ),
        connectionAnswers = mutableListOf(
            "1/2/8",
            "5/1/6",
            "1/2/10",
            "3/3/11",
            "6/2/2"
        ),
        connectionAnswerIndex = mutableListOf(1, 0, 3, 4, 2),
        question = "2세트 젠지의 KDA를 선수와 연결하세요."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf(
            "애쉬",
            "징크스",
            "스카너",
            "렐",
            "니달리"
        ),
        correctFlags = mutableListOf(false, true, true, false, false),
        question = "다음 중 T1이 3세트에서 밴한 챔피언이 아닌 것들은?"
    ),
    ReorderQuiz(
        answers = mutableListOf(
            "룰루가 바위게를 먹던 리신을 잡아 퍼스트 블러드를 얻어냈다.",
            "라인 스왑 중 그랩-덫 콤보로 럼블을 잡아냈다.",
            "탑 4인 다이브를 통해 우디르를 잡아냈다.",
            "바텀 2대2에서 제리가 징크스를 잡아냈다.",
            "리신의 점멸 궁으로 제리를 잡아냈다.",
            "미드 포탑 앞에서 하나씩 끊어먹으며 대량득점을 했다."
        ),
        question = "3세트는 초반부터 많은 킬이 나왔는데 그 킬들을 순서대로 나열하세요."
    ),
    ReorderQuiz(
        answers = mutableListOf(
            "오너가 먼저 진입해 룰러의 플래시를 뽑았다.",
            "스매쉬가 벽을 넘어 룰러를 잡아냈다.",
            "오너와 스매쉬가 쵸비를 잡아냈다.",
            "도란이 캐니언을 잡아냈다.",
            "한타 승리 후 바론을 차지했다."
        ),
        question = "3세트는 치열했던 게임이 바론 앞 한타로 확 기울었는데 이 한타를 순서대로 나열하세요."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf(
            "스매쉬가 노데스 13킬을 기록하며 MVP를 차지했다.",
            "T1이 사이온을 이용한 초반 바텀 다이브로 2세트 초반 주도권을 잡았다.",
            "스매쉬의 제리가 룰러의 징크스 궁을 피하며 바텀 솔로킬을 달성했다.",
            "세 경기 중 가장 높은 딜은 기록한 챔피언은 기인의 오로라였다.",
            "세 경기 모두 유충을 한 팀이 6마리 전부 먹으며 초반 스노우볼을 굴렸다."
        ),
        correctFlags = mutableListOf(true, false, true, true, false),
        question = "다음 중 이 경기에서 있었던 기록들은?"
    ),
)

val t1GengQuizData = QuizData(
    title       = "2025/02/01 T1vsGen.G LCK 컵 그룹 스테이지",
    description = "2025/02/01 T1 vs Gen.G의 LCK 컵 그룹 스테이지 경기에 대한 퀴즈입니다.",
    tags        = setOf("T1", "Gen.G", "LCK 컵", "2025/02/01", "그룹 스테이지", "롤")
    // image remains createEmptyBitmap(); inject via your test‐builder if needed
)

// 3) Build your QuizTheme using the hex values from the old DTO
val t1GengQuizTheme = QuizTheme(
    backgroundImage = ImageColor(
        color         = hexColor("FF2D1D07"),  // primaryColor
        color2        = hexColor("FFffddb6"),  // backgroundColorFilter
        colorGradient = hexColor("ff93000a"),  // effectColor
        state         = ImageColorState.COLOR
    ),
    questionTextStyle = listOf(0, 0, 0),
    bodyTextStyle     = listOf(3, 0, 0),
    answerTextStyle   = listOf(1, 6, 0),
    colorScheme = com.asu1.resources.LightColorScheme.copy(
        primary            = hexColor("FF2D1D07"),
        onPrimary          = hexColor("ffba1a1a"),
        background         = hexColor("FFffddb6"),
        secondaryContainer = hexColor("ff93000a")
    )
)

// 4) Wrap it all up in a QuizBundle
@Suppress("unused")
val t1GengQuizBundle20250201 = QuizBundle(
    data       = t1GengQuizData,
    theme      = t1GengQuizTheme,
    quizzes    = t1GenQuiz20250201,
    titleImage = R.drawable.t1_geng
)