package com.example.composenotesapp1.ui.notes

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.composenotesapp1.commons.Constants
import com.example.composenotesapp1.data.local.NoteRepository
import com.example.composenotesapp1.data.model.NoteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotesPageVm @Inject constructor(
    private val repository: NoteRepository
) : ViewModel(), NotesPageBaseVm {
    override val loader = MutableLiveData<Boolean>()

    override val sortAndOrderData: MutableLiveData<Pair<String, String>> =
        MutableLiveData(Pair(Constants.CREATED_AT, Constants.DESCENDING))

    override fun sortAndOrder(sortBy: String, orderBy: String) {
        sortAndOrderData.value = Pair(sortBy, orderBy)
    }

    override val noteList: LiveData<Result<List<NoteModel>>> = sortAndOrderData.switchMap {
        liveData {
            loader.postValue(true)
            try {
                emitSource(repository.getNotes(it.first, it.second)
                    .onEach { loader.postValue(false) }
                    .asLiveData())
            } catch (e: Exception) {
                loader.postValue(false)
                emit(Result.failure(e))
            }
        }
    }

    override val isSearching = mutableStateOf(false)
    override val searchingTitleText = mutableStateOf("")

    override val isMarking = mutableStateOf(false)
    override val markedNoteList = mutableStateListOf<NoteModel>()

    override fun markAllNote(notes: List<NoteModel>) {
        markedNoteList.addAll(notes.minus(markedNoteList.toSet()))
    }

    override fun unMarkAllNote() {
        markedNoteList.clear()
    }

    override fun addToMarkNoteList(note: NoteModel) {
        if (note in markedNoteList) {
            markedNoteList.remove(note)
        } else {
            markedNoteList.add(note)
        }
    }

    override suspend fun deleteNoteList(vararg notes: NoteModel): Result<Boolean> =
        withContext(Dispatchers.IO) {
            loader.postValue(true)
            try {
                val items: List<NoteModel> = if (notes.isEmpty()) markedNoteList else notes.toList()
                repository.deleteNotes(*items.toTypedArray())
                    .onEach { loader.postValue(false) }
                    .last()
            } catch (e: Exception) {
                loader.postValue(false)
                Result.failure(e)
            }
        }

    override fun closeMarkingEvent() {
        isMarking.value = false
        markedNoteList.clear()
    }


    override fun closeSearchEvent() {
        isSearching.value = false
        searchingTitleText.value = ""
    }

}