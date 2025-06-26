package com.example.subasta.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.subasta.data.repository.AuctionRepository
import com.example.subasta.navigation.Screen
import com.example.subasta.viewModel.AuctionViewModel
import com.example.subasta.viewModel.AuctionViewModelFactory
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuctionListScreen(navController: NavController, repository: AuctionRepository) {
    val viewModel: AuctionViewModel = viewModel(factory = AuctionViewModelFactory(repository))
    val auctions by viewModel.auctions.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Subastas") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screen.AddAuction.route) },
                icon = { Icon(Icons.Filled.Add, "Crear nueva subasta") },
                text = { Text("Crear Subasta") },
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { paddingValues ->
        if (auctions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "No hay subastas",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No hay subastas para mostrar.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "¡Crea una nueva subasta usando el botón '+'!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp), // Padding horizontal para la lista
                contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp), // Espacio para el FAB
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(auctions) { auction ->
                    ElevatedCard( // Usamos ElevatedCard para un mejor efecto visual
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min), // Asegura que la tarjeta tenga una altura mínima
                        onClick = { navController.navigate(Screen.AuctionDetail.createRoute(auction.id)) },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = auction.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Puja Actual: $${auction.currentBid}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                // Opcional: Mostrar la descripción corta o un estado
                                // Text(auction.description, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            Button(
                                onClick = { navController.navigate(Screen.AuctionDetail.createRoute(auction.id)) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Ver Detalles")
                            }
                        }
                    }
                }
            }
        }
    }
}