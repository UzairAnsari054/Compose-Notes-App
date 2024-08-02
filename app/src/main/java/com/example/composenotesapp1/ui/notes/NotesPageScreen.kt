package com.example.composenotesapp1.ui.notes

import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.composenotesapp1.R
import com.example.composenotesapp1.commons.TestTags
import com.example.composenotesapp1.components.LockScreenOrientation
import com.example.composenotesapp1.components.dialog.LoadingDialog
import com.example.composenotesapp1.components.dialog.TextDialog
import com.example.composenotesapp1.data.model.NoteModel
import com.example.composenotesapp1.navigation.Route
import com.example.composenotesapp1.ui.notes.components.drawer.NavDrawer
import com.example.composenotesapp1.ui.notes.components.item.NoteItem
import com.example.composenotesapp1.ui.notes.components.navbar.NotesAppBar
import com.example.composenotesapp1.ui.notes.components.sheet.FilterSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesPageScreen(
    navController: NavHostController,
    viewModel: NotesPageBaseVm = hiltViewModel<NotesPageVm>()
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val noteListState = viewModel.noteList.observeAsState()
    val loadingState = viewModel.loader.observeAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }
    val filteredNoteListState = remember { mutableStateOf<List<NoteModel>>(listOf()) }
    val loadingDialog = remember { mutableStateOf(false) }
    val deleteDialog = remember { mutableStateOf(false) }

    val openBottomSheet = rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded = remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded.value
    )

    LaunchedEffect(key1 = noteListState.value) {
        // Showing error snackBar on error
        noteListState.value?.onFailure {
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = it.message ?: "",
                    withDismissAction = true
                )
            }
        }
    }

    LaunchedEffect(noteListState.value, viewModel.searchingTitleText.value) {
        // Replace filteredNoteListState value with filtered noteList state
        // every searchedTitleText changed
        filteredNoteListState.value = noteListState.value?.getOrNull()?.filter { note ->
            note.title.contains(viewModel.searchingTitleText.value, true)
        } ?: listOf()
    }

    LaunchedEffect(key1 = loadingState.value) {
        // Showing loading dialog whenever loading state is true
        loadingDialog.value = (loadingState.value == true)
    }

    val deletedMessage = stringResource(id = R.string.note_is_successfully_deleted)

    fun deleteNoteList() {
        scope.launch {
            viewModel.deleteNoteList()
                .onSuccess {
                    deleteDialog.value = false
                    viewModel.unMarkAllNote()

                    snackBarHostState.showSnackbar(
                        message = deletedMessage,
                        withDismissAction = true
                    )
                }
                .onFailure {
                    deleteDialog.value = false

                    snackBarHostState.showSnackbar(
                        message = it.message ?: "",
                        withDismissAction = true
                    )
                }
        }
    }

    NavDrawer(
        drawerState = drawerState,
        onError = {
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = it,
                    withDismissAction = true
                )
            }
        },
        content = {
            Scaffold(
                topBar = {
                    NotesAppBar(
                        isMarking = viewModel.isMarking.value,
                        markedNoteListSize = viewModel.markedNoteList.size,
                        isSearching = viewModel.isSearching.value,
                        searchedTitle = viewModel.searchingTitleText.value,
                        toggleDrawerCallback = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        selectAllCallback = {
                            viewModel.noteList.value?.getOrNull().let {
                                if (it != null) viewModel.markAllNote(it)
                            }
                        },
                        unSelectAllCallback = {
                            viewModel.unMarkAllNote()
                        },
                        onSearchValueChange = {
                            viewModel.searchingTitleText.value = it
                        },
                        closeMarkingCallback = {
                            viewModel.closeMarkingEvent()
                        },
                        searchCallback = {
                            viewModel.isSearching.value = true
                            viewModel.searchingTitleText.value = ""
                        },
                        sortCallback = {
                            openBottomSheet.value = true
                        },
                        deleteCallback = {
                            deleteDialog.value = true
                        },
                        closeSearchCallback = {
                            Log.d("pressed", "close pressed")
                            viewModel.closeSearchEvent()
                        }
                    )
                },
                snackbarHost = { SnackbarHost(snackBarHostState) },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = {
                            viewModel.closeMarkingEvent()
                            viewModel.closeSearchEvent()

                            navController.navigate(Route.NoteCreationPage.routeName)
                        },
                        modifier = Modifier.semantics {
                            testTag = TestTags.ADD_NOTE_FAB
                        },
                        text = {
                            Text(stringResource(R.string.add), fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = stringResource(R.string.fab)
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                },
                content = { contentPadding ->
                    Box(modifier = Modifier.padding(contentPadding)) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .semantics { testTag = TestTags.NOTE_RV },
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(
                                top = 24.dp,
                                bottom = 96.dp
                            )
                        ) {
                            items(items = filteredNoteListState.value) {
                                Box(Modifier.padding(start = 16.dp, end = 16.dp)) {
                                    NoteItem(
                                        isMarking = viewModel.isMarking.value,
                                        isMarked = it in viewModel.markedNoteList,
                                        data = it,
                                        onClick = {
                                            if (viewModel.isMarking.value) {
                                                viewModel.addToMarkNoteList(it)
                                            } else {
                                                viewModel.closeMarkingEvent()
                                                viewModel.closeSearchEvent()

                                                navController.navigate("${Route.NoteDetailPage.routeName}/${it.id}")
                                            }
                                        },
                                        onLongClick = {
                                            if (!viewModel.isMarking.value) {
                                                viewModel.isMarking.value = true
                                            }
                                            viewModel.addToMarkNoteList(it)
                                        },
                                        onCheckClick = {
                                            viewModel.addToMarkNoteList(it)
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.semantics { testTag = TestTags.NOTES_PAGE }
            )
        }
    )

    LoadingDialog(isOpened = loadingDialog.value, onDismissCallback = { loadingDialog.value = false },
        modifier = Modifier.semantics { testTag = TestTags.LOADING_DIALOG })

    TextDialog(isOpened = deleteDialog.value,
        onDismissCallBack = { deleteDialog.value = false },
        onConfirmCallBack = { deleteNoteList() },
        modifier = Modifier.semantics { testTag = TestTags.CONFIRMATION_DIALOG })


    FilterSheet(
        openBottomSheet = openBottomSheet,
        bottomSheetState = bottomSheetState,
        onDismiss = { openBottomSheet.value = false },
        onFilter = { sortBy, orderBy -> viewModel.sortAndOrder(sortBy, orderBy) }
    )

}

@Preview
@Composable
fun NotesPagePreview() {
    NotesPageScreen(
        navController = rememberNavController(),
        viewModel = NotesPageMockVM()
    )
}