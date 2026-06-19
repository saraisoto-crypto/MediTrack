package com.sarai.meditrack.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sarai.meditrack.ui.screens.MedicationListScreen
import com.sarai.meditrack.ui.screens.MedicationDetailScreen
import com.sarai.meditrack.ui.screens.MedicationFormScreen
import com.sarai.meditrack.ui.screens.QuoteScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "medication_list"
    ) {
        composable("medication_list") {
            MedicationListScreen(navController = navController)
        }
        composable(
            route = "medication_detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            MedicationDetailScreen(navController = navController, medicationId = id)
        }
        composable(
            route = "medication_form?id={id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
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