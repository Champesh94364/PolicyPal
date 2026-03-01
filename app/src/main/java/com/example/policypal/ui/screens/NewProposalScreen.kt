package com.example.policypal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.policypal.data.entities.ProposalEntity
import com.example.policypal.viewmodel.ProposalViewModel

@Composable
fun NewProposalScreen(
    navController: NavController,
    viewModel: ProposalViewModel
) {

    var planName by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Draft") }

    Scaffold {

        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Text(
                    "New Proposal",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {
                OutlinedTextField(
                    value = planName,
                    onValueChange = { planName = it },
                    label = { Text("Plan Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Button(
                    onClick = {
                        viewModel.insertProposal(
                            ProposalEntity(
                                planName = planName,
                                status = status
                            )
                        )
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Proposal")
                }
            }
        }
    }
}