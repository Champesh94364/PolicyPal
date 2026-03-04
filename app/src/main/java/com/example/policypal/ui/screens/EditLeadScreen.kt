package com.example.policypal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.policypal.data.CustomField
import com.example.policypal.data.LeadViewModel

@Composable
fun EditLeadScreen(id: Long, navController: NavController, vm: LeadViewModel) {
    val leads = vm.leads.collectAsStateWithLifecycle().value
    val lead = leads.find { it.id == id } ?: return

    var name by remember(lead.id) { mutableStateOf(lead.name) }
    var phone by remember(lead.id) { mutableStateOf(lead.phone) }
    var age by remember(lead.id) { mutableStateOf(lead.age) }
    var city by remember(lead.id) { mutableStateOf(lead.city) }
    var income by remember(lead.id) { mutableStateOf(lead.income) }

    var checklist by remember(lead.id) { mutableStateOf(lead.checklist) }
    val customFields = remember(lead.id) {
        mutableStateListOf<CustomField>().apply { addAll(lead.customFields) }
    }

    val labels = listOf("Contacted", "Needs Analysis", "Docs Collected", "Proposal Shared", "Deal Closed")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item { Text("Edit Client", style = MaterialTheme.typography.titleLarge) }

        item {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(value = income, onValueChange = { income = it }, label = { Text("Income") }, modifier = Modifier.fillMaxWidth())
        }

        item { Text("Checklist", style = MaterialTheme.typography.titleMedium) }

        items(labels) { label ->
            val i = labels.indexOf(label)
            val checked = checklist.getOrNull(i) ?: false

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { v ->
                        val newList = checklist.toMutableList()
                        while (newList.size < labels.size) newList.add(false)
                        newList[i] = v
                        checklist = newList
                    }
                )
                Text(label)
            }
        }

        item {
            Button(
                onClick = {
                    vm.update(
                        lead.copy(
                            name = name,
                            phone = phone,
                            age = age,
                            city = city,
                            income = income,
                            customFields = customFields.toList(),
                            checklist = checklist
                        )
                    )
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save Changes") }
        }
    }
}