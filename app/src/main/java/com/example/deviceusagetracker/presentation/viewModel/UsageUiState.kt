package com.example.deviceusagetracker.presentation.viewModel

import com.example.deviceusagetracker.domain.model.AppUsage

data class UsageUiState(
    val isLoading: Boolean = false,
    val usageList: List<AppUsage> = emptyList(),
    val error: String? = null
)
