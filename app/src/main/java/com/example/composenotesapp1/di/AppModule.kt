package com.example.composenotesapp1.di

import android.content.Context
import androidx.room.Room
import com.example.composenotesapp1.data.local.NotesDao
import com.example.composenotesapp1.data.local.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        NotesDatabase::class.java,
        "note_db"
    ).build()

    @Provides
    @Singleton
    fun provideNoteDao(db: NotesDatabase): NotesDao {
        return db.notesDao()
    }
}