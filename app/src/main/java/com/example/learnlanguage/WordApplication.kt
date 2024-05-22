package com.example.learnlanguage

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.learnlanguage.data.AppContainer
import com.example.learnlanguage.data.MyContainer
import com.example.learnlanguage.data.UserPreferenceRepository

private const val VIEWMODE_PREFERENCE_NAME = "viewMode_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = VIEWMODE_PREFERENCE_NAME
)
class WordApplication : Application() {

    lateinit var container: AppContainer

//    lateinit var userPreferenceRepository: UserPreferenceRepository

    override fun onCreate() {
        super.onCreate()
        container = MyContainer(this, dataStore)
//        userPreferenceRepository = UserPreferenceRepository(dataStore)
    }
}