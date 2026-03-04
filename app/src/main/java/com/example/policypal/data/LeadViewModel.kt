package com.example.policypal.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LeadViewModel(private val repo: LeadRepository) : ViewModel() {
    val leads = repo.leads.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun insert(lead: LeadEntity) = viewModelScope.launch { repo.insert(lead) }
    fun update(lead: LeadEntity) = viewModelScope.launch { repo.update(lead) }
    fun delete(lead: LeadEntity) = viewModelScope.launch { repo.delete(lead) }
}