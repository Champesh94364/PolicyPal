package com.example.policypal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.policypal.viewmodel.ProposalViewModel

@Composable
fun ClientsScreen(
    navController: NavController,
    viewModel: ProposalViewModel
) {

    val proposals =
        viewModel.proposals.collectAsStateWithLifecycle().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(proposals) { proposal ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // later we will open detail screen
                    }
            ) {

                Column(Modifier.padding(16.dp)) {

                    Text(
                        text = "Proposal #${proposal.id}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(6.dp))

                    Text("Plan: ${proposal.planName}")
                    Text("Status: ${proposal.status}")
                }
            }
        }
    }
}