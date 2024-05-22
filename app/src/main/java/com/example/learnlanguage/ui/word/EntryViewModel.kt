package com.example.learnlanguage.ui.word

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.learnlanguage.data.Word
import com.example.learnlanguage.data.WordRepository

class EntryViewModel(
    private val wordRepository: WordRepository
) : ViewModel() {
    var wordUiState by mutableStateOf(WordUiState())

    fun updateUiState(wordDetail: Word) {
        wordUiState =
            WordUiState(wordDetail = wordDetail, isEntryValid = validateInput())
    }

    suspend fun saveWord() {
        if(validateInput()){
            wordRepository.insertWord(wordUiState.wordDetail)
        }
    }

    private fun validateInput(uiState: Word = wordUiState.wordDetail) : Boolean {
        return with(uiState) {
            mongolian.isNotBlank() && english.isNotBlank()
        }
    }
}

data class WordUiState(
    val wordDetail: Word = Word(),
    val isEntryValid: Boolean = false
)