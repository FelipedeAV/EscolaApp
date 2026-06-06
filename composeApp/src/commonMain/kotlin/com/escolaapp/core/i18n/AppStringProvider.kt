package com.escolaapp.core.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.lyricist.Lyricist

val LocalAppStrings = staticCompositionLocalOf { PtStrings }
val LocalLyricist = staticCompositionLocalOf<Lyricist<AppStrings>> { error("No Lyricist provided") }

@Composable
fun ProvideAppStrings(
    defaultLanguageTag: String = "pt",
    translations: Map<String, AppStrings> = mapOf("pt" to PtStrings, "en" to EnStrings),
    content: @Composable () -> Unit,
) {
    val lyricist = remember { Lyricist<AppStrings>(defaultLanguageTag = defaultLanguageTag, translations = translations) }
    val state by lyricist.state.collectAsState()
    CompositionLocalProvider(
        LocalAppStrings provides state.strings,
        LocalLyricist provides lyricist,
        content = content,
    )
}
