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
import com.example.subasta.data.localbd.AuctionDao
import com.example.subasta.data.repository.AuctionRepository
import com.example.subasta.navigation.Screen
import com.example.subasta.viewModel.AuctionViewModel
import com.example.subasta.viewModel.AuctionViewModelFactory
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight


@Composable
fun AuctionListScreen(navController: NavController, repository: AuctionRepository) {
    val viewModel: AuctionViewModel = viewModel(factory = AuctionViewModelFactory(repository))
    val auctions = viewModel.auctions.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Añade un Spacer aquí para empujar el botón hacia abajo
        Spacer(modifier = Modifier.height(32.dp)) // Puedes ajustar este valor (por ejemplo, 32.dp, 64.dp)

        Button(
            onClick = { navController.navigate(Screen.AddAuction.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear Subasta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            auctions.forEach { auction ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = auction.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { navController.navigate(Screen.AuctionDetail.createRoute(auction.id)) },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Ver Detalles")
                    }
                }
            }
        }
    }
}