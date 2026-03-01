package com.example.policypal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.policypal.data.ProposalViewModel

@Composable
fun HomeScreen(vm: ProposalViewModel) {
    val proposals = vm.proposals.collectAsStateWithLifecycle().value
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("PolicyPal", style = MaterialTheme.typography.headlineLarge, color = Color(0xFF0B7A75))
        Spacer(Modifier.height(12.dp))
        Text("Total proposals: ${proposals.size}")
    }
}