package com.example.composenotesapp1.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.composenotesapp1.data.model.NoteModel
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface NotesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(noteModel: NoteModel): Long

    fun insertNoteWithTimeStamp(noteModel: NoteModel): Long {
        return insertNote(noteModel.apply {
            createdAt = Date(System.currentTimeMillis())
            updatedAt = Date(System.currentTimeMillis())
        })
    }

    @Delete
    fun deleteNotes(vararg notes: NoteModel): Int

    @Update
    fun updateNote(note: NoteModel): Int

    fun updateNoteWithTimeStamp(note: NoteModel): Int {
        return updateNote(note.apply {
            updatedAt = Date(System.currentTimeMillis())
        })
    }

    @Query("SELECT * FROM noteTable WHERE id=:id")
    fun getNoteDetailsById(id: Int): Flow<NoteModel>

    fun getNotes(sortBy: String, orderBy: String): Flow<List<NoteModel>>

}
