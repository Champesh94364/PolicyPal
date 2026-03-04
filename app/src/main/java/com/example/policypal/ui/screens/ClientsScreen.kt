package com.example.policypal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.policypal.data.LeadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientsScreen(navController: NavController, vm: LeadViewModel) {
    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val leads = vm.leads.collectAsStateWithLifecycle().value

    val filteredLeads = leads.filter {
        (it.name.contains(searchText, true) || it.phone.contains(searchText, true)) &&
                (selectedFilter == "All" || it.status == selectedFilter)
    }

    if (filteredLeads.isEmpty()) {
        val msg = if (leads.isEmpty()) {
            "No clients yet. Add a client to get started."
        } else {
            "No matching results. Try another search/filter."
        }
        EmptyClientsState(navController, msg)
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Search clients") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Search, null) }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("All", "Active", "Closed").forEach { filter ->
                        AssistChip(
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (selectedFilter == filter) Color(0xFF0B7A75) else Color.LightGray,
                                labelColor = if (selectedFilter == filter) Color.White else Color.Black
                            )
                        )
                    }
                }
            }
        }

        items(filteredLeads, key = { it.id }) { lead ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    if (value == SwipeToDismissBoxValue.EndToStart) {
                        vm.delete(lead)
                        false
                    } else true
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                            .padding(16.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                    }
                }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("lead/${lead.id}") },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(lead.name, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                                Text(lead.phone, style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
                            }

                            AssistChip(
                                onClick = { navController.navigate("edit/${lead.id}") },
                                label = { Text("Edit ✏️") }
                            )
                        }

                        Spacer(Modifier.height(6.dp))

                        Text("City: ${lead.city}")
                        Text("Income: ${lead.income}")

                        Spacer(Modifier.height(8.dp))

                        AssistChip(
                            onClick = {},
                            label = { Text(lead.status) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyClientsState(navController: NavController, message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(message, color = Color.Gray)

        Spacer(Modifier.height(12.dp))

        androidx.compose.material3.Button(onClick = {
            navController.navigate("new") { launchSingleTop = true }
        }) { Text("Add New Lead") }

        Spacer(Modifier.height(8.dp))

        androidx.compose.material3.OutlinedButton(onClick = {
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
                launchSingleTop = true
            }
        }) { Text("Go Home") }
    }
}