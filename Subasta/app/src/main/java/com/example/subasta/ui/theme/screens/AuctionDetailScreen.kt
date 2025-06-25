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


// Archivo: com/example.subasta.ui.theme.screens/AuctionDetailScreen.kt
@Composable
fun AuctionDetailScreen(
    auctionId: String,
    navController: NavHostController,
    repository: AuctionRepository
) {
    val viewModel: AuctionViewModel = viewModel(
        factory = AuctionViewModelFactory(repository)
    )

    val auction by viewModel.selectedAuction.collectAsState() // Esto es AuctionEntity?

    LaunchedEffect(auctionId) {
        viewModel.loadAuctionDetail(auctionId)
    }

    if (auction != null) {
        // Usa 'auction!!' para asegurar que el compilador sepa que no es nulo aquí,
        // ya que está dentro del bloque 'if (auction != null)'.
        // Opcional: puedes usar 'auction?.let { currentAuction -> ... }'
        val currentAuction = auction!! // <--- ¡Añade esta línea!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Título: ${currentAuction.title}", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Descripción: ${currentAuction.description}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Puja actual: $${currentAuction.currentBid}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("URL Imagen: ${currentAuction.imageUrl}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Finalizada: ${if (currentAuction.isFinished) "Sí" else "No"}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Volver")
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}