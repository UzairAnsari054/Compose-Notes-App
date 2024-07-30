package com.example.composenotesapp1.data.local.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composenotesapp1.data.model.NoteModel

interface NotesPageBaseVm {

    val loader: MutableLiveData<Boolean>

    val sortAndOrderData: MutableLiveData<Pair<String, String>>

    val noteList: LiveData<Result<List<NoteModel>>>
}
