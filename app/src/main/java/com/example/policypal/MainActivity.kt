package com.example.policypal

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.policypal.data.AppDatabase
import com.example.policypal.data.LeadRepository
import com.example.policypal.data.LeadViewModel
import com.example.policypal.data.LeadViewModelFactory
import com.example.policypal.ui.screens.ClientsScreen
import com.example.policypal.ui.screens.EditLeadScreen
import com.example.policypal.ui.screens.HomeScreen
import com.example.policypal.ui.screens.LeadProfileScreen
import com.example.policypal.ui.screens.NewLeadScreen
import com.example.policypal.ui.screens.ReportsScreen
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
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
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
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentRoute == "clients",
            onClick = {
                navController.navigate("clients") {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.List, null) },
            label = { Text("Clients") }
        )
        NavigationBarItem(
            selected = currentRoute == "new",
            onClick = {
                navController.navigate("new") {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Add, null) },
            label = { Text("New") }
        )
        NavigationBarItem(
            selected = currentRoute == "reports",
            onClick = {
                navController.navigate("reports") {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Assessment, null) },
            label = { Text("Reports") }
        )
    }
}