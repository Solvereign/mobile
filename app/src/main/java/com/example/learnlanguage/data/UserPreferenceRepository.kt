package com.example.learnlanguage.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.learnlanguage.navigation.ViewMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferenceRepository(
    private val dataStore: DataStore<Preferences>
) {
    private val VIEW_MODE = stringPreferencesKey("view_mode")
    private val SHOW_NOTIFICATION = booleanPreferencesKey("show_notification")

    suspend fun saveViewMode(viewMode: String) {
        dataStore.edit { preferences ->
            preferences[VIEW_MODE] = viewMode
        }
    }

    suspend fun saveShowNotification(notif: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_NOTIFICATION] = notif
        }
    }

    val viewMode: Flow<ViewMode> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e("Hello", "Error reading preferences. ", it)
                emit(emptyPreferences())
            }
            else {
                Log.i("Hello", "wot")
                throw it
            }
        }
        .map { preferences ->
        when(preferences[VIEW_MODE]) {
            ViewMode.MONGOLIAN.name -> ViewMode.MONGOLIAN
            ViewMode.ENGLISH.name -> ViewMode.ENGLISH
            else -> ViewMode.BOTH
        }
    }
    val showNotification: Flow<Boolean> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e("Hello", "Error reading preferences. ", it)
                emit(emptyPreferences())
            }
            else {
                Log.i("Hello", "woot")
                throw it
            }
        }
        .map { preferences ->
            preferences[SHOW_NOTIFICATION] ?: false
        }
}