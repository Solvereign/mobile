package com.example.learnlanguage.ui.word

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnlanguage.data.UserPreferenceRepository
import com.example.learnlanguage.navigation.ViewMode
import com.example.learnlanguage.worker.NotificationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ModeViewModel (
    private val userPreferenceRepository: UserPreferenceRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    private val selectedMode : StateFlow<ViewMode> =
        userPreferenceRepository.viewMode
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ViewMode.BOTH
            )

    private val showNotification: StateFlow<Boolean> =
        userPreferenceRepository.showNotification
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    var uiState: ChangeViewUiState by mutableStateOf(ChangeViewUiState(ViewMode.BOTH, false))
        private set // Make the setter private to prevent external modification

    init {
        // Observe changes to selectedMode and update uiState accordingly
        viewModelScope.launch {
            selectedMode.collect { mode ->
                uiState = ChangeViewUiState(selectedMode = mode, isEnabled = mode != selectedMode.value)
            }
        }
        viewModelScope.launch {
            showNotification.collect{notif ->
                uiState = uiState.copy(showNotification=notif)
            }
        }
    }

    fun saveMode(mode: ViewMode = uiState.selectedMode) {
        viewModelScope.launch {
            userPreferenceRepository.saveViewMode(mode.name)
        }
    }

    fun notificationSettings(notif: Boolean = !uiState.showNotification) {
        viewModelScope.launch {
            userPreferenceRepository.saveShowNotification(notif)
        }
        if(notif) {
            notificationRepository.scheduleNotification()
        }
        else {
            notificationRepository.cancelNotification()
        }
    }

    fun updateUiState(mode: ViewMode) {
        uiState = ChangeViewUiState(selectedMode = mode, isEnabled = mode != selectedMode.value)
    }
}


data class ChangeViewUiState(
    val selectedMode: ViewMode = ViewMode.BOTH,
    val isEnabled: Boolean = false,
    val showNotification: Boolean = false
)