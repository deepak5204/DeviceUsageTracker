package com.example.deviceusagetracker.presentation.viewModel

import com.example.deviceusagetracker.domain.model.AppUsage
import com.example.deviceusagetracker.domain.model.CategoryUsage

data class UsageUiState(
    val isLoading: Boolean = false,
    val usageList: List<AppUsage> = emptyList(),
    val categoryList: List<CategoryUsage> = emptyList(),
    val error: String? = null
)
