package com.example.policypal.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.policypal.data.entities.ProposalEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProposalViewModel(private val repo: ProposalRepository) : ViewModel() {

    val proposals = repo.observeAllProposals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun insertProposal(p: ProposalEntity) = viewModelScope.launch {
        repo.insertProposal(p)
    }

    fun updateProposal(p: ProposalEntity) = viewModelScope.launch {
        repo.updateProposal(p)
    }

    fun deleteProposal(id: Long) = viewModelScope.launch {
        repo.deleteProposal(id)
    }
}