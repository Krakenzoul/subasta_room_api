// com.example.subasta.MainActivity.kt
package com.example.subasta

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
import com.example.subasta.data.localbd.AppDatabase
import com.example.subasta.data.repository.AuctionRepository
import com.example.subasta.navigation.AppNavigation

// --- ¡ASEGÚRATE DE QUE ESTAS IMPORTACIONES ESTÁN PRESENTES Y SON CORRECTAS! ---
import com.example.subasta.data.remote.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor // <-- ¡Esta es crucial!
// --- FIN NUEVAS IMPORTACIONES ---

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.auctionDao()

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // <-- 'Level' también necesita ser resuelto a través de la importación de HttpLoggingInterceptor
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val repository = AuctionRepository(dao, apiService)

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