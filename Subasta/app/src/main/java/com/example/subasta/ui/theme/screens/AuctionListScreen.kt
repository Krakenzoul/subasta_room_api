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

@Composable

fun AuctionListScreen(navController: NavController, repository: AuctionRepository) {
    val viewModel: AuctionViewModel = viewModel(factory = AuctionViewModelFactory(repository))
    val auctions = viewModel.auctions.collectAsState().value

    Column {
        Button(onClick = {
            navController.navigate(Screen.AddAuction.route)
        }) {
            Text("Crear Subasta")
        }

        auctions.forEach { auction ->
            Text(text = auction.title)
            Button(onClick = {
                navController.navigate(Screen.AuctionDetail.createRoute(auction.id))
            }) {
                Text("Ver Detalles")
            }
        }
    }
}