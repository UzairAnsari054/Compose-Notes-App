package com.example.composenotesapp1.data.local

import com.example.composenotesapp1.data.model.NoteModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteLocalService: NoteLocalService
) {

    suspend fun insertNote(note: NoteModel): Flow<Result<Boolean>> =
        noteLocalService.insertNote(note).map {
            if (it.isSuccess) {
                Result.success(it.getOrNull() ?: false)
            } else {
                Result.failure(it.exceptionOrNull()!!)
            }
        }


    suspend fun deleteNotes(vararg notes: NoteModel): Flow<Result<Boolean>> =
        noteLocalService.deleteNotes(*notes).map {
            if (it.isSuccess) {
                Result.success(it.getOrNull() ?: false)
            } else {
                Result.failure(it.exceptionOrNull()!!)
            }
        }

    suspend fun updateNote(note: NoteModel): Flow<Result<Boolean>> =
        noteLocalService.updateNote(note).map {
            if (it.isSuccess) {
                Result.success(it.getOrNull() ?: false)
            } else {
                Result.failure(it.exceptionOrNull()!!)
            }
        }

    suspend fun getNoteDetailsById(id: Int): Flow<Result<NoteModel>> =
        noteLocalService.getNoteDetailsById(id).map {
            if (it.isSuccess) {
                Result.success(it.getOrNull() ?: NoteModel(1, "", ""))
            } else {
                Result.failure(it.exceptionOrNull()!!)
            }
        }

    suspend fun getNotes(sortBy: String, orderBy: String): Flow<Result<List<NoteModel>>> =
        noteLocalService.getNotes(sortBy, orderBy).map {
            if (it.isSuccess) {
                Result.success(it.getOrNull() ?: listOf())
            } else {
                Result.failure(it.exceptionOrNull()!!)
            }
        }

}

/*
    suspend fun insertNote(note: NoteModel): Flow<Result<Boolean>> =
        noteLocalService.insertNote(note).map {
            if (it.isSuccess){
                Result.success(it.getOrNull() ?: false)
            } else {
                Result.failure(it.exceptionOrNull()!!)
            }
        }
}*/
