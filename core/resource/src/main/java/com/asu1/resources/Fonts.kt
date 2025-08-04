package com.asu1.resources

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)

val robotoBlack = FontFamily(
    androidx.compose.ui.text.font.Font(R.font.roboto_black, FontWeight.Black)
)
val GothicA1 = FontFamily(
    androidx.compose.ui.text.font.Font(resId = R.font.gothic_a1_bold, weight = FontWeight.Bold),
    androidx.compose.ui.text.font.Font(resId = R.font.gothic_a1_medium, weight = FontWeight.Medium),
    androidx.compose.ui.text.font.Font(resId = R.font.gothic_a1_light, weight = FontWeight.Light),
)

val NotoSans = FontFamily(
    Font(googleFont = GoogleFont(fonts[1]), fontProvider = provider),
)

fun getFontFamily(selection: Int): FontFamily{
    if(selection >= fonts.size) return GothicA1
    return FontFamily(
        Font(googleFont = GoogleFont(fonts[selection]), fontProvider = provider),
    )
}
