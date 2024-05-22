package com.example.learnlanguage.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.learnlanguage.worker.NotificationRepository

interface AppContainer {
    val wordRepository: WordRepository
    val userPreferenceRepository: UserPreferenceRepository
    val notificationRepository: NotificationRepository
}
class MyContainer(
    private val context: Context,
    private val dataStore: DataStore<Preferences>
) : AppContainer {

    override val wordRepository: WordRepository by lazy {
        MyWordRepository(WordDatabase.getDatabase(context).wordDao())
    }

    override val userPreferenceRepository: UserPreferenceRepository by lazy {
        UserPreferenceRepository(dataStore)
    }

    override val notificationRepository: NotificationRepository by lazy {
        NotificationRepository(context)
    }
}