package com.example.composenotesapp1.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.composenotesapp1.data.model.NoteModel
import java.util.Date

object DateConverters {
    @TypeConverter
    fun fromDateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTimeStampToDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }
}

@Database(
    entities = [NoteModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverters::class)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}