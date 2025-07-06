package de.benkralex.socius.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import de.benkralex.socius.R

// Set of Material typography styles to start with
val baloo2Family = FontFamily(
    Font(R.font.baloo_2_regular, FontWeight.Normal),
    Font(R.font.baloo_2_bold, FontWeight.Bold),
    Font(R.font.baloo_2_semi_bold, FontWeight.SemiBold),
    Font(R.font.baloo_2_medium, FontWeight.Medium),
    Font(R.font.baloo_2_extra_bold, FontWeight.ExtraBold),
)

val balooBhaijaan2Family = FontFamily(
    Font(R.font.baloo_bhaijaan_2_regular, FontWeight.Normal),
    Font(R.font.baloo_bhaijaan_2_bold, FontWeight.Bold),
    Font(R.font.baloo_bhaijaan_2_semi_bold, FontWeight.SemiBold),
    Font(R.font.baloo_bhaijaan_2_medium, FontWeight.Medium),
    Font(R.font.baloo_bhaijaan_2_extra_bold, FontWeight.ExtraBold),
)

val Typography = Typography(
    displayLarge = Typography().displayLarge.copy(fontFamily = baloo2Family),
    displayMedium = Typography().displayMedium.copy(fontFamily = baloo2Family),
    displaySmall = Typography().displaySmall.copy(fontFamily = baloo2Family),

    headlineLarge = Typography().headlineLarge.copy(fontFamily = baloo2Family),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = baloo2Family),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = baloo2Family),

    titleLarge = Typography().titleLarge.copy(fontFamily = baloo2Family),
    titleMedium = Typography().titleMedium.copy(fontFamily = baloo2Family),
    titleSmall = Typography().titleSmall.copy(fontFamily = baloo2Family),

    bodyLarge = Typography().bodyLarge.copy(fontFamily = balooBhaijaan2Family),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = balooBhaijaan2Family),
    bodySmall = Typography().bodySmall.copy(fontFamily = balooBhaijaan2Family),

    labelLarge = Typography().labelLarge.copy(fontFamily = balooBhaijaan2Family),
    labelMedium = Typography().labelMedium.copy(fontFamily = balooBhaijaan2Family),
    labelSmall = Typography().labelSmall.copy(fontFamily = balooBhaijaan2Family)
)