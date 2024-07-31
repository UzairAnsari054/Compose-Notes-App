package com.example.composenotesapp1.data.local.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.composenotesapp1.commons.Constants.GLOBAL_PREFERENCE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteDataStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(GLOBAL_PREFERENCE)
    }

     fun getStringData(key: String): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[getStringKey(key)] ?: ""
        }
    }

    suspend fun setStringData(key: String, data: String) {
        context.dataStore.edit { preferences ->
            preferences[getStringKey(key)] = data
        }
    }

    private fun getStringKey(key: String): Preferences.Key<String> {
        return stringPreferencesKey(key)
    }
}