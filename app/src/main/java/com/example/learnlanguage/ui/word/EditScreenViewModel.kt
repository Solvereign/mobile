package com.example.learnlanguage.ui.word

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnlanguage.data.Word
import com.example.learnlanguage.data.WordRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val wordRepository: WordRepository
) : ViewModel() {

    var wordUiState by mutableStateOf(WordUiState())
        private set

    private val wordId: Int = checkNotNull(savedStateHandle[EditDestination.idArg])

    private fun validateInput(uiState: Word = wordUiState.wordDetail) : Boolean {
        return with(uiState) {
            mongolian.isNotBlank() && english.isNotBlank()
        }
    }

    init {
        viewModelScope.launch {
            wordUiState = wordRepository.getWordStream(wordId)
                .filterNotNull()
                .first()
                .toWordUiState()

        }
    }

    fun updateUiState(wordDetail: Word) {
        wordUiState =
            WordUiState(wordDetail = wordDetail, isEntryValid = validateInput())
    }

    fun updateWord() {
        if(validateInput(wordUiState.wordDetail)) {
            viewModelScope.launch{
                wordRepository.updateWord(wordUiState.wordDetail)
            }
        }
    }
}

fun Word.toWordUiState(isEntryValid: Boolean = false) : WordUiState  =
    WordUiState(this, isEntryValid)