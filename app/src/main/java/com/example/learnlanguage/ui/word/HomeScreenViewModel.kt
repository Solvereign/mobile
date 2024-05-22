package com.example.learnlanguage.ui.word

//import androidx.compose.runtime.collectAsState
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.learnlanguage.data.Word
//import com.example.learnlanguage.data.WordRepository
//import com.example.learnlanguage.navigation.ViewMode
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.stateIn
//
//class ListScreenViewModel(wordRepository: WordRepository) : ViewModel() {
//
//    val words: StateFlow<WordList> =
//        wordRepository.getAllWordsStream().map { WordList(it) }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = WordList()
//            )
//
//    fun getItemCount(): Int {
//        return words.value.wordList.size
//    }
//
//    val len = words.value.wordList.lastIndex
//    companion object {
//        private const val TIMEOUT_MILLIS = 5_000L
//    }
//
//    var iterator = 0
//
//    var homeUiState = if(words.value.wordList.isEmpty()) HomeUiState(wordExist = false)
//        else HomeUiState(
//            word = words.value.wordList[iterator],
//            wordExist = true,
//            prevExist = false,
//            nextExist = words.value.wordList.size > 1
//        )
//
//    // daraagiin ug baihad l ajillah bolomjtoi
//    fun changeWord(change : Int) {
//        iterator += if(change > 0) 1 else -1
//
//        homeUiState =HomeUiState(
//            word = words.value.wordList[iterator],
//            wordExist = true,
//            nextExist = words.value.wordList.size > iterator + 1,
//            prevExist = iterator > 0
//        )
//    }
//
//    fun changeViewMode(mode: ViewMode) {
//
//    }
//
//}

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnlanguage.data.UserPreferenceRepository
import com.example.learnlanguage.data.Word
import com.example.learnlanguage.data.WordRepository
import com.example.learnlanguage.navigation.ViewMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ListScreenViewModel(
    private val wordRepository: WordRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    private val _words: MutableStateFlow<List<Word>> = MutableStateFlow(emptyList())
//    val words: StateFlow<List<Word>> = _words.asStateFlow()

    private val _homeUiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    val selectedMode : StateFlow<ViewMode> =
        userPreferenceRepository.viewMode
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ViewMode.BOTH
            )

    init {
        viewModelScope.launch {
            wordRepository.getAllWordsStream()
                .collect { words ->
                    _words.value = words
                    updateHomeUiState()
                }
            selectedMode.collect {
                updateHomeUiState()
            }
        }
    }


//    private fun updateHomeUiState() {
//        val currentWords = _words.value
//        var currentIndex = currentWords.indexOf(homeUiState.value.word)
//        if(currentIndex == -1 && currentWords.size > 0) {
//            currentIndex = 0
//            _homeUiState.value = _homeUiState.value.copy(
//                word = currentWords[0],
//                nextExist = currentIndex < currentWords.size - 1,
//                prevExist = false,
//                wordExist = true,
//                viewMode = selectedMode.value
//            )
//            return
//        }
//
//        val nextExist = currentIndex < currentWords.size - 1
//        val prevExist = currentIndex > 0
//
//        val newUiState = HomeUiState(
//            word = homeUiState.value.word,
//            wordExist = currentWords.isNotEmpty(),
//            nextExist = nextExist,
//            prevExist = prevExist
//        )
//        _homeUiState.value = newUiState
//    }

    private fun updateHomeUiState() {
        val currentWords = _words.value
        val currentHomeUiState = _homeUiState.value

        if (currentWords.isNotEmpty()) {
            val currentIndex = currentWords.indexOf(currentHomeUiState.word)
            val nextExist = currentIndex < currentWords.size - 1
            val prevExist = currentIndex > 0

            val newWord = currentHomeUiState.word.takeIf { currentWords.contains(it) } ?: currentWords.first()
            val newUiState = currentHomeUiState.copy(
                word = newWord,
                wordExist = true,
                nextExist = nextExist,
                prevExist = prevExist,
                viewMode = selectedMode.value
            )
            _homeUiState.value = newUiState
        } else {
            _homeUiState.value = currentHomeUiState.copy(
                wordExist = false,
                nextExist = false,
                prevExist = false,
                viewMode = selectedMode.value
            )
        }
    }

    fun changeWord(change: Int) {
        val currentWords = _words.value
        val currentIndex = currentWords.indexOf(homeUiState.value.word)
        val newIndex = (currentIndex + change).coerceIn(0, currentWords.size - 1)

        _homeUiState.value = _homeUiState.value.copy(
            word = currentWords[newIndex],
            nextExist = newIndex < currentWords.size - 1,
            prevExist = newIndex > 0
        )
    }

    fun changeViewMode(mode: ViewMode) {
        if(selectedMode.value == mode) {
            viewModelScope.launch {
                userPreferenceRepository.saveViewMode(ViewMode.BOTH.name)
            }
        }
    }

    fun delete(word: Word) {
        if(_words.value.size == 1) {
            _homeUiState.value = _homeUiState.value.copy(
                word = Word(),
                nextExist = false,
                prevExist = false
            )
        }
        viewModelScope.launch {
            wordRepository.deleteWord(word)
        }
    }
}

data class HomeUiState(
    val word : Word = Word(),
    val wordExist: Boolean = true,
    val nextExist: Boolean = false,
    val prevExist: Boolean = false,
    val viewMode: ViewMode = ViewMode.BOTH
)