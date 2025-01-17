package com.example.composenotesapp1.share_preview

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.composenotesapp1.R
import com.example.composenotesapp1.commons.DateUtil
import com.example.composenotesapp1.commons.TestTags
import com.example.composenotesapp1.components.LockScreenOrientation
import com.example.composenotesapp1.components.bottomSheet.RegularBottomSheet
import com.example.composenotesapp1.components.dialog.TextDialog
import com.example.composenotesapp1.data.model.NoteModel
import com.example.composenotesapp1.share_preview.components.SharePreviewAppBar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharePreviewPage(
    navHostController: NavHostController,
    note: NoteModel?
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)
    val rememberedScrollBehavior = remember { scrollBehavior }

    val shareNoteDialogState = remember { mutableStateOf(false) }

    val snackBarHostState = remember { SnackbarHostState() }

    val date = DateUtil.convertDateString("EEE, MMM dd HH:mm yyyy",
        note?.createdAt?.toString() ?: Date().toString()
    )
    val title = note?.title ?: "title"
    val text = note?.note ?: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eget egestas nisi, sit amet tincidunt lorem. Aliquam pharetra, tortor nec bibendum rhoncus, lorem est placerat quam, vitae posuere dui massa eu tortor. Nullam nibh turpis, egestas id sollicitudin nec, placerat vitae libero. Nulla commodo ex orci, et commodo leo tempor vitae. Sed posuere dolor urna, vitae tempus magna lobortis ac. Fusce dignissim eros sit amet velit commodo, ac viverra augue iaculis. Aenean facilisis, est ut gravida feugiat, est sem varius neque, at ultricies lectus nisi sed urna. Ut sagittis orci ac ante convallis eleifend. Nulla nec congue purus, at sagittis felis. Sed mattis quam orci, molestie auctor orci venenatis et. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Sed vestibulum placerat enim eu tempor. Aenean sed augue ut eros hendrerit porttitor.\n"
    val joinedText = date + "\n\n" + title + "\n\n" + text

    val openBottomSheet = rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded = remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded.value
    )

    val context = LocalContext.current

    Scaffold(
        topBar = {
            SharePreviewAppBar(
                onBackPressed = {
                    navHostController.popBackStack()
                },
                onHelpPressed = {
                    shareNoteDialogState.value = true
                },
                scrollBehavior = rememberedScrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { openBottomSheet.value = true },
                modifier = Modifier
                    .semantics {
                        testTag = TestTags.SHARE_NOTE_FAB
                    },
                text = {
                    Text(stringResource(R.string.share_note), fontSize = 16.sp, fontWeight = FontWeight.Medium)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = stringResource(R.string.fab)
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            ) {
                Text(
                    date,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    TextDialog(
        title = stringResource(R.string.information),
        description = stringResource(R.string.share_note_information),
        isOpened = shareNoteDialogState.value,
        onDismissCallBack = { shareNoteDialogState.value = false }

    )

    RegularBottomSheet(
        isOpened = openBottomSheet.value,
        bottomSheetState = bottomSheetState,
        onDismissRequest = { openBottomSheet.value = false }
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
            Text(stringResource(R.string.share_note_as_text),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.clickable {
                    openBottomSheet.value = false
                    shareText(joinedText, context)
                })
        }
    }
}

fun shareText(text: String, context: Context) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

@Preview
@Composable
fun SharePreviewPagePreview() {
    SharePreviewPage(
        navHostController = rememberNavController(),
        note = null
    )
}