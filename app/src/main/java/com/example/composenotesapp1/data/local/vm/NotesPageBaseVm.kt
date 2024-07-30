package com.example.composenotesapp1.data.local.vm

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composenotesapp1.data.model.NoteModel

interface NotesPageBaseVm {

    val loader: MutableLiveData<Boolean>

    val sortAndOrderData: MutableLiveData<Pair<String, String>>

    val noteList: LiveData<Result<List<NoteModel>>>

    fun sortAndOrder(sortBy: String, orderBy: String)

    val markedNoteList: SnapshotStateList<NoteModel>

    fun markAllNote(notes: List<NoteModel>)

    fun unMarkAllNote()

    fun addToMarkNoteList(note: NoteModel)

    suspend fun deleteNoteList(vararg notes: NoteModel): Result<Boolean>

    val isMarking: MutableState<Boolean>
    fun clearMarkingEvent()

    val isSearching: MutableState<Boolean>
    val searchingTitleText: MutableState<String>
    fun clearSearchEvent()
}
