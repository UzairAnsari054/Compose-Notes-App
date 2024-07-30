package com.example.composenotesapp1.data.local

import com.example.composenotesapp1.data.model.NoteModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteLocalService @Inject constructor(
    private val notesDao: NotesDao
) {

    suspend fun insertNote(note: NoteModel): Flow<Result<Boolean>> =
        flow {
            val result = notesDao.insertNoteWithTimeStamp(note) != -1L
            emit(Result.success(result))
        }.catch {
            emit(Result.failure(RuntimeException("Failed to insert list of notes")))
        }

    suspend fun deleteNotes(vararg notes: NoteModel): Flow<Result<Boolean>> =
        flow {
            val result = notesDao.deleteNotes(*notes) == notes.size
            emit(Result.success(result))
        }.catch {
            emit(Result.failure(RuntimeException("Failed to delete the list of notes")))
        }

    suspend fun updateNote(note: NoteModel): Flow<Result<Boolean>> =
        flow {
            val result = notesDao.updateNote(note) >= 1
            emit(Result.success(result))
        }.catch {
            emit(Result.failure(RuntimeException("Failed to update Note")))
        }

    suspend fun getNoteDetailsById(id: Int): Flow<Result<NoteModel>> =
        notesDao.getNoteDetailsById(id).map {
            Result.success(it)
        }.catch {
            emit(Result.failure(RuntimeException("Failed to get the note details")))
        }

    suspend fun getNotes(sortBy: String, orderBy: String): Flow<Result<List<NoteModel>>> =
        notesDao.getNotes(sortBy, orderBy).map {
            Result.success(it)
        }.catch {
            emit(Result.failure(RuntimeException("Failed to get notes")))
        }

}


