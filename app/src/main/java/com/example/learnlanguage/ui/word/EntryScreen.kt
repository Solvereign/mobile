package com.example.learnlanguage.ui.word

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnlanguage.AppViewModelProvider
import com.example.learnlanguage.R
import com.example.learnlanguage.data.Word
import com.example.learnlanguage.navigation.NavigationDestination
import com.example.learnlanguage.ui.MyTopAppBar
import kotlinx.coroutines.launch


object EntryScreenDestination : NavigationDestination {
    override val route = "wordEntry"
    override val titleRes = R.string.add_word_title
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: EntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar ={
            MyTopAppBar(
                title = stringResource(id = EntryScreenDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) {innerPadding ->
        WordEntryBody(
            wordUiState = viewModel.wordUiState,
            onUiChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveWord()
                    navigateBack()
                }
            },
            navigateBack = navigateBack,
            modifier = Modifier.padding(innerPadding)
        )

    }
}

@Composable
fun WordEntryBody(
    wordUiState: WordUiState,
    onUiChange: (Word) -> Unit,
    onSaveClick: () -> Unit,
    navigateBack: () -> Unit,
    modifier : Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.lowest_pad)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.low_pad))
    ) {
        WordInputForm(
            wordDetails = wordUiState.wordDetail,
            onValueChange = onUiChange,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onSaveClick,
                enabled = wordUiState.isEntryValid,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(6f)
            ) {
                Text(text = stringResource(R.string.insert))
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = navigateBack,
                enabled = true,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(6f)
            ) {
                Text(text = stringResource(R.string.cancel))
            }

        }
    }
}

@Composable
fun WordInputForm(
    wordDetails: Word,
    onValueChange: (Word) -> Unit,
    modifier : Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = wordDetails.english,
            placeholder = {Text(stringResource(id = R.string.foreign_word))},
            onValueChange = {onValueChange(wordDetails.copy(english = it))},
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = false
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.custom_pad)))
        TextField(
            value = wordDetails.mongolian,
            placeholder = {Text(stringResource(id = R.string.mongolian_word))},
            onValueChange = {onValueChange(wordDetails.copy(mongolian = it))},
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = false
        )

    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun InputPreview() {
    WordEntryBody(
        wordUiState = WordUiState(wordDetail = Word(0, "love", "хайр")),
        {}, { }, {}
    )
}