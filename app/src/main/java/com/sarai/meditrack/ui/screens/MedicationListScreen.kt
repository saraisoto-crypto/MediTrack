package com.sarai.meditrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis Medicamentos") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("medication_form?id=-1")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Button(
                onClick = { navController.navigate("quotes") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver frase motivacional")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Aún no tienes medicamentos registrados.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}