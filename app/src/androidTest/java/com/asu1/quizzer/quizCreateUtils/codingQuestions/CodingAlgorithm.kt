package com.asu1.quizzer.quizCreateUtils.codingQuestions

import com.asu1.quizzer.quizCreateUtils.TestQuiz1
import com.asu1.quizzer.quizCreateUtils.quizTheme.codingTheme

val codingApproachQuiz = listOf(
    TestQuiz1(
        point = 5,
        question = "You need to determine the minimum number of coins required to make a given amount of money. Which algorithmic approach is best suited for this problem?",
        answers = mutableListOf(
            "Dynamic Programming",
            "Greedy Algorithm",
            "Backtracking",
            "Brute Force",
            "Divide and Conquer"
        ),
        ans = mutableListOf(true, false, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "Given a maze represented as a grid, where you can move in four directions, which approach is the most efficient to find the shortest path to the exit?",
        answers = mutableListOf(
            "Dijkstra’s Algorithm",
            "Bellman-Ford Algorithm",
            "BFS (Breadth-First Search)",
            "Floyd-Warshall Algorithm",
            "A* Algorithm"
        ),
        ans = mutableListOf(false, false, true, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "You are solving the N-Queens problem where you must place N queens on an N×N chessboard so that no two queens threaten each other. Which approach is the most efficient?",
        answers = mutableListOf(
            "Brute Force",
            "Backtracking",
            "Dynamic Programming",
            "Hashing",
            "Monte Carlo Method"
        ),
        ans = mutableListOf(false, true, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "You need to determine the minimum number of platforms required for a set of train arrivals and departures at a station. Which approach would work best?",
        answers = mutableListOf(
            "Dynamic Programming",
            "Divide and Conquer",
            "Brute Force",
            "Greedy Algorithm",
            "Depth-First Search"
        ),
        ans = mutableListOf(false, false, false, true, false)
    ),

    TestQuiz1(
        point = 5,
        question = "Given a large integer array, you need to find the majority element (an element that appears more than n/2 times). Which algorithmic approach is the most efficient?",
        answers = mutableListOf(
            "Dynamic Programming",
            "Divide and Conquer",
            "Greedy Algorithm",
            "Moore’s Voting Algorithm",
            "BFS/DFS"
        ),
        ans = mutableListOf(false, false, false, true, false)
    ),

    TestQuiz1(
        point = 5,
        question = "You have a password generator that must try all possible combinations of a given character set to crack an unknown password. Which approach is best suited?",
        answers = mutableListOf(
            "Greedy Algorithm",
            "Backtracking",
            "Dynamic Programming",
            "Brute Force",
            "A* Search"
        ),
        ans = mutableListOf(false, false, false, true, false)
    ),

    TestQuiz1(
        point = 5,
        question = "You need to search for an element in a sorted array with logarithmic time complexity. Which technique is most efficient?",
        answers = mutableListOf(
            "Linear Search",
            "Binary Search",
            "Hashing",
            "BFS",
            "Ternary Search"
        ),
        ans = mutableListOf(false, true, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "You are given a rod of length N and a list of prices for different lengths. You must determine the maximum profit you can achieve by cutting the rod into smaller lengths. Which approach should you use?",
        answers = mutableListOf(
            "Dynamic Programming",
            "Greedy Algorithm",
            "Backtracking",
            "Brute Force",
            "Monte Carlo Algorithm"
        ),
        ans = mutableListOf(true, false, false, false, false)
    )
)

val codingApproachQuizData = codingTheme.copy(
    quizzes = codingApproachQuiz,
    title = "Coding Test Algorithm Quiz",
    description = "Test your understanding of different algorithmic approaches and when to use them in coding tests.",
    tags = setOf("Algorithms", "Coding Tests", "Problem Solving", "Technical Interview")
)

//------------------------------------------------------------------


val codingApproachQuizKo = listOf(
    TestQuiz1(
        point = 5,
        question = "주어진 금액을 만들기 위해 필요한 최소 동전 개수를 결정해야 합니다. 어떤 접근 방식이 가장 적절한가요?",
        answers = mutableListOf(
            "Dynamic Programming",
            "Greedy Algorithm",
            "Backtracking",
            "Brute Force",
            "Divide and Conquer"
        ),
        ans = mutableListOf(true, false, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "격자로 표현된 미로에서 네 방향으로 이동할 수 있을 때, 출구까지의 최단 경로를 찾기 위한 가장 효율적인 접근 방식은 무엇인가요?",
        answers = mutableListOf(
            "Dijkstra’s Algorithm",
            "Bellman-Ford Algorithm",
            "BFS (Breadth-First Search)",
            "Floyd-Warshall Algorithm",
            "A* Algorithm"
        ),
        ans = mutableListOf(false, false, true, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "N×N 체스판에서 N개의 퀸을 서로 공격하지 않도록 배치하는 N-Queens 문제를 해결하려고 합니다. 가장 효율적인 접근 방식은 무엇인가요?",
        answers = mutableListOf(
            "Brute Force",
            "Backtracking",
            "Dynamic Programming",
            "Hashing",
            "Monte Carlo Method"
        ),
        ans = mutableListOf(false, true, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "기차역에서 여러 대의 기차가 도착하고 출발하는데 필요한 최소 플랫폼 수를 결정해야 합니다. 가장 적절한 접근 방식은 무엇인가요?",
        answers = mutableListOf(
            "Dynamic Programming",
            "Divide and Conquer",
            "Brute Force",
            "Greedy Algorithm",
            "Depth-First Search"
        ),
        ans = mutableListOf(false, false, false, true, false)
    ),

    TestQuiz1(
        point = 5,
        question = "큰 정수 배열에서 과반수(n/2 이상) 이상 등장하는 요소를 찾아야 합니다. 가장 효율적인 방식은 무엇인가요?",
        answers = mutableListOf(
            "Dynamic Programming",
            "Divide and Conquer",
            "Greedy Algorithm",
            "Moore’s Voting Algorithm",
            "BFS/DFS"
        ),
        ans = mutableListOf(false, false, false, true, false)
    ),

    TestQuiz1(
        point = 5,
        question = "비밀번호 생성기가 주어진 문자 집합의 모든 가능한 조합을 시도하여 알 수 없는 비밀번호를 해독해야 합니다. 어떤 방식이 가장 적절한가요?",
        answers = mutableListOf(
            "Greedy Algorithm",
            "Backtracking",
            "Dynamic Programming",
            "Brute Force",
            "A* Search"
        ),
        ans = mutableListOf(false, false, false, true, false)
    ),

    TestQuiz1(
        point = 5,
        question = "정렬된 배열에서 특정 요소를 로그 시간 안에 찾고자 할 때 가장 효율적인 검색 방법은 무엇인가요?",
        answers = mutableListOf(
            "Linear Search",
            "Binary Search",
            "Hashing",
            "BFS",
            "Ternary Search"
        ),
        ans = mutableListOf(false, true, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "길이 N의 막대와 다양한 길이에 대한 가격 목록이 주어졌을 때, 막대를 적절히 잘라서 얻을 수 있는 최대 이익을 구해야 합니다. 어떤 방식을 사용해야 하나요?",
        answers = mutableListOf(
            "Dynamic Programming",
            "Greedy Algorithm",
            "Backtracking",
            "Brute Force",
            "Monte Carlo Algorithm"
        ),
        ans = mutableListOf(true, false, false, false, false)
    )
)

val codingApproachQuizDataKo = codingTheme.copy(
    quizzes = codingApproachQuizKo,
    title = "코딩테스트 알고리즘 퀴즈",
    description = "코딩 테스트에서 사용되는 다양한 알고리즘을 적절히 적용할 수 있는 능력을 테스트하세요.",
    tags = setOf("알고리즘", "코딩 테스트", "문제 해결", "기술 면접")
)
