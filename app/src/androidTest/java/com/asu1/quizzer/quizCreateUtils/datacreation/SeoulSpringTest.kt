package com.asu1.quizzer.quizCreateUtils.datacreation

import com.asu1.quizzer.quizCreateUtils.TestQuiz1
import com.asu1.quizzer.quizCreateUtils.TestQuiz2
import com.asu1.quizzer.quizCreateUtils.TestQuiz3
import com.asu1.quizzer.quizCreateUtils.TestQuiz4
import com.asu1.quizzer.quizCreateUtils.allInOneForTest
import com.asu1.quizzer.test.R
import java.time.LocalDate
import java.time.YearMonth

val seoulSpringQuiz = listOf(
    TestQuiz2(
        centerDate = YearMonth.of(1979, 12),
        answerDate = mutableSetOf(LocalDate.of(1979, 12, 12)),
        answers = mutableListOf(),
        question = "서울의 봄은 과거 어떤 군사반란을 기반으로 소재로 한 영화입니다. 이 군사반란의 날짜는?"
    ),
    TestQuiz1(
        answers = mutableListOf("전두광", "서재필", "이태신", "김좌진", "김준엽", "노태건"),
        ans = mutableListOf(false, true, false, true, false, false),
        question = "서울의 봄의 주요 등장인물이 아닌 사람들은?"
    ),
    TestQuiz4(
        connectionAnswers = mutableListOf("장태완", "김진기", "정승화", "전두환", "노태우"),
        connectionAnswerIndex = mutableListOf(3, 0, 2, 4, 1),
        answers = mutableListOf("황정민", "정우성", "이성민", "박해준", "김성균"),
        question = "배우 이름과 원 역사에서 누구였는지 연결하세요."
    ),
    TestQuiz3(
        answers = mutableListOf(
            "10.26 사건",
            "이태신의 수도경비사령관 임명",
            "작전명 '생일 잔치' 개시",
            "총장 공관에서 총격전 발생",
            "수뇌부 육본에서 도주",
            "하나회의 대통령 재가 수령"
        ),
        question = "발생했던 일들을 순서대로 나열하세요."
    ),
    TestQuiz1(
        answers = mutableListOf(
            "실패하면 반역, 성공하면 혁명 아입니까!",
            "그래 사살 임마 사살!",
            "어이 도희철이. 2공수가 와야겠다.",
            "정 총장 연행할 때부터 전쟁 시작된 거 아닙니까?",
            "니가 가기 싫으모, 내 심장다가 팍 쏴 버리라. 쏘라고!"
        ),
        ans = mutableListOf(false, true, false, false, false),
        question = "전두광이 반란 진행 중 했던 말이 아닌 것은?"
    ),
    TestQuiz1(
        answers = mutableListOf(
            "대통령의 암살로 인해 설립된 수사기구이다.",
            "검찰, 경찰, 헌병 등 권력기관들을 모두 장악하였다.",
            "인권을 신경쓰지 않고 의심되는 사람을 납치해 고문하였다.",
            "이 권력을 통해 사람들을 모아 하나회를 만들었다.",
            "본부장 자리를 바탕으로 수방사령관에 자기 사람을 않히려고 하였다."
        ),
        ans = mutableListOf(false, false, false, true, false),
        question = "전두광이 맡은 합동수사본부장에 대한 설명으로 틀린 것은?"
    ),
    TestQuiz4(
        connectionAnswers = mutableListOf("정상호", "전두광", "노태건", "이태신"),
        connectionAnswerIndex = mutableListOf(1, 3, 0, 2),
        answers = mutableListOf(
            "바뀐 거 하나도 없습니다. 세상은 그대로야.",
            "대한민국 육군은 다 같은 편입니다.",
            "내가 정치를 맡길 거면 이 장군 당신한테 왜 맡기겠소!",
            "내는 겁 안 뭇데이. 니 알제?",
        ),
        question = "인물과 한 대사를 연결하세요."
    ),
    TestQuiz1(
        answers = mutableListOf(
            "보안사령관",
            "수도군단장",
            "육군참모차장",
            "제8공수특전여단장",
            "육군특수전사령관",
            "제2공수특전여단장"
        ),
        ans = mutableListOf(true, true, false, false, false, true),
        question = "전두광의 반란에 가담한 직책들은?"
    ),
    TestQuiz2(
        centerDate = YearMonth.of(2023, 11),
        answerDate = mutableSetOf(LocalDate.of(2023, 11, 22)),
        answers = mutableListOf(),
        question = "서울의 봄이 한국에서 개봉한 날은?"
    ),
    TestQuiz3(
        answers = mutableListOf("명량", "국제시장", "베테랑", "서울의 봄", "괴물"),
        question = "다음 영화들을 한국에서 흥행 순서대로 나열하면?"
    )
)
val springTest = allInOneForTest(
    title = "영화 '서울의 봄'에 대한 퀴즈",
    description = "역사적 사건에 기반한 영화 '서울의 봄'에 대한 퀴즈입니다. 영화를 떠올리면서 풀어보세요.",
    tags = setOf("서울의 봄", "영화", "전두광", "군사반란"),
    titleImage = R.drawable.seoul,
    colorInt = 3,
    quizzes = seoulSpringQuiz,
    bodyImages = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    bodyYoutubeLinks = listOf("", "", "", "", "", "", "", "", "", ""),
    primaryColor = "ff156683",
    questionTextStyle = listOf(0, 0, 0),
    bodyTextStyle = listOf(3, 0, 0),
    answerTextStyle = listOf(1, 6, 0),
    backgroundColorFilter = "ff894e24",
    backgroundImageIndex = 0,
    effectColor = "80512400",
    effectIndex = 9,
    textColor = "ffffdbc7",
    overlayImage = R.drawable.soldier_back,
)
