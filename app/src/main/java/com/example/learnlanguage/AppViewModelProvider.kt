package com.example.learnlanguage

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.learnlanguage.ui.word.EditScreenViewModel
import com.example.learnlanguage.ui.word.EntryViewModel
import com.example.learnlanguage.ui.word.ListScreenViewModel
import com.example.learnlanguage.ui.word.ModeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            ListScreenViewModel(
                wordApplication().container.wordRepository,
                wordApplication().container.userPreferenceRepository
            )
        }

        initializer {
            EntryViewModel(wordApplication().container.wordRepository)
        }

        initializer {
            EditScreenViewModel(
                this.createSavedStateHandle(),
                wordApplication().container.wordRepository)
        }

        initializer {
            ModeViewModel(
                wordApplication().container.userPreferenceRepository,
                wordApplication().container.notificationRepository
            )
        }


    }
}

fun CreationExtras.wordApplication() : WordApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WordApplication)