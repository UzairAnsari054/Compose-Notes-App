package com.example.composenotesapp1.ui.notecreation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composenotesapp1.data.model.NoteModel

class NoteCreationPageMockVM: ViewModel(), NotesCreationBaseVM {
    override val loader: MutableLiveData<Boolean> = MutableLiveData(false)
    override val titleText: MutableState<String> = mutableStateOf("")
    override val descriptionText: MutableState<String> = mutableStateOf("")

    override suspend fun addNotes(note: NoteModel): Result<Boolean> {
        TODO("Not yet implemented")
    }

}