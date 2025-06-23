package com.example.subastas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.subasta.data.localbd.AuctionDao
import com.example.subasta.ui.theme.SubastaTheme
import com.example.subasta.ui.theme.screens.AuctionListScreen
import androidx.navigation.compose.rememberNavController
import com.example.subasta.data.localbd.AuctionDatabase
import com.example.subasta.data.repository.AuctionRepository
import com.example.subasta.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Room
        val db = AuctionDatabase.getDatabase(applicationContext)
        val dao = db.auctionDao()

        // Crear el repositorio
        val repository = AuctionRepository(dao)

        setContent {
            SubastasApp {
                val navController = rememberNavController()
                AppNavigation(navController, repository)
            }
        }
    }
}
@Composable
fun SubastasApp(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SubastaTheme {
        Greeting("Android")
    }
}