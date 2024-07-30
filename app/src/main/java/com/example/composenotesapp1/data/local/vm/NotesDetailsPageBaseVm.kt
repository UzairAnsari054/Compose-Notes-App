package com.example.composenotesapp1.data.local.vm

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import com.example.composenotesapp1.data.model.NoteModel

interface NotesDetailsPageBaseVm {

    val loader: MutableLiveData<Boolean>

    val markedNoteList: SnapshotStateList<NoteModel>

    val noteDetails: MutableLiveData<Result<NoteModel>>


    suspend fun deleteNotes(vararg notes: NoteModel): Result<Boolean>
    suspend fun updateNotes(note: NoteModel): Result<Boolean>
    suspend fun getNoteById(id: Int)
}