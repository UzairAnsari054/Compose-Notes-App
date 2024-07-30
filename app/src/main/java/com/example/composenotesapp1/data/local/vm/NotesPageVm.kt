package com.example.composenotesapp1.data.local.vm

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
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NotesPageVm @Inject constructor(
    private val repository: NoteRepository
) : ViewModel(), NotesPageBaseVm {
    override val loader = MutableLiveData<Boolean>()

    override val sortAndOrderData: MutableLiveData<Pair<String, String>> =
        MutableLiveData(Pair(Constants.CREATED_AT, Constants.DESCENDING))

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


}