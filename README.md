Quizzer

This project is to build a quiz platform that can provide customizable quiz UIs to let user make their own, unique quizzes.
Final Goal of this project is to let user decide all UIs by providing some images, and making some selections.
Current state requires much effort from user, which I don't consider good in mobile environment, so I'll put my goal here to improve this.

Ongoing Android Closed Testing: https://play.google.com/store/apps/details?id=com.asu1.quizzer

이 프로젝트, Quizzer는 사용자가 자신이 원하는 주제의 UI 테마를 만들어서 자신만의 특별한 퀴즈를 만들 수 있게 하려는 프로젝트입니다.
그래서 이 프로젝트의 최종 목표는 사용자가 사진 몇 장과 자신이 선호하는 선택지를 몇 번 선택한다면 이에 걸맞는 UI를 생성하고, 이를 바탕으로 퀴즈를 만들 수 있게 하는 것입니다.
비록 현재 상태로는 사용자의 많은 선택을 필요로 해서, 모바일 환경에서 적합하지 않은 부분이 있어 이를 개선하기 위해 노력 중 입니다.

비공개 테스트 진행중이며 안드로이드 링크입니다 : https://play.google.com/store/apps/details?id=com.asu1.quizzer

--------------------------------------------------------
회원가입 영상 / App Registering Example.

Since I'm trying to change my OAuth level from test to production currently, Release version of app only provides GUEST Accounts.
But I did implement all basis with credential login with google at test, and below is example video.
It goes from Register with google -> Agree Policy -> Nickname duplicate checks -> Add tags -> Register & Login Done.

아직 OAuth가 테스트 레벨에서 프로덕션 레벨로 올리는 심사를 진행중이어서 릴리즈 버전의 앱이 게스트 계정만을 제공하고 있습니다.
하지만 테스트 환경에서는 구글 아이디를 사용한 로그인/회원가입을 전부 테스트 완료하여 심사를 기다리고 있으며, 아래에 영상이 첨부되어 있습니다.
영상의 내용은 구글로 계속하기 -> 약관 동의 -> 닉네임 중복 확인 -> 태그 설정 -> 회원가입 및 로그인 완료의 순서입니다.

https://github.com/user-attachments/assets/1a5a497c-0f32-43bc-80da-16c7c331f32c


----------------------------
Quiz generation test code :
Below video is my testing code at QuizCreationTest.kt
This code works as both test and generation for items.

아래 영상은 QuizCreationTest.kt에 있는 UI 테스팅 코드 실행 영상입니다.
아래 코드를 통해 테스트 그리고 아이템 생성을 하고 있습니다.


https://github.com/user-attachments/assets/3e8bf436-4d68-4739-82e8-94cebf64b653




I am using items from these projects:

QuizCard backgrounds : ChatGPT

Effect images : Lottie

App Icon : [Freepik - Flaticon](https://www.google.com/url?q=https%3A%2F%2Fwww.flaticon.com%2Fkr%2Ffree-icons%2F&amp;sa=D&amp;sntz=1&amp;usg=AOvVaw1qnbHFrezAGPl2L1nOORQw)

Color Picker : https://github.com/skydoves/colorpicker-compose

Youtube Player : https://github.com/PierfrancescoSoffritti/android-youtube-player

Color Scheme Generation : https://github.com/jordond/MaterialKolor

Calendar : https://github.com/kizitonwose/Calendar

Reorderable Items : https://github.com/Calvin-LL/Reorderable
