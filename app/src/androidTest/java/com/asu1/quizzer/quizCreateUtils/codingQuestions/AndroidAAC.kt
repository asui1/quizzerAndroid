package com.asu1.quizzer.quizCreateUtils.codingQuestions

import com.asu1.models.quiz.QuizData
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.quizzer.quizCreateUtils.QuizBundle
import com.asu1.quizzer.quizCreateUtils.quizTheme.androidQuizTheme
import com.asu1.quizzer.quizCreateUtils.quizTheme.androidScoreCard
import com.asu1.quizzer.test.R

val androidAacQuiz = listOf(
    MultipleChoiceQuiz(
        question = "What is the primary goal of Android Architecture Components (AAC)?",
        options = mutableListOf(
            "Optimize Android UI performance",
            "Standardize Android app architecture and make maintenance easier",
            "Reduce app size",
            "Replace Jetpack Compose",
            "Simplify multithreading in Android"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "What is the main purpose of the ViewModel class?",
        options = mutableListOf(
            "Store UI state and retain data across configuration changes",
            "Manage database trcorrectFlagsactions",
            "Optimize network requests",
            "Improve RecyclerView performance",
            "Handle UI animations"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "What is the main advantage of LiveData?",
        options = mutableListOf(
            "Provides data storage and encryption",
            "Built-in coroutines for automatic async operations",
            "Optimizes UI animations",
            "Lifecycle-aware data handling",
            "Reduces app size"
        ),
        correctFlags = mutableListOf(false, false, false, true, false)
    ),

    MultipleChoiceQuiz(
        question = "What is the role of DAO (Data Access Object) in Room Database?",
        options = mutableListOf(
            "Creates and manages the database",
            "Updates the UI",
            "Abstracts SQL queries to simplify data manipulation",
            "Performs asynchronous network requests",
            "Manages animation effects"
        ),
        correctFlags = mutableListOf(false, false, true, false, false)
    ),

    MultipleChoiceQuiz(
        question = "What is the primary function of the Lifecycle class?",
        options = mutableListOf(
            "Manages Fragment trcorrectFlagsitions",
            "Optimizes app execution time",
            "Tracks Activity and Fragment lifecycle and provides callbacks",
            "Increases data loading speed",
            "Sorts RecyclerView items"
        ),
        correctFlags = mutableListOf(false, false, true, false, false)
    ),

    MultipleChoiceQuiz(
        question = "What is the role of the Navigation Component?",
        options = mutableListOf(
            "Stores user navigation data",
            "Manages screen trcorrectFlagsitions and handles the back stack automatically",
            "Optimizes API requests",
            "Manages multithreading operations",
            "Optimizes list scrolling"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Why is the Paging library used?",
        options = mutableListOf(
            "To increase network speed",
            "To load all data at once and save storage",
            "To efficiently load large datasets and improve RecyclerView performance",
            "To optimize all UI animations",
            "To speed up Room database queries"
        ),
        correctFlags = mutableListOf(false, false, true, false, false)
    ),

    MultipleChoiceQuiz(
        question = "What functionality does WorkManager provide?",
        options = mutableListOf(
            "Battery optimization techniques",
            "Safe and efficient execution of background tasks",
            "Optimizes RecyclerView scrolling performance",
            "Dynamically changes the user interface",
            "Reduces app size"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "What is the main advantage of DataStore?",
        options = mutableListOf(
            "Optimizes background tasks",
            "Speeds up network requests",
            "Reduces app memory usage",
            "Improves UI performance",
            "Provides a safer and more efficient alternative to SharedPreferences"
        ),
        correctFlags = mutableListOf(false, false, false, false, true)
    ),

    MultipleChoiceQuiz(
        question = "Why use Hilt?",
        options = mutableListOf(
            "To enhance Jetpack Compose UI performance",
            "To speed up network requests",
            "To automatically sort RecyclerView items",
            "To simplify Dependency Injection and integrate with ViewModel",
            "To optimize Room Database performance"
        ),
        correctFlags = mutableListOf(false, false, false, true, false)
    )
)
val androidAacDataEn = QuizData(
    title       = "Android Quiz: AAC",
    description = "Test your understanding of AAC fundamentals, including ViewModel, LiveData, Room Database, and more.",
    tags        = setOf("Android", "AAC", "Coding Interview", "Technical Interview")
)
@Suppress("unused")
val androidAacQuizData = QuizBundle(
    data = androidAacDataEn,
    theme = androidQuizTheme,
    quizzes = androidAacQuiz,
    titleImage = R.drawable.android_coding_interview,
    scoreCard = androidScoreCard,
)

val androidAacQuizKo = listOf(
    MultipleChoiceQuiz(
        question = "AAC (Android Architecture Components)의 주요 목표는 무엇인가요?",
        options = mutableListOf(
            "안드로이드 UI 성능 최적화",
            "안드로이드 앱의 아키텍처를 표준화하고 유지보수를 쉽게 만들기 위함",
            "앱 크기를 줄이는 기능 제공",
            "Jetpack Compose를 대체하기 위한 라이브러리",
            "안드로이드에서 멀티쓰레딩을 단순화하기 위함"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "ViewModel 클래스의 주요 목적은 무엇인가요?",
        options = mutableListOf(
            "UI 상태를 저장하고 구성 변경에서 데이터를 유지하는 것",
            "데이터베이스 트랜잭션을 관리하는 것",
            "네트워크 요청을 최적화하는 것",
            "RecyclerView의 성능을 향상시키는 것",
            "UI 애니메이션을 관리하는 것"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "LiveData의 주요 장점은 무엇인가요?",
        options = mutableListOf(
            "데이터 저장 및 암호화 기능 제공",
            "코루틴을 내장하여 비동기 작업을 자동으로 수행",
            "UI 애니메이션을 최적화",
            "수명 주기 인식 (Lifecycle-Aware) 기능 제공",
            "앱 크기를 줄이는 기능 제공"
        ),
        correctFlags = mutableListOf(false, false, false, true, false)
    ),

    MultipleChoiceQuiz(
        question = "Room Database에서 DAO(Data Access Object)의 역할은 무엇인가요?",
        options = mutableListOf(
            "데이터베이스를 생성하고 관리",
            "UI를 업데이트하는 역할",
            "SQL 쿼리를 추상화하여 데이터 조작을 쉽게 만듦",
            "비동기 네트워크 요청을 수행",
            "애니메이션 효과를 관리"
        ),
        correctFlags = mutableListOf(false, false, true, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Lifecycle 클래스의 주요 기능은 무엇인가요?",
        options = mutableListOf(
            "Fragment 전환을 관리",
            "앱 실행 시간을 최적화",
            "Activity 및 Fragment의 수명 주기를 추적하고 콜백을 제공",
            "데이터 로딩 속도를 증가",
            "RecyclerView 아이템을 정렬"
        ),
        correctFlags = mutableListOf(false, false, true, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Navigation Component의 역할은 무엇인가요?",
        options = mutableListOf(
            "사용자의 내비게이션 데이터를 저장",
            "화면 간 이동을 관리하고 백 스택을 자동 처리",
            "API 요청을 최적화",
            "멀티스레드 작업을 관리",
            "리스트 스크롤을 최적화"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Paging 라이브러리를 사용하는 주요 이유는 무엇인가요?",
        options = mutableListOf(
            "네트워크 속도를 증가시키기 위해",
            "모든 데이터를 한 번에 불러와 저장 공간을 절약하기 위해",
            "대량의 데이터를 효율적으로 로드하고 RecyclerView에서 성능을 향상시키기 위해",
            "모든 UI 애니메이션을 최적화하기 위해",
            "Room 데이터베이스 쿼리 속도를 향상시키기 위해"
        ),
        correctFlags = mutableListOf(false, false, true, false, false)
    ),

    MultipleChoiceQuiz(
        question = "WorkManager는 어떤 기능을 제공하나요?",
        options = mutableListOf(
            "배터리 사용량을 줄이는 최적화 기술",
            "백그라운드 작업을 안전하고 효율적으로 실행하는 기능",
            "RecyclerView 스크롤 성능을 최적화하는 기능",
            "사용자 인터페이스를 동적으로 변경하는 기능",
            "앱 크기를 줄이는 기능"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "DataStore의 주요 장점은 무엇인가요?",
        options = mutableListOf(
            "백그라운드 작업을 최적화",
            "네트워크 요청을 빠르게 처리",
            "앱의 메모리 사용량을 감소",
            "UI 성능을 향상",
            "SharedPreferences보다 더 안전하고 효율적인 데이터 저장을 제공",
        ),
        correctFlags = mutableListOf(false, false, false, false, true)
    ),

    MultipleChoiceQuiz(
        question = "Hilt를 사용하는 주요 이유는 무엇인가요?",
        options = mutableListOf(
            "Jetpack Compose의 UI 성능을 향상시키기 위해",
            "네트워크 요청을 빠르게 하기 위해",
            "RecyclerView 아이템을 자동으로 정렬하기 위해",
            "Dependency Injection을 간단하게 구현하고 ViewModel 등과 통합하기 위해",
            "Room Database의 성능을 최적화하기 위해"
        ),
        correctFlags = mutableListOf(false, false, false, true, false)
    )
)

val androidAacDataKo = QuizData(
    title = "안드로이드 퀴즈: AAC",
    description = "안드로이드 AAC의 핵심 개념, ViewModel, LiveData, Room Database 등에 대한 이해도를 테스트하세요.",
    tags = setOf("안드로이드", "AAC", "코딩 인터뷰", "기술 면접")
)

@Suppress("unused")
val androidAacQuizBundleKo = QuizBundle(
    data = androidAacDataKo,
    theme = androidQuizTheme,
    quizzes = androidAacQuizKo,
    titleImage = R.drawable.android_coding_interview,
    scoreCard = androidScoreCard,
)