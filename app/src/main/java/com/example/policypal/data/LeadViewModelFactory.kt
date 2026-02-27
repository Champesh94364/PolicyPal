package com.example.policypal.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LeadViewModelFactory(
    private val repository: LeadRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeadViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeadViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}