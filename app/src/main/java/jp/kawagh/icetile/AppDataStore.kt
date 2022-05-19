package jp.kawagh.icetile

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("setting")
        val KEY = booleanPreferencesKey("is_first_play")
    }

    val getValue: Flow<Boolean> = context.dataStore.data.map { it[KEY] ?: true }

    suspend fun saveFirstPlay() {
        context.dataStore.edit { it[KEY] = false }
    }
}
