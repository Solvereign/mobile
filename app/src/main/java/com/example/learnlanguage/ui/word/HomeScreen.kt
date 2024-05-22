package com.example.learnlanguage.ui.word

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnlanguage.AppViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnlanguage.navigation.NavigationDestination
import com.example.learnlanguage.ui.MyTopAppBar
import com.example.learnlanguage.R
import com.example.learnlanguage.data.Word
import com.example.learnlanguage.navigation.ViewMode

object HomeDestination: NavigationDestination {
    override val route: String = "home"
    override val titleRes: Int = R.string.home_title
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navigateToWordEntry: () -> Unit,
    navigateToWordUpdate: (Int) -> Unit,
    navigateToMode: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState = viewModel.homeUiState.collectAsState()
    val selectedMode = viewModel.selectedMode.collectAsState()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MyTopAppBar(
                title = stringResource(id = HomeDestination.titleRes) ,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(
                        onClick = { navigateToMode() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {innerPadding ->
            HomeBody(
                word = homeUiState.value.word,
//                wordExist = viewModel.homeUiState.wordExist,
                wordExist = homeUiState.value.wordExist,
//                nextExist = viewModel.homeUiState.nextExist,
                nextExist = homeUiState.value.nextExist,
//                prevExist = viewModel.homeUiState.prevExist,
                prevExist = homeUiState.value.prevExist,
                navigateToWordEntry = navigateToWordEntry,
                navigateToWordUpdate = navigateToWordUpdate,
//                viewMode = viewModel.homeUiState.viewMode,
//                viewMode = homeUiState.value.viewMode,
                viewMode = selectedMode.value,
                changeShow = viewModel::changeViewMode,
                changeWord = viewModel::changeWord,
                onDelete = viewModel::delete,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )


    }
}

@Composable
fun HomeBody(
    word: Word,
    wordExist: Boolean,
    nextExist: Boolean,
    prevExist: Boolean,
    navigateToWordUpdate: (Int) -> Unit,
    navigateToWordEntry: () -> Unit,
    changeWord: (Int) -> Unit,
    changeShow: (ViewMode) -> Unit,
    viewMode: ViewMode,
    onDelete: (Word) -> Unit,
    modifier: Modifier = Modifier
){
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.weight(6f))
        Text(
            text = if(viewMode == ViewMode.BOTH || viewMode == ViewMode.ENGLISH) word.english else "",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 36.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
                .height(dimensionResource(id = R.dimen.word_text_height))
                .background(MaterialTheme.colorScheme.primary)
                .clickable { changeShow(ViewMode.MONGOLIAN) }
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = if(viewMode == ViewMode.BOTH || viewMode == ViewMode.MONGOLIAN) word.mongolian else "",
            color = MaterialTheme.colorScheme.onPrimary,
//            style = MaterialTheme.typography.labelLarge,
            fontSize = 36.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
                .height(dimensionResource(id = R.dimen.word_text_height))
                .background(MaterialTheme.colorScheme.primary)
                .clickable { changeShow(ViewMode.ENGLISH) }
        )
        Spacer(modifier = Modifier.weight(3f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.low_pad))
        ) {
            Button(
                onClick = navigateToWordEntry,
                modifier = Modifier.weight(2f)
            ) {
                Text(text = stringResource(id = R.string.add))
            }
            Button(
                onClick = { navigateToWordUpdate(word.id) },
                enabled = wordExist,
                modifier = Modifier.weight(3f)
            ) {
                Text(text = stringResource(id = R.string.update))
            }
            Button(
                onClick = { deleteConfirmationRequired = true },
                enabled = wordExist,
                modifier = Modifier.weight(2f)
            ) {
                Text(text = stringResource(id = R.string.delete))
            }
            
        }
        Spacer(modifier = Modifier.weight(2f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.lowest_pad))
        ) {
            Button(
                onClick = { changeWord(-1) },
                enabled = prevExist,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(id = R.string.prev))
            }
            Button(
                onClick = { changeWord(1) },
                enabled = nextExist,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(id = R.string.next))
            }

        }
        Spacer(modifier = Modifier.weight(1f))
        if(deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete(word)
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.low_pad))
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        })
}


@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun HomeBodyPreview() {
    HomeBody(word = Word(0, "hello", "сайн уу"), wordExist = true, nextExist = false, prevExist = true,
        {}, {}, {}, {}, viewMode = ViewMode.BOTH, {} )
}