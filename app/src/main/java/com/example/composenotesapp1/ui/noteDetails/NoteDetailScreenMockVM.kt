package com.example.composenotesapp1.ui.noteDetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composenotesapp1.data.model.NoteModel

class NoteDetailScreenMockVM: ViewModel(), NotesDetailsPageBaseVm {
    override val loader: MutableLiveData<Boolean> = MutableLiveData()
    override val noteDetails: MutableLiveData<Result<NoteModel>> = MutableLiveData()

    override var titleText: MutableState<String> = mutableStateOf("This is sample title text")
    override var descriptionText: MutableState<String> = mutableStateOf("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus pretium odio maximus tellus pellentesque, a dignissim massa commodo.\n")
    override var isEditing: MutableState<Boolean> = mutableStateOf(false)


    override suspend fun getNoteById(id: Int) {}
    override suspend fun updateNotes(note: NoteModel): Result<Boolean> = Result.success(true)
    override suspend fun deleteNotes(vararg notes: NoteModel): Result<Boolean> = Result.success(true)
}