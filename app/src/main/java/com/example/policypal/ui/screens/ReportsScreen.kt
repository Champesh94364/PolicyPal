package com.example.policypal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.policypal.viewmodel.ProposalViewModel

@Composable
fun ReportsScreen(
    navController: NavController,
    viewModel: ProposalViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Reports", style = MaterialTheme.typography.titleLarge)
    }
}