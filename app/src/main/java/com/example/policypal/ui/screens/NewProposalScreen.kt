package com.example.policypal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.policypal.data.ProposalViewModel
import com.example.policypal.data.entities.ProposalEntity

@Composable
fun NewProposalScreen(vm: ProposalViewModel) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("New Proposal (temporary)", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        Button(onClick = { vm.insertProposal(ProposalEntity()) }) {
            Text("Create Draft")
        }
    }
}