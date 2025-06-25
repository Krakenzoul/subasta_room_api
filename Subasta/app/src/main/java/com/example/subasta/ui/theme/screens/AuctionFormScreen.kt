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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp

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
    var imageUrl by remember { mutableStateOf("") } // <-- ¡Añadida la variable para imageUrl!

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

        OutlinedTextField( // <-- ¡Nuevo campo para la URL de la imagen!
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("URL de la Imagen") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Botón "Guardar subasta"
        Button(
            onClick = {
                if (title.isNotBlank() && description.isNotBlank() && currentBid.toDoubleOrNull() != null) {
                    val newAuction = AuctionEntity(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        description = description,
                        currentBid = currentBid.toDouble(),
                        isFinished = false, // Puedes mantenerlo como false por defecto al crear
                        imageUrl = imageUrl // <-- ¡Añadido el campo imageUrl!
                    )
                    viewModel.addAuctionLocallyAndRemotely(newAuction)
                    // Limpiar los campos después de guardar
                    title = ""
                    description = ""
                    currentBid = ""
                    imageUrl = "" // <-- Limpiar también el campo de la URL de la imagen
                    // Opcional: Volver al home después de guardar exitosamente
                    navController.popBackStack()
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Guardar subasta")
        }

        // Espacio entre los botones
        Spacer(modifier = Modifier.height(16.dp))

        // --- Nuevo botón para volver al Home ---
        Button(
            onClick = { navController.popBackStack() }, // Esto vuelve a la pantalla anterior
            modifier = Modifier.fillMaxWidth() // Puedes ajustar el Modifier según tu diseño
        ) {
            Text("Volver al Home")
        }
        // ----------------------------------------
    }
}