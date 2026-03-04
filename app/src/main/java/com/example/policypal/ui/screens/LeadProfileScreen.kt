package com.example.policypal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.policypal.data.LeadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeadProfileScreen(id: Long, navController: NavController, vm: LeadViewModel) {
    val leads = vm.leads.collectAsStateWithLifecycle().value
    val lead = leads.find { it.id == id } ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Client Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { navController.navigate("edit/${lead.id}") }) {
                        Text("Edit")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(lead.name)
            Text("Phone: ${lead.phone}")
            Text("Age: ${lead.age}")
            Text("City: ${lead.city}")
            Text("Income: ${lead.income}")
            Text("Status: ${lead.status}")

            if (lead.customFields.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text("Custom Fields")
                lead.customFields.forEach { cf ->
                    Text("${cf.label}: ${cf.value}")
                }
            }

            Spacer(Modifier.height(12.dp))
            Text("ANANDA Checklist")

            val labels = listOf(
                "Contacted",
                "Needs Analysis",
                "Docs Collected",
                "Proposal Shared",
                "Deal Closed"
            )

            var checklist by remember(lead.id) { mutableStateOf(lead.checklist) }

            labels.forEachIndexed { i, label ->
                val checked = checklist.getOrNull(i) ?: false

                Row {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { newValue ->
                            val newList = checklist.toMutableList()
                            while (newList.size < labels.size) newList.add(false)
                            newList[i] = newValue
                            checklist = newList

                            vm.update(lead.copy(checklist = newList))
                        }
                    )
                    Text(label, modifier = Modifier.padding(top = 12.dp))
                }
            }
        }
    }
}