package com.example.composenotesapp1.ui.notes.components.drawer

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties.TestTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composenotesapp1.R
import com.example.composenotesapp1.commons.ColorPalletName
import com.example.composenotesapp1.commons.ColorSchemeName
import com.example.composenotesapp1.commons.Constants
import com.example.composenotesapp1.commons.TestTags
import com.example.composenotesapp1.data.local.dataStore.NoteDataStore
import com.example.composenotesapp1.ui.theme.CadmiumGreenLightPrimary
import com.example.composenotesapp1.ui.theme.CobaltBlueLightPrimary
import com.example.composenotesapp1.ui.theme.CrimsonLightPrimary
import com.example.composenotesapp1.ui.theme.PurpleLightPrimary
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun NavDrawer(
    drawerState: DrawerState,
    onError: (String) -> Unit,
    content: @Composable () -> Unit
) {

    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val scope = rememberCoroutineScope()

    val appPath = ""
    val devPagePath = ""

    fun openPlayStore(appURL: String) {
        val playIntent: Intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(appURL)
        }
        try {
            context.startActivity(playIntent)
        } catch (e: Exception) {
            onError("Cannot Open URL")
        }
    }


    val dataStore = NoteDataStore(LocalContext.current)
    var currentScheme by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        dataStore.getStringData(Constants.COLOR_SCHEME).collect {
            currentScheme = it
        }
    }

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


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RectangleShape,
                drawerContainerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(0.dp)
                    .width(screenWidth - 50.dp)
            ) {
                SectionTitle(title = stringResource(id = R.string.about_us))

                NavDrawerItem(
                    leftIcon = Icons.Filled.Star,
                    title = stringResource(id = R.string.rate_app),
                    subtitle = stringResource(id = R.string.rate_app_description),
                    testTagName = "",
                    onClick = { openPlayStore(appPath) })

                NavDrawerItem(
                    leftIcon = Icons.Filled.Shop,
                    title = stringResource(id = R.string.more_apps),
                    subtitle = stringResource(id = R.string.more_apps_description),
                    testTagName = "",
                    onClick = { openPlayStore(devPagePath) })

                NavDrawerItem(
                    leftIcon = Icons.Filled.Update,
                    title = stringResource(id = R.string.app_version),
                    subtitle = "BuildConfig.VEERSION_NAME = 1.0",
                    testTagName = "",
                    onClick = { })

                SectionTitle(title = stringResource(id = R.string.preferences))

                NavDrawerColorPicker(
                    leftIcon = Icons.Filled.ColorLens,
                    title = stringResource(id = R.string.theme_color),
                    testTagName = ""
                ) {
                    setColorPallet(it.second)
                }

                NavDrawerItemColorSwitchScheme(
                    leftIcon = Icons.Filled.Bedtime,
                    title = stringResource(id = R.string.theme_setting),
                    testTagName = "",
                    currentScheme = currentScheme
                ) {
                    setColorScheme(it)
                }
            }
        },
        content = { content() },
        modifier = Modifier.semantics { testTag = TestTags.NAV_DRAWER }
    )
}

@Composable
fun SectionTitle(
    title: String
) {
    val firstLetterColor = MaterialTheme.colorScheme.primary
    val restLetterColor = MaterialTheme.colorScheme.onSurface

    val modifiedTitle = title.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }

    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = firstLetterColor, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp
                )
            ) {
                append(modifiedTitle.first())
            }
            withStyle(
                style = SpanStyle(
                    color = restLetterColor, fontWeight = FontWeight.Bold, fontSize = 22.sp
                )
            ) {
                append(modifiedTitle.substring(1))
            }
        },
        modifier = Modifier.padding(
            start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp
        )
    )
}

@Composable
fun NavDrawerItem(
    leftIcon: ImageVector,
    title: String,
    subtitle: String,
    testTagName: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp)
            .semantics { testTag = testTagName },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
        ) {
            Icon(
                imageVector = leftIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp)
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun NavDrawerColorPicker(
    leftIcon: ImageVector,
    title: String,
    testTagName: String,
    onColorPicked: (color: Pair<Color, String>) -> Unit
) {

    val colorList = listOf(
        Pair(PurpleLightPrimary, ColorPalletName.PURPLE),
        Pair(CrimsonLightPrimary, ColorPalletName.CRIMSON),
        Pair(CadmiumGreenLightPrimary, ColorPalletName.CADMIUM_GREEN),
        Pair(CobaltBlueLightPrimary, ColorPalletName.COBALT_BLUE)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp)
            .semantics { testTag = testTagName },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
        ) {
            Icon(
                imageVector = leftIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp)
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                colorList.forEach {
                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .width(24.dp)
                        .height(24.dp)
                        .background(it.first)
                        .clickable { onColorPicked(it) })
                }
            }
        }
    }
}

@Composable
fun NavDrawerItemColorSwitchScheme(
    leftIcon: ImageVector,
    title: String,
    testTagName: String,
    currentScheme: String,
    onColorSchemePicked: (scheme: String) -> Unit
) {
    val lightModeString = stringResource(id = R.string.switch_to_light_mode)
    val darkModeString = stringResource(id = R.string.switch_to_dark_mode)
    val subtitle = if (currentScheme == ColorSchemeName.DARK_MODE)
        lightModeString else darkModeString

    fun onTileClicked() {
        if (subtitle == darkModeString) {
            onColorSchemePicked(ColorSchemeName.DARK_MODE)
        } else {
            onColorSchemePicked(ColorSchemeName.LIGHT_MODE)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp)
            .semantics { testTag = testTagName }
            .clickable { onTileClicked() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
        ) {
            Icon(
                imageVector = leftIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp)
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp)
        )

    }
}

@Preview
@Composable
private fun DrawerPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    NavDrawer(drawerState = drawerState, onError = {}, content = {})

}