package com.example.subasta.ui.theme.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.subasta.viewModel.AuctionViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.subasta.data.repository.AuctionRepository
import com.example.subasta.viewModel.AuctionViewModelFactory


@Composable
fun AuctionDetailScreen(
    auctionId: String,
    navController: NavHostController,
    repository: AuctionRepository
) {
    // ViewModel con Factory (inyección manual del repositorio)
    val viewModel: AuctionViewModel = viewModel(
        factory = AuctionViewModelFactory(repository)
    )

    // Estado de la subasta observando el Flow
    val auction by viewModel.selectedAuction.collectAsState()

    // Efecto para cargar los datos al entrar
    LaunchedEffect(auctionId) {
        viewModel.loadAuctionDetail(auctionId)
    }

    // UI
    if (auction != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Título: ${auction!!.title}", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Descripción: ${auction!!.description}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Puja actual: $${auction!!.currentBid}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Volver")
            }
        }
    } else {
        // Estado de carga o error
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}