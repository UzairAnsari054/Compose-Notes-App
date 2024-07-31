package com.example.composenotesapp1.ui.vm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import com.example.composenotesapp1.data.model.NoteModel

interface NotesDetailsPageBaseVm {

    val loader: MutableLiveData<Boolean>

    val markedNoteList: SnapshotStateList<NoteModel>

    val noteDetails: MutableLiveData<Result<NoteModel>>

    val titleText: MutableState<String>
    val descriptionText: MutableState<String>
    val isEditing: MutableState<Boolean>


    suspend fun deleteNotes(vararg notes: NoteModel): Result<Boolean>
    suspend fun updateNotes(note: NoteModel): Result<Boolean>
    suspend fun getNoteById(id: Int)
}