package com.asu1.quizzer.quizCreateUtils.codingQuestions

import com.asu1.models.quiz.QuizData
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.quizzer.quizCreateUtils.QuizBundle
import com.asu1.quizzer.quizCreateUtils.quizTheme.androidQuizTheme
import com.asu1.quizzer.quizCreateUtils.quizTheme.codingScoreCard
import com.asu1.quizzer.test.R

val pythonDataStructureQuiz = listOf(
    MultipleChoiceQuiz(
        question = "What is the main difference between a Python list and a tuple?",
        options = mutableListOf(
            "Lists are mutable, but tuples are immutable",
            "Tuples cannot be indexed, but lists can",
            "Lists cannot be sorted, but tuples can",
            "Tuples can change size dynamically, but lists cannot",
            "Tuples are used to store key-value pairs"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Which of the following is a correct characteristic of the Python set data structure?",
        options = mutableListOf(
            "Maintains the order of elements",
            "Allows indexing",
            "Is immutable",
            "Does not allow duplicate elements",
            "Always remains sorted"
        ),
        correctFlags = mutableListOf(false, false, false, true, false)
    ),

    MultipleChoiceQuiz(
        question = "What is a key characteristic of Python dictionaries?",
        options = mutableListOf(
            "Always stores data in a sorted order",
            "Uses a hash table to store key-value pairs",
            "Keys can be duplicated, but values cannot",
            "Both keys and values are immutable",
            "Elements can be added in the same way as lists"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "What is a key characteristic of the heap data structure implemented using Python's heapq module?",
        options = mutableListOf(
            "All operations have O(1) time complexity",
            "Elements can be inserted in the same way as an array",
            "Values are not automatically sorted",
            "Follows the same operations as a list",
            "Can implement both min-heap and max-heap"
        ),
        correctFlags = mutableListOf(false, false, false, false, true)
    ),

    MultipleChoiceQuiz(
        question = "What is the Python deque (double-ended queue) and its key characteristic?",
        options = mutableListOf(
            "A double-ended queue allowing fast insertions and deletions from both ends",
            "Operates exactly like a stack",
            "Always maintains a sorted order",
            "Provides faster searching than arrays",
            "Once added, elements cannot be modified"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    )
)

val pythonDataStructureQuizData = QuizData(
    title = "Data Structures Quiz for Coding Tests",
    description = "Test your knowledge of Python's commonly used data structures and their characteristics.",
    tags = setOf("Data Structures", "Coding Test", "Python", "Technical Interview")
)

@Suppress("unused")
val pythonDataStructureQuizBundle = QuizBundle(
    data = pythonDataStructureQuizData,
    theme = androidQuizTheme,
    quizzes = pythonDataStructureQuiz,
    titleImage = R.drawable.android_coding_interview,
    scoreCard = codingScoreCard,
)


val pythonDataStructureQuizKo = listOf(
    MultipleChoiceQuiz(
        question = "Python의 리스트(list)와 튜플(tuple)의 주요 차이점은 무엇인가요?",
        options = mutableListOf(
            "리스트는 변경 가능(mutable)하지만 튜플은 변경 불가능(immutable)하다",
            "튜플은 인덱싱이 불가능하지만 리스트는 가능하다",
            "리스트는 정렬될 수 없지만 튜플은 정렬 가능하다",
            "튜플은 동적으로 크기를 변경할 수 있지만 리스트는 불가능하다",
            "튜플은 키-값 쌍을 저장하는 데 사용된다"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Python의 set(집합) 자료형의 특징으로 올바른 것은 무엇인가요?",
        options = mutableListOf(
            "요소의 순서를 유지한다",
            "인덱싱이 가능하다",
            "변경이 불가능하다",
            "중복된 요소를 허용하지 않는다",
            "항상 정렬된 상태로 유지된다"
        ),
        correctFlags = mutableListOf(false, false, false, true, false)
    ),

    MultipleChoiceQuiz(
        question = "Python의 딕셔너리(dictionary)의 주요 특징은 무엇인가요?",
        options = mutableListOf(
            "항상 정렬된 순서로 데이터를 저장한다",
            "키-값 쌍을 저장하는 해시 테이블 기반의 자료구조이다",
            "키는 중복될 수 있지만 값은 중복될 수 없다",
            "키와 값 모두 변경이 불가능하다",
            "리스트와 동일한 방식으로 요소를 추가할 수 있다"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Python의 heapq 모듈을 사용하여 구현하는 힙(Heap) 자료구조의 특징은 무엇인가요?",
        options = mutableListOf(
            "모든 연산이 O(1)의 시간 복잡도를 가진다",
            "배열과 동일한 방식으로 요소를 삽입할 수 있다",
            "값이 자동으로 정렬되지 않는다",
            "리스트와 동일한 연산 방식을 따른다",
            "최소 힙(min heap) 또는 최대 힙(max heap)을 구현할 수 있다",
        ),
        correctFlags = mutableListOf(false, false, false, false,true)
    ),

    MultipleChoiceQuiz(
        question = "Python의 deque(덱)는 어떤 자료구조이며 어떤 특징을 가지나요?",
        options = mutableListOf(
            "양쪽 끝에서 빠른 삽입과 삭제가 가능한 이중 끝 큐(Double-ended Queue)이다",
            "스택과 동일한 동작을 한다",
            "항상 정렬된 상태를 유지한다",
            "배열보다 검색 속도가 빠르다",
            "한 번 추가된 요소는 변경할 수 없다"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    )
)

val pythonDataStructureQuizDataKo = QuizData(
    title = "코딩테스트 파이썬 자료구조 퀴즈",
    description = "Python에서 자주 사용되는 자료구조들의 특징과 차이점을 테스트하세요.",
    tags = setOf("자료구조", "코딩 테스트", "Python", "기술 면접")
)

@Suppress("unused")
val pythonDataStructureQuizBundleKo = QuizBundle(
    data = pythonDataStructureQuizDataKo,
    theme = androidQuizTheme,
    quizzes = pythonDataStructureQuizKo,
    titleImage = R.drawable.android_coding_interview,
    scoreCard = codingScoreCard,
)

