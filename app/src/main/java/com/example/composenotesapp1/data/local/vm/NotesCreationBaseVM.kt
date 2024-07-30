package com.example.composenotesapp1.data.local.vm

import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import com.example.composenotesapp1.data.model.NoteModel

interface NotesCreationBaseVM {

    val loader:  MutableLiveData<Boolean>
    val titleText: MutableState<String>
    val descriptionText: MutableState<String>

    suspend fun addNotes(note: NoteModel): Result<Boolean>
}