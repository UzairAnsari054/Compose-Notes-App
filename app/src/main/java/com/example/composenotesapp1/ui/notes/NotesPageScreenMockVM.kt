package com.example.composenotesapp1.ui.notes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.composenotesapp1.data.model.NoteModel

class NotesPageMockVM(
) : ViewModel(), NotesPageBaseVm {
    override val loader = MutableLiveData<Boolean>()
    override val sortAndOrderData: MutableLiveData<Pair<String, String>> = MutableLiveData()

    override fun sortAndOrder(sortBy: String, orderBy: String) {

    }

    override val noteList: LiveData<Result<List<NoteModel>>> = liveData {
        Result.success(
            listOf(
                NoteModel("", ""),
                NoteModel("", ""),
                NoteModel("", ""),
                NoteModel("", ""),
            )
        )
    }

    override val isSearching = mutableStateOf(false)
    override val searchingTitleText = mutableStateOf("")

    override val isMarking = mutableStateOf(false)
    override val markedNoteList = mutableStateListOf<NoteModel>()

    override fun markAllNote(notes: List<NoteModel>) {}

    override fun unMarkAllNote() {}

    override fun addToMarkNoteList(note: NoteModel) {}

    override suspend fun deleteNoteList(vararg notes: NoteModel): Result<Boolean> = Result.success(true)

    override fun closeMarkingEvent() {
        isMarking.value = false
        markedNoteList.clear()
    }

    override fun closeSearchEvent() {
        isSearching.value = false
        searchingTitleText.value = ""
    }
}