package com.example.policypal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.policypal.data.AppDatabase
import com.example.policypal.repository.ProposalRepository
import com.example.policypal.viewmodel.ProposalViewModel
import com.example.policypal.viewmodel.ProposalViewModelFactory
import com.example.policypal.ui.screens.*
import com.example.policypal.ui.theme.PolicyPalTheme

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

    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { ProposalRepository(database.proposalDao()) }

    val viewModel: ProposalViewModel = viewModel(
        factory = ProposalViewModelFactory(repository)
    )

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color(0xFFEAF7F6)
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {

            composable("home") {
                HomeScreen(navController, viewModel)
            }

            composable("clients") {
                ClientsScreen(navController, viewModel)
            }

            composable("new") {
                NewProposalScreen(navController, viewModel)
            }

            composable("reports") {
                ReportsScreen(navController, viewModel)
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") { launchSingleTop = true }
            },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = currentRoute == "clients",
            onClick = {
                navController.navigate("clients") { launchSingleTop = true }
            },
            icon = { Icon(Icons.Default.List, null) },
            label = { Text("Clients") }
        )

        NavigationBarItem(
            selected = currentRoute == "new",
            onClick = {
                navController.navigate("new") { launchSingleTop = true }
            },
            icon = { Icon(Icons.Default.Add, null) },
            label = { Text("New") }
        )

        NavigationBarItem(
            selected = currentRoute == "reports",
            onClick = {
                navController.navigate("reports") { launchSingleTop = true }
            },
            icon = { Icon(Icons.Default.Assessment, null) },
            label = { Text("Reports") }
        )
    }
}