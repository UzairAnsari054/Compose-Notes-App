package com.example.composenotesapp1.ui.notecreation

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
class NotesCreationVM @Inject constructor(
    private val repository: NoteRepository
) : ViewModel(), NotesCreationBaseVM {
    override val loader: MutableLiveData<Boolean> = MutableLiveData()
    override val titleText: MutableState<String> = mutableStateOf("")
    override val descriptionText: MutableState<String> = mutableStateOf("")


    override suspend fun addNotes(note: NoteModel): Result<Boolean> = withContext(Dispatchers.IO) {
        loader.postValue(true)
        try {
            repository.insertNote(note).onEach {
                loader.postValue(false)
            }.last()
        } catch (e: Exception) {
            loader.postValue(false)
            Result.failure(e)
        }
    }


}