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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Crear nueva subasta", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = currentBid,
            onValueChange = { currentBid = it },
            label = { Text("Puja inicial") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                if (title.isNotBlank() && description.isNotBlank() && currentBid.toDoubleOrNull() != null) {
                    val newAuction = AuctionEntity(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        description = description,
                        currentBid = currentBid.toDouble(),
                        isFinished = false
                    )
                    viewModel.addAuctionLocallyAndRemotely(newAuction)
                    title = ""
                    description = ""
                    currentBid = ""
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Guardar subasta")
        }
    }
}