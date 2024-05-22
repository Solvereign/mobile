package com.example.learnlanguage.data

import kotlinx.coroutines.flow.Flow

interface WordRepository {

    fun getAllWordsStream() : Flow<List<Word>>

    fun getWordStream(id: Int) : Flow<Word?>

    suspend fun insertWord(word: Word)

    suspend fun deleteWord(word: Word)

    suspend fun updateWord(word: Word)
}