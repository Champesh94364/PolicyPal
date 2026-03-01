package com.example.policypal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.policypal.viewmodel.ProposalViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ProposalViewModel
) {

    val proposals =
        viewModel.proposals.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "PolicyPal",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF0B7A75)
        )

        Spacer(Modifier.height(16.dp))

        Text("Total Proposals: ${proposals.size}")

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("new") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create New Proposal")
        }
    }
}