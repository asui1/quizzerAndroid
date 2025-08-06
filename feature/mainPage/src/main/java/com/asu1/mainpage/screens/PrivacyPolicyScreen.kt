package com.asu1.mainpage.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.customComposable.topBar.QuizzerTopBarBase
import com.asu1.activityNavigation.Route
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.utils.LanguageSetter

@Composable
fun PrivacyPolicy(navController: NavController) {
    Scaffold(
        topBar = {
            QuizzerTopBarBase(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer),
                header = {
                    IconButton(onClick = { navController.popBackStack(
                        Route.Home,
                        inclusive = false
                    ) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Move Back to Home")
                    }
                },
                body = {
                    Text(
                        text = stringResource(R.string.privacy_policy),
                        style = QuizzerTypographyDefaults.quizzerBodySmallNormal,
                    )
                },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                PrivacyPolicyText()
            }
        }
    )
}

fun annotatedPolicyKo(): AnnotatedString{
    return buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("1. 개인정보의 수집 및 이용 목적\n")
        }
        append("Quizzer(이하 \"앱\")은 사용자의 개인정보를 중요하게 생각하며, 아래와 같은 목적을 위해 개인정보를 수집 및 이용합니다.\n\n")
        append("• 사용자 식별 및 앱 서비스 제공\n")
        append("• 사용자와의 의사소통을 위한 이메일 사용\n")
        append("• 앱 내부에서의 활동 정보 분석을 통한 서비스 개선\n\n")

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("2. 수집하는 개인정보 항목\n")
        }
        append("앱은 다음과 같은 개인정보를 수집합니다.\n\n")
        append("• ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("이메일 주소") }
        append(": 사용자 식별 및 의사소통을 위해 사용됩니다.\n")
        append("• ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("프로필 이미지") }
        append(": 사용자 식별 및 의사소통을 위해 사용됩니다.\n")
        append("• ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("앱 내부 활동 정보") }
        append(": 앱 사용 패턴 분석 및 서비스 개선을 위해 사용됩니다.\n\n")

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("3. 개인정보의 보유 및 이용기간\n")
        }
        append("수집된 개인정보는 이용목적이 달성되거나 사용자가 삭제를 요청할 때까지 보유 및 이용됩니다. 사용자가 요청할 경우, 수집된 개인정보는 지체 없이 삭제됩니다.\n\n")

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("4. 개인정보의 제3자 제공\n")
        }
        append("앱은 사용자의 사전 동의 없이는 개인정보를 제3자에게 제공하지 않습니다.\n\n")

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("5. 개인정보의 안전성 확보 조치\n")
        }
        append("앱은 사용자의 개인정보를 안전하게 관리하기 위해 다음과 같은 조치를 취하고 있습니다.\n")
        append("• 개인정보의 암호화\n")
        append("• 해킹이나 컴퓨터 바이러스 등에 의한 개인정보 유출을 막기 위한 기술적 대책\n")
        append("• 개인정보 접근 권한 제한\n\n")

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("6. 개인정보보호책임자\n")
        }
        append("개인정보와 관련된 문의사항은 아래의 개인정보보호책임자에게 연락해 주시기 바랍니다.\n")
        append("책임자: 조장현\n")
        append("연락처: whwkd122@gmail.com\n\n")

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("7. 개인정보 처리방침의 변경\n")
        }
        append("개인정보처리방침이 변경될 경우, 변경 사항은 앱 내 공지사항을 통해 고지됩니다.\n\n")
        append("본 방침은 2024년 8월 4일부터 시행됩니다.")
    }
}

fun annotatedPolicyEn(): AnnotatedString{
    return buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("1. Purpose of Collecting and Using Personal Information\n")
        }
        append("Quizzer (hereinafter referred to as \"the App\") highly values user privacy and collects ")
        append("and uses personal information for the following purposes:\n\n")
        append("• User identification and provision of app services\n")
        append("• Communication with users via email\n")
        append("• Analyzing in-app activity information to improve services\n\n")

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("2. Items of Personal Information Collected\n")
        }
        append("The App collects the following personal information:\n\n")
        append("• ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Email Address") }
        append(": Used for user identification and communication.\n")
        append("• ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Profile Image") }
        append(": Used for user identification and communication.\n")
        append("• ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("In-app Activity Information") }
        append(": Used for analyzing app usage patterns and improving services.\n\n")

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("3. Retention and Use Period of Personal Information\n")
        }
        append("Collected personal information is retained " +
                "and used until the purpose of use is achieved or the user requests deletion. " +
                "Upon user request, the collected personal information will be promptly deleted.\n\n")

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("4. Provision of Personal Information to Third Parties\n")
        }
        append("The App does not provide personal information " +
                "to third parties without the prior consent of the user.\n\n")

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("5. Measures to Ensure the Safety of Personal Information\n")
        }
        append("The App takes the following measures to ensure the safety of users' personal information:\n")
        append("• Encryption of personal information\n")
        append("• Technical measures to prevent personal information leakage due to hacking or computer viruses\n")
        append("• Restriction of access rights to personal information\n\n")

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("6. Personal Information Protection Officer\n")
        }
        append("For inquiries related to personal information, " +
                "please contact the Personal Information Protection Officer below.\n")
        append("Officer: [Name of Personal Information Protection Officer]\n")
        append("Contact: whwkd122@gmail.com\n\n")

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("7. Changes to the Privacy Policy\n")
        }
        append("If there are any changes to the privacy policy, " +
                "the changes will be notified through the app's notice board.\n\n")
        append("This policy is effective as of August 4, 2024.")
    }
}

@Composable
fun PrivacyPolicyText(modifier: Modifier = Modifier) {
    val annotatedString = remember(LanguageSetter.lang) {
        when (LanguageSetter.lang) {
            "ko" -> annotatedPolicyKo()
            else -> annotatedPolicyEn()
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Preview
@Composable
fun PreviewPrivacyPolicy() {
    val navController = rememberNavController()
    com.asu1.resources.QuizzerAndroidTheme {
        PrivacyPolicy(
            navController = navController
        )
    }
}
