package com.example.composenotesapp1.ui.noteDetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composenotesapp1.data.local.NoteRepository
import com.example.composenotesapp1.data.model.NoteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotesDetailsPageVM @Inject constructor(
    private val repository: NoteRepository
) : ViewModel(), NotesDetailsPageBaseVm {

    override val loader = MutableLiveData<Boolean>()
    override val noteDetails: MutableLiveData<Result<NoteModel>> = MutableLiveData()

    override val titleText: MutableState<String> = mutableStateOf("")
    override val descriptionText: MutableState<String> = mutableStateOf("")
    override val isEditing: MutableState<Boolean> = mutableStateOf(false)

    override suspend fun deleteNotes(vararg notes: NoteModel): Result<Boolean> =
        withContext(Dispatchers.IO) {
            loader.postValue(true)
            try {
                repository.deleteNotes(*notes).onEach {
                    loader.postValue(false)
                }.last()
            } catch (e: Exception) {
                loader.postValue(false)
                Result.failure(e)
            }
        }

    override suspend fun updateNotes(note: NoteModel): Result<Boolean> =
        withContext(Dispatchers.IO) {
            loader.postValue(true)
            try {
                repository.updateNote(note).onEach { loader.postValue(false) }.last()
            } catch (e: Exception) {
                loader.postValue(false)
                Result.failure(e)
            }
        }

    override suspend fun getNoteById(id: Int) =
        withContext(Dispatchers.IO) {
            loader.postValue(true)
            repository.getNoteDetailsById(id)
                .onEach { loader.postValue(false) }
                .collect { noteDetails.postValue(it) }

        }
}