package com.example.learnlanguage.ui.word

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnlanguage.AppViewModelProvider
import com.example.learnlanguage.R
import com.example.learnlanguage.navigation.NavigationDestination
import com.example.learnlanguage.navigation.ViewMode
import com.example.learnlanguage.ui.MyTopAppBar

object ModeDestination : NavigationDestination {
    override val route = "mode"
    override val titleRes = R.string.change_mode_title
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ModeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
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

        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.low_pad))
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.low_pad)),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.low_pad))
            ) {
                Text(text = stringResource(id = R.string.notif_set))
                Switch(
                    checked = viewModel.uiState.showNotification,
                    onCheckedChange = viewModel::notificationSettings
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = viewModel.uiState.selectedMode == ViewMode.BOTH,
                    onClick = { viewModel.updateUiState( ViewMode.BOTH) }
                )
                Text(
                    text = stringResource(id = R.string.both),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected =  viewModel.uiState.selectedMode ==  ViewMode.ENGLISH,
                    onClick = { viewModel.updateUiState( ViewMode.ENGLISH)}
                )
                Text(
                    text = stringResource(id = R.string.only_english),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected =  viewModel.uiState.selectedMode == ViewMode.MONGOLIAN,
                    onClick = { viewModel.updateUiState( ViewMode.MONGOLIAN) }
                )
                Text(
                    text = stringResource(id = R.string.only_mongolian),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        viewModel.saveMode()
                        navigateBack()
                              },
                    enabled = viewModel.uiState.isEnabled,
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
}