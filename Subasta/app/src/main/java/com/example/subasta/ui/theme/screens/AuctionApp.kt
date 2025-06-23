package com.example.subasta.ui.theme.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.subasta.data.repository.AuctionRepository
import com.example.subasta.navigation.AppNavigation

@Composable
fun SubastasApp(repository: AuctionRepository) {
    val navController = rememberNavController()

    MaterialTheme {
        AppNavigation(navController, repository)
    }
}