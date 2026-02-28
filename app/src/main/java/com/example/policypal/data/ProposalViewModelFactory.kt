package com.example.policypal.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProposalViewModelFactory(
    private val repo: ProposalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProposalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProposalViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}