package com.example.deviceusagetracker.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deviceusagetracker.data.repository.UsageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsageViewModel(
    private val repository: UsageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsageUiState())
    val uiState: StateFlow<UsageUiState> = _uiState.asStateFlow()

    init {
        loadUsageData()
    }

    fun loadUsageData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val usage = repository.getTodayAppUsage()

                _uiState.value = UsageUiState(
                    isLoading = false,
                    usageList = usage
                )

            } catch (e: Exception) {
                _uiState.value = UsageUiState(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
