package com.example.policypal.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LeadViewModel(private val repository: LeadRepository) : ViewModel() {

    val leads = repository.getAllLeads()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insert(lead: LeadEntity) = viewModelScope.launch {
        repository.insertLead(lead)
    }

    fun update(lead: LeadEntity) = viewModelScope.launch {
        repository.updateLead(lead)
    }

    fun delete(lead: LeadEntity) = viewModelScope.launch {
        repository.deleteLead(lead)
    }
}