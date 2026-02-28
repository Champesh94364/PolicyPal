package com.example.policypal

import androidx.compose.foundation.layout.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.policypal.ui.theme.PolicyPalTheme
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.graphics.Color as AndroidColor
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.layout.imePadding
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PolicyPalTheme {
                PolicyPalApp()
            }
        }
    }
}


@Composable
fun PolicyPalApp() {
    val context = LocalContext.current

    // 1) Room database
    val db = remember { AppDatabase.getDatabase(context) }

    // 2) Repository
    val repo = remember { LeadRepository(db.leadDao()) }

    // 3) ViewModel (with factory)
    val leadViewModel: LeadViewModel = viewModel(
        factory = LeadViewModelFactory(repo)
    )

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color(0xFFEAF7F6)
    ) { padding ->
        NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(padding)) {
            composable("home") { HomeScreen(leadViewModel) }
            composable("clients") { ClientsScreen(navController, leadViewModel) }
            composable("new") { NewLeadScreen(leadViewModel) }
            composable("reports") { ReportsScreen(leadViewModel) }
            composable("lead/{id}") { backStack ->
                val id = backStack.arguments?.getString("id")!!.toLong()
                LeadProfileScreen(id, navController, leadViewModel)
            }
            composable("edit/{id}") { backStack ->
                val id = backStack.arguments?.getString("id")!!.toLong()
                EditLeadScreen(id, navController, leadViewModel)
            }
        }
    }
}


@Composable
fun BottomNavBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") {
                popUpTo("home") { inclusive = true }
                launchSingleTop = true
            } },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentRoute == "clients",
            onClick = { navController.navigate("clients") {
                launchSingleTop = true
            } },
            icon = { Icon(Icons.Default.List, null) },
            label = { Text("Clients") }
        )
        NavigationBarItem(
            selected = currentRoute == "new",
            onClick = { navController.navigate("new") {
                launchSingleTop = true
            } },
            icon = { Icon(Icons.Default.Add, null) },
            label = { Text("New") }
        )
        NavigationBarItem(
            selected = currentRoute == "reports",
            onClick = { navController.navigate("reports") {
                launchSingleTop = true
            } },
            icon = { Icon(Icons.Default.Assessment, null) },
            label = { Text("Reports") }
        )
    }
}


@Composable
fun HomeScreen(vm: LeadViewModel) {

    val leads by vm.leads.collectAsStateWithLifecycle()
    val totalLeads = leads.size
    val newLeads = leads.count { it.status == "Active" }   // change logic if needed
    val activeLeads = leads.count { it.status == "Active"}
    val closedLeads = leads.count { it.status == "Closed" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("GOOD AFTERNOON", style = MaterialTheme.typography.labelSmall)
        Text(
            "PolicyPal",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF0B7A75)
        )
        Text("LIC Agent Command Center")

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("TOTAL LEADS", totalLeads.toString(), Modifier.weight(1f))
            StatCard("NEW LEADS", newLeads.toString(), Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("SCHEDULED", "12", Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("ACTIVE", activeLeads.toString(), Modifier.weight(1f))
            StatCard("CLOSED", closedLeads.toString(), Modifier.weight(1f))
        }

        Spacer(Modifier.height(20.dp))

       LeadChartCard(leads)
    }
}
@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier   // 👈 passed from parent
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelSmall)
            Spacer(Modifier.height(6.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium)
            Text("+12% vs last month",
                color = Color(0xFF1FAE5B),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
@Composable
fun LeadChartCard(leads: List<LeadEntity>) {
    val activeCount = leads.count { it.status == "Active" }
    val closedCount = leads.count { it.status == "Closed" }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Lead Status", style = MaterialTheme.typography.titleMedium)
            Text("Active vs Closed", style = MaterialTheme.typography.bodySmall)

            Spacer(Modifier.height(16.dp))

            AndroidView(
                factory = { context ->
                    BarChart(context).apply {

                        val entries = listOf(
                            BarEntry(1f, activeCount.toFloat()),
                            BarEntry(2f, closedCount.toFloat())
                        )

                        val dataSet = BarDataSet(entries, "").apply {
                            color = AndroidColor.parseColor("#0B7A75")
                            valueTextSize = 12f
                        }

                        data = BarData(dataSet).apply {
                            barWidth = 0.6f
                        }

                        description.isEnabled = false
                        legend.isEnabled = false
                        axisRight.isEnabled = false

                        axisLeft.apply {
                            axisMinimum = 0f
                            setDrawGridLines(false)
                            setDrawAxisLine(false)
                        }

                        xAxis.apply {
                            setDrawGridLines(false)
                            setDrawAxisLine(false)
                            setDrawLabels(true)
                            granularity = 1f
                            position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                            valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                                override fun getFormattedValue(value: Float): String {
                                    return when (value.toInt()) {
                                        1 -> "Active"
                                        2 -> "Closed"
                                        else -> ""
                                    }
                                }
                            }
                        }

                        setScaleEnabled(false)
                        setPinchZoom(false)
                        animateY(600)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientsScreen(navController: NavController, vm: LeadViewModel) {

    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val leads by vm.leads.collectAsStateWithLifecycle()

    val filteredLeads = leads.filter {
        (it.name.contains(searchText, true) ||
                it.phone.contains(searchText, true)) &&
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

        // 🔍 SEARCH + FILTER UI
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
                                containerColor = if (selectedFilter == filter)
                                    Color(0xFF0B7A75) else Color.LightGray,
                                labelColor = if (selectedFilter == filter)
                                    Color.White else Color.Black
                            )
                        )
                    }
                }
            }
        }

        // 👥 CLIENT LIST
        items(filteredLeads, key = { it.id }) { lead ->

            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    if (value == SwipeToDismissBoxValue.EndToStart) {
                        vm.delete(lead)
                        false
                    } else {
                        true
                    }
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
                                Text(lead.name, style = MaterialTheme.typography.titleMedium)
                                Text(lead.phone, style = MaterialTheme.typography.bodySmall)
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeadProfileScreen(id: Long, navController: NavController, vm: LeadViewModel) {
    val leads by vm.leads.collectAsStateWithLifecycle()
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
            Text(lead.name, style = MaterialTheme.typography.headlineSmall)
            Text("Phone: ${lead.phone}")
            Text("Age: ${lead.age}")
            Text("City: ${lead.city}")
            Text("Income: ${lead.income}")
            Text("Status: ${lead.status}")
            if (lead.customFields.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text("Custom Fields", style = MaterialTheme.typography.titleMedium)
                lead.customFields.forEach { cf ->
                    Text("${cf.label}: ${cf.value}")
                }
            }

            Spacer(Modifier.height(12.dp))
            Text("ANANDA Checklist", style = MaterialTheme.typography.titleMedium)

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

                Row(verticalAlignment = Alignment.CenterVertically) {
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
                    Text(label)
                }
            }
        }
    }
}
@Composable
fun EmptyClientsState(navController: NavController, message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            message,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )

        Spacer(Modifier.height(12.dp))

        Button(onClick = {
            navController.navigate("new") {
                launchSingleTop = true
            }
        }) { Text("Add New Lead") }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(onClick = {
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
                launchSingleTop = true
            }
        }) { Text("Go Home") }
    }
}
@Composable
fun ClientCard(lead: LeadEntity, navController: NavController) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(lead.name, style = MaterialTheme.typography.titleMedium)
            Text(lead.phone)
            Text("City: ${lead.city}")
            Text("Income: ${lead.income}")

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    navController.navigate("edit/${lead.id}")
                }) {
                    Text("Edit")
                }
            }
        }
    }
}
@Composable
fun EditLeadScreen(id: Long, navController: NavController, vm: LeadViewModel) {

    val leads by vm.leads.collectAsStateWithLifecycle()
    val lead = leads.find { it.id == id } ?: return

    var name by remember(lead.id) { mutableStateOf(lead.name) }
    var phone by remember(lead.id) { mutableStateOf(lead.phone) }
    var age by remember(lead.id) { mutableStateOf(lead.age) }
    var city by remember(lead.id) { mutableStateOf(lead.city) }
    var income by remember(lead.id) { mutableStateOf(lead.income) }

    var checklist by remember(lead.id) { mutableStateOf(lead.checklist) }
    val customFields = remember(lead.id) {
        mutableStateListOf<com.example.policypal.data.CustomField>().apply { addAll(lead.customFields) }
    }

    val labels = listOf("Contacted", "Needs Analysis", "Docs Collected", "Proposal Shared", "Deal Closed")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 90.dp) // ✅ allows reaching last button
    ) {
        item { Text("Edit Client", style = MaterialTheme.typography.titleLarge) }

        item {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = income,
                onValueChange = { income = it },
                label = { Text("Income") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text("Checklist", style = MaterialTheme.typography.titleMedium)
        }

        items(labels.size) { i ->
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
                Text(labels[i])
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
@Composable
fun NewLeadScreen(vm: LeadViewModel) {

    val customFields = remember { mutableStateListOf<com.example.policypal.data.CustomField>() }
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
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = income,
                    onValueChange = { income = it },
                    label = { Text("Monthly Income") },
                    modifier = Modifier.fillMaxWidth()
                )
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

                        // ✅ clear inputs (stay on same page)
                        name = ""
                        phone = ""
                        age = ""
                        city = ""
                        income = ""
                        customFields.clear()
                        for (i in checkedStates.indices) checkedStates[i] = false

                        // ✅ success message for a moment
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
fun CustomFieldSection(customFields: MutableList<com.example.policypal.data.CustomField>) {
    var label by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                    customFields.add(
                        com.example.policypal.data.CustomField(
                            label = label.trim(),
                            value = value.trim()
                        )
                    )
                    label = ""
                    value = ""
                }
            }
        ) { Text("Add Field") }

        if (customFields.isNotEmpty()) {
            Text("Saved Fields:", style = MaterialTheme.typography.labelMedium)

            customFields.forEachIndexed { index, cf ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${cf.label}: ${cf.value}")

                    TextButton(onClick = { customFields.removeAt(index) }) {
                        Text("Remove")
                    }
                }
            }
        }
    }
}
@Composable
fun AIPersonaSection() {

    var suggestions by remember { mutableStateOf(listOf<String>()) }

    Column {
        Text("AI Persona Profiler", style = MaterialTheme.typography.titleMedium)

        Button(onClick = {
            suggestions = listOf(
                "Has dependents?",
                "Existing insurance?",
                "Health conditions?",
                "Investment goal?"
            )
        }) {
            Text("Get AI Suggestions")
        }

        suggestions.forEach {
            Text("• $it")
        }
    }
}
@Composable
fun ChecklistSection(checkedStates: MutableList<Boolean>) {

    val items = listOf(
        "Contacted",
        "Needs Analysis",
        "Docs Collected",
        "Proposal Shared",
        "Deal Closed"
    )

    Column {
        Text("Lead Progress Checklist")

        items.forEachIndexed { index, label ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkedStates[index],
                    onCheckedChange = { checkedStates[index] = it }
                )
                Text(label)
            }
        }
    }
}
@Composable fun ReportsScreen(vm: LeadViewModel) { Text("Reports Screen") }