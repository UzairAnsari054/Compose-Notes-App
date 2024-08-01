package com.example.composenotesapp1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.composenotesapp1.data.model.NoteModel
import com.example.composenotesapp1.share_preview.SharePreviewPage
import com.example.composenotesapp1.ui.noteDetails.NoteDetailsScreen
import com.example.composenotesapp1.ui.notecreation.NoteCreationPage
import com.example.composenotesapp1.ui.notes.NotesPageScreen


@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Route.NotesPage.routeName
    ) {
        composable(Route.NotesPage.routeName) {
            NotesPageScreen(navController = navHostController)
        }
        composable(
            route = "${Route.NoteDetailPage.routeName}/{noteId}",
            arguments = listOf(navArgument("noteId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            NoteDetailsScreen(
                navController = navHostController,
                id = it.arguments?.getString("noteId") ?: "0"
            )
        }
        composable(Route.NoteCreationPage.routeName) {
            NoteCreationPage(navController = navHostController)
        }
        composable(
            route = "${Route.SharePreviewPage.routeName}/{noteData}",
            arguments = listOf(navArgument("noteData") {
                type = NoteModelParamType()
            })
        ) {
            val note = it.arguments?.getParcelable<NoteModel>("noteData")
            SharePreviewPage(navHostController = navHostController, note = note)
        }
    }
}