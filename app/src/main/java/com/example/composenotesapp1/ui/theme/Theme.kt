package com.example.composenotesapp1.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.composenotesapp1.commons.ColorPalletName
import com.example.composenotesapp1.commons.ColorSchemeName
import com.example.composenotesapp1.commons.Constants
import com.example.composenotesapp1.data.local.dataStore.NoteDataStore
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@Composable
fun ComposeNotesApp1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val dataStore = NoteDataStore(LocalContext.current)
    val scope = rememberCoroutineScope()

    fun setColorPallet(colorPallet: String) {
        scope.launch {
            dataStore.setStringData(key = Constants.COLOR_PALLET, data = colorPallet)
        }
    }

    fun setColorScheme(colorScheme: String) {
        scope.launch {
            dataStore.setStringData(key = Constants.COLOR_SCHEME, data = colorScheme)
        }
    }

    val colorScheme = remember {
        val scheme = if (darkTheme) ColorSchemeName.DARK_MODE else ColorSchemeName.LIGHT_MODE
        mutableStateOf(ColorSchemeChoice.getColorScheme(scheme, ColorPalletName.PURPLE))
    }

    LaunchedEffect(key1 = Unit) {
        val colorSchemeFlow = dataStore.getStringData(Constants.COLOR_SCHEME)
        val colorPalletFLow = dataStore.getStringData(Constants.COLOR_PALLET)

        val combinedFlow = combine(
            colorSchemeFlow, colorPalletFLow
        ) { scheme, pallet ->
            val defaultScheme = scheme.ifEmpty {
                if (darkTheme) ColorSchemeName.DARK_MODE
                else ColorSchemeName.LIGHT_MODE
            }
            val defaultPallet = pallet.ifEmpty { ColorPalletName.PURPLE }
            setColorScheme(defaultScheme)
            setColorPallet(defaultPallet)

            Pair(defaultScheme, defaultPallet)
        }

        combinedFlow.collect {
            colorScheme.value = ColorSchemeChoice.getColorScheme(it.first, it.second)
        }

    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        darkIcons = !darkTheme, color = colorScheme.value.primary
    )

    MaterialTheme(
        colorScheme = colorScheme.value, typography = Typography, content = content
    )
}