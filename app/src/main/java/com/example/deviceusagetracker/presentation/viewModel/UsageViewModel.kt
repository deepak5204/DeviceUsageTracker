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
        loadTodayUsage()
    }

    fun loadTodayUsage() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {

                val categoryUsage = repository.getTodayCategoryUsage()
                val appUsage = repository.getTodayAppUsage()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    usageList = appUsage,
                    categoryList = categoryUsage
                )

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }
}
