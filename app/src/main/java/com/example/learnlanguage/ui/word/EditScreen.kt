package com.example.learnlanguage.ui.word

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnlanguage.AppViewModelProvider
import com.example.learnlanguage.R
import com.example.learnlanguage.navigation.NavigationDestination
import com.example.learnlanguage.ui.MyTopAppBar

object EditDestination : NavigationDestination {
    override val route = "edit"
    override val titleRes = R.string.edit_word_title
    val idArg = "wordId"
    val routeWithArg = "$route/{${idArg}}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier : Modifier = Modifier,
    viewModel: EditScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = stringResource(id = EditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) {innerPadding ->
        WordEntryBody(
            wordUiState = viewModel.wordUiState,
            onUiChange = viewModel::updateUiState,
            onSaveClick = {
                viewModel.updateWord()
                navigateBack()
            },
            navigateBack = navigateBack,
            modifier = Modifier.padding(innerPadding)
        )

    }
}