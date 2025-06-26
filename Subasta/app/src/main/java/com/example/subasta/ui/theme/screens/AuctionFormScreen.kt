package com.example.subasta.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.subasta.data.model.AuctionEntity
import com.example.subasta.data.repository.AuctionRepository
import com.example.subasta.viewModel.AuctionViewModel
import com.example.subasta.viewModel.AuctionViewModelFactory
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.font.FontWeight



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuctionFormScreen(
    navController: NavHostController,
    repository: AuctionRepository
) {
    val viewModel: AuctionViewModel = viewModel(
        factory = AuctionViewModelFactory(repository)
    )

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var currentBid by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Subasta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                "Ingresa los detalles de la subasta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título de la Subasta") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción Detallada") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp) // Permite múltiples líneas
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = currentBid,
                onValueChange = { newValue ->
                    // Solo permite números y un punto decimal
                    // La validación de entrada sigue aquí para asegurar que solo se ingresen números
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        currentBid = newValue
                    }
                },
                label = { Text("Puja Inicial (ej. 100.00)") },
                // Eliminadas las líneas de keyboardOptions
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL de la Imagen (Opcional)") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val bidValue = currentBid.toDoubleOrNull()
                    if (title.isNotBlank() && description.isNotBlank() && bidValue != null) {
                        val newAuction = AuctionEntity(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            description = description,
                            currentBid = bidValue,
                            isFinished = false,
                            imageUrl = imageUrl
                        )
                        viewModel.addAuctionLocallyAndRemotely(newAuction)
                        navController.popBackStack()
                    } else {
                        // Opcional: mostrar un Toast o Snackbar si los campos no son válidos
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && description.isNotBlank() && currentBid.toDoubleOrNull() != null
            ) {
                Text("Guardar Subasta")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar y Volver")
            }
        }
    }
}