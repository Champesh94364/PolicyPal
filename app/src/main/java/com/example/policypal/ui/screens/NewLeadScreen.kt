package com.example.policypal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.policypal.data.CustomField
import com.example.policypal.data.LeadEntity
import com.example.policypal.data.LeadViewModel
import kotlinx.coroutines.launch

@Composable
fun NewLeadScreen(vm: LeadViewModel) {
    val customFields = remember { mutableStateListOf<CustomField>() }
    val checkedStates = remember { mutableStateListOf(false, false, false, false, false) }

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var income by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {

            item {
                Text(
                    "New Lead",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF0B7A75)
                )
            }

            item {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(value = income, onValueChange = { income = it }, label = { Text("Monthly Income") }, modifier = Modifier.fillMaxWidth())
            }

            item { HorizontalDivider(thickness = 1.dp) }

            item { CustomFieldSection(customFields) }
            item { AIPersonaSection() }
            item { ChecklistSection(checkedStates) }

            item {
                Button(
                    onClick = {
                        val newLead = LeadEntity(
                            name = name,
                            phone = phone,
                            age = age,
                            city = city,
                            income = income,
                            status = "Active",
                            customFields = customFields.toList(),
                            checklist = checkedStates.toList()
                        )

                        vm.insert(newLead)

                        // clear inputs (stay on same page)
                        name = ""
                        phone = ""
                        age = ""
                        city = ""
                        income = ""
                        customFields.clear()
                        for (i in checkedStates.indices) checkedStates[i] = false

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Lead saved successfully ✅",
                                withDismissAction = false,
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Lead")
                }
            }
        }
    }
}

@Composable
private fun CustomFieldSection(customFields: MutableList<CustomField>) {
    var label by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }

    androidx.compose.foundation.layout.Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Add Custom Field", style = MaterialTheme.typography.titleSmall)

        OutlinedTextField(
            value = label,
            onValueChange = { label = it },
            label = { Text("Field Label") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = { Text("Value") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (label.isNotBlank()) {
                    customFields.add(CustomField(label = label.trim(), value = value.trim()))
                    label = ""
                    value = ""
                }
            }
        ) { Text("Add Field") }

        if (customFields.isNotEmpty()) {
            Text("Saved Fields:", style = MaterialTheme.typography.labelMedium)

            customFields.forEachIndexed { index, cf ->
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${cf.label}: ${cf.value}")
                    androidx.compose.material3.TextButton(onClick = { customFields.removeAt(index) }) {
                        Text("Remove")
                    }
                }
            }
        }
    }
}

@Composable
private fun AIPersonaSection() {
    var suggestions by remember { mutableStateOf(listOf<String>()) }

    androidx.compose.foundation.layout.Column {
        Text("AI Persona Profiler", style = MaterialTheme.typography.titleMedium)

        Button(onClick = {
            suggestions = listOf(
                "Has dependents?",
                "Existing insurance?",
                "Health conditions?",
                "Investment goal?"
            )
        }) { Text("Get AI Suggestions") }

        suggestions.forEach { Text("• $it") }
    }
}

@Composable
private fun ChecklistSection(checkedStates: MutableList<Boolean>) {
    val items = listOf(
        "Contacted",
        "Needs Analysis",
        "Docs Collected",
        "Proposal Shared",
        "Deal Closed"
    )

    androidx.compose.foundation.layout.Column {
        Text("Lead Progress Checklist")

        items.forEachIndexed { index, label ->
            androidx.compose.foundation.layout.Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                androidx.compose.material3.Checkbox(
                    checked = checkedStates[index],
                    onCheckedChange = { checkedStates[index] = it }
                )
                Text(label)
            }
        }
    }
}