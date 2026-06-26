package com.sarai.meditrack.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sarai.meditrack.ui.screens.LoginScreen
import com.sarai.meditrack.ui.screens.MedicationDetailScreen
import com.sarai.meditrack.ui.screens.MedicationFormScreen
import com.sarai.meditrack.ui.screens.MedicationListScreen
import com.sarai.meditrack.ui.screens.QuoteScreen
import com.sarai.meditrack.ui.screens.RegisterScreen
import com.sarai.meditrack.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    // Si hay sesión activa abre la lista, si no va al login
    val startDestination = if (authViewModel.isLoggedIn) "medication_list" else "login"

    NavHost(
        navController    = navController,
        startDestination = startDestination
    ) {

        // ── Auth ─────────────────────────────────────────────────────────────
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }

        // ── App ──────────────────────────────────────────────────────────────
        composable("medication_list") {
            MedicationListScreen(
                navController = navController,
                onLogout = {
                    authViewModel.logout {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable(
            route     = "medication_detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            MedicationDetailScreen(navController = navController, medicationId = id)
        }
        composable(
            route     = "medication_form?id={id}",
            arguments = listOf(navArgument("id") {
                type         = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            MedicationFormScreen(navController = navController, medicationId = id)
        }
        composable("quotes") {
            QuoteScreen(navController = navController)
        }
    }
}