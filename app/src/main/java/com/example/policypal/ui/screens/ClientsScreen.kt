package com.example.policypal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.policypal.data.ProposalViewModel

@Composable
fun ClientsScreen(vm: ProposalViewModel) {
    val proposals = vm.proposals.collectAsStateWithLifecycle().value
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Clients", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        proposals.take(20).forEach { p ->
            Text("• #${p.id}  ${p.planName}  (${p.status})")
        }
    }
}