package com.asu1.quizzer.quizCreateUtils.codingQuestions

import com.asu1.models.serializers.BodyType
import com.asu1.quizzer.quizCreateUtils.TestQuiz1
import com.asu1.quizzer.quizCreateUtils.quizTheme.codingTheme

val pythonUtilityQuiz = listOf(
    TestQuiz1(
        point = 5,
        question = "What does the Python map() function do?",
        answers = mutableListOf(
            "Filters elements based on a condition",
            "Sorts elements in ascending order",
            "Applies a function to each item in an iterable",
            "Finds the maximum value in a list",
            "Combines two lists into key-value pairs"
        ),
        ans = mutableListOf(false, false, true, false, false),
        bodyType = BodyType.CODE("""
numbers = [1, 2, 3, 4, 5]
squared = list(map(lambda x: x ** 2, numbers))
print(squared)
""")
    ),

    TestQuiz1(
        point = 5,
        question = "How does the Python filter() function work?",
        answers = mutableListOf(
            "Sorts a list in-place",
            "Applies a function to every element in a list",
            "Finds the sum of all list elements",
            "Removes duplicate elements from a list",
            "Returns only elements that satisfy a condition",
        ),
        ans = mutableListOf(false, false, false, false, true),
        bodyType = BodyType.CODE("""
numbers = [10, 15, 20, 25, 30]
even_numbers = list(filter(lambda x: x % 2 == 0, numbers))
print(even_numbers)
""")
    ),

    TestQuiz1(
        point = 5,
        question = "What is the purpose of the sorted() function in Python?",
        answers = mutableListOf(
            "Sorts elements in ascending order by default",
            "Removes duplicate values from a list",
            "Finds the smallest value in a list",
            "Reverses the order of elements in a list",
            "Returns only unique values from a list"
        ),
        ans = mutableListOf(true, false, false, false, false),
        bodyType = BodyType.CODE("""
numbers = [5, 3, 8, 1, 2]
sorted_numbers = sorted(numbers)
print(sorted_numbers)
""")
    ),

    TestQuiz1(
        point = 5,
        question = "What does the any() function do in Python?",
        answers = mutableListOf(
            "Returns True only if all elements are True",
            "Returns True if at least one element in an iterable is True",
            "Checks if an iterable is empty",
            "Returns False if at least one element is False",
            "Sorts elements in descending order"
        ),
        ans = mutableListOf(false, true, false, false, false),
        bodyType = BodyType.CODE("""
numbers = [0, 0, 1, 0]
result = any(numbers)
print(result)
""")
    ),

    TestQuiz1(
        point = 5,
        question = "How does the zip() function work in Python?",
        answers = mutableListOf(
            "Sorts two lists together",
            "Finds common elements in two lists",
            "Merges dictionaries",
            "Combines multiple iterables element-wise",
            "Splits a list into multiple lists"
        ),
        ans = mutableListOf(false, false, false, true, false),
        bodyType = BodyType.CODE("""
names = ['Alice', 'Bob', 'Charlie']
ages = [25, 30, 35]
pairs = list(zip(names, ages))
print(pairs)
""")
    )
)

val pythonUtilityQuizData = codingTheme.copy(
    quizzes = pythonUtilityQuiz,
    title = "Python Utility Functions Quiz",
    description = "Test your knowledge of Python's commonly used utility functions and how they work.",
    tags = setOf("Python", "Coding Test", "Utility Functions", "Technical Interview")
)

val pythonUtilityQuizKo = listOf(
    TestQuiz1(
        point = 5,
        question = "Python의 map() 함수는 무엇을 하나요?",
        answers = mutableListOf(
            "조건에 따라 요소를 필터링한다",
            "요소를 오름차순으로 정렬한다",
            "이터러블의 각 항목에 함수를 적용한다",
            "리스트에서 최대값을 찾는다",
            "두 개의 리스트를 키-값 쌍으로 결합한다"
        ),
        ans = mutableListOf(false, false, true, false, false),
        bodyType = BodyType.CODE("""
numbers = [1, 2, 3, 4, 5]
squared = list(map(lambda x: x ** 2, numbers))
print(squared)
""")
    ),

    TestQuiz1(
        point = 5,
        question = "Python의 filter() 함수는 어떻게 작동하나요?",
        answers = mutableListOf(
            "리스트를 제자리에서 정렬한다",
            "리스트의 모든 요소에 함수를 적용한다",
            "리스트의 모든 요소의 합을 찾는다",
            "리스트에서 중복 요소를 제거한다",
            "조건을 만족하는 요소만 반환한다"
        ),
        ans = mutableListOf(false, false, false, false, true),
        bodyType = BodyType.CODE("""
numbers = [10, 15, 20, 25, 30]
even_numbers = list(filter(lambda x: x % 2 == 0, numbers))
print(even_numbers)
""")
    ),

    TestQuiz1(
        point = 5,
        question = "Python의 sorted() 함수의 목적은 무엇인가요?",
        answers = mutableListOf(
            "기본적으로 요소를 오름차순으로 정렬한다",
            "리스트에서 중복 값을 제거한다",
            "리스트에서 가장 작은 값을 찾는다",
            "리스트의 요소 순서를 반대로 바꾼다",
            "리스트에서 유일한 값만 반환한다"
        ),
        ans = mutableListOf(true, false, false, false, false),
        bodyType = BodyType.CODE("""
numbers = [5, 3, 8, 1, 2]
sorted_numbers = sorted(numbers)
print(sorted_numbers)
""")
    ),

    TestQuiz1(
        point = 5,
        question = "Python의 any() 함수는 무엇을 하나요?",
        answers = mutableListOf(
            "모든 요소가 True일 경우에만 True를 반환한다",
            "이터러블에서 하나라도 True이면 True를 반환한다",
            "이터러블이 비어 있는지 확인한다",
            "하나라도 False이면 False를 반환한다",
            "요소를 내림차순으로 정렬한다"
        ),
        ans = mutableListOf(false, true, false, false, false),
        bodyType = BodyType.CODE("""
numbers = [0, 0, 1, 0]
result = any(numbers)
print(result)
""")
    ),

    TestQuiz1(
        point = 5,
        question = "Python의 zip() 함수는 어떻게 작동하나요?",
        answers = mutableListOf(
            "두 개의 리스트를 함께 정렬한다",
            "두 리스트에서 공통 요소를 찾는다",
            "딕셔너리를 병합한다",
            "여러 이터러블을 요소별로 결합한다",
            "리스트를 여러 개의 리스트로 분할한다"
        ),
        ans = mutableListOf(false, false, false, true, false),
        bodyType = BodyType.CODE("""names = ['Alice', 'Bob', 'Charlie']
ages = [25, 30, 35]
pairs = list(zip(names, ages))
print(pairs)""")
    )
)

val pythonUtilityQuizDataKo = codingTheme.copy(
    quizzes = pythonUtilityQuizKo,
    title = "Python 유틸리티 함수 퀴즈",
    description = "Python의 자주 사용되는 유틸리티 함수와 작동 방식을 테스트하세요.",
    tags = setOf("Python", "코딩 테스트", "유틸리티 함수", "기술 면접")
)