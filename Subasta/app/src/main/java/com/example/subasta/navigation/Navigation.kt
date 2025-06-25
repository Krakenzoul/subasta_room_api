// Archivo: com/example/subasta/navigation/AppNavigation.kt
package com.example.subasta.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.subasta.data.repository.AuctionRepository
import com.example.subasta.ui.theme.screens.AuctionDetailScreen
import com.example.subasta.ui.theme.screens.AuctionListScreen
import com.example.subasta.ui.theme.screens.AuctionFormScreen // <--- ELIMINAR LA DUPLICADA, DEJAR SOLO UNA

@Composable
fun AppNavigation(navController: NavHostController, repository: AuctionRepository) {
    NavHost(navController = navController, startDestination = Screen.AuctionList.route) {

        composable(
            route = Screen.AuctionDetail.route, // Mantenerlo así si creas la ruta con el ID al navegar
            arguments = listOf(navArgument("auctionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val auctionId = backStackEntry.arguments?.getString("auctionId") ?: return@composable
            AuctionDetailScreen(
                auctionId = auctionId,
                navController = navController,
                repository = repository
            )
        }

        composable(Screen.AddAuction.route) {
            AuctionFormScreen(navController = navController, repository = repository)
        }

        composable(Screen.AuctionList.route) {
            AuctionListScreen(navController, repository)
        }
    }
}