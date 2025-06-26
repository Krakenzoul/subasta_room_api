
package com.example.subasta.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subasta.data.model.Auction
import com.example.subasta.data.model.AuctionEntity
import com.example.subasta.data.repository.AuctionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuctionViewModel(private val repository: AuctionRepository) : ViewModel() {

    // Tus otras propiedades y funciones existentes
    private val _auctions = MutableStateFlow<List<Auction>>(emptyList())
    val auctions: StateFlow<List<Auction>> = _auctions

    init {
        // Cargar subastas al inicio
        viewModelScope.launch {
            repository.getAuctionsLocally().collect { localAuctions ->
                _auctions.value = localAuctions.map { auctionEntityToAuction(it) }
            }
        }
        // También puedes iniciar la carga remota aquí
        viewModelScope.launch {
            repository.fetchAuctionsFromRemote() // Llama a tu función para obtener del servidor
        }
    }

    fun addAuctionLocallyAndRemotely(auction: AuctionEntity) {
        viewModelScope.launch {
            repository.addAuctionLocallyAndRemotely(auction)
            // Después de agregar, refrescar la lista para que la UI se actualice
            repository.fetchAuctionsFromRemote()
        }
    }

    // --- ¡NUEVA FUNCIÓN PARA ELIMINAR SUBASTA! ---
    fun deleteAuction(auctionId: String) {
        viewModelScope.launch {
            try {
                repository.deleteAuctionRemotely(auctionId) // Primero eliminar del servidor
                repository.deleteAuctionLocally(auctionId)   // Luego eliminar de la base de datos local (Room)
                // Después de eliminar, refrescar la lista para que la UI se actualice
                repository.fetchAuctionsFromRemote()
            } catch (e: Exception) {
                // Manejar errores de eliminación, por ejemplo, mostrar un Toast
                println("Error al eliminar subasta: ${e.message}")
            }
        }
    }

    // Funciones de mapeo (si las usas para convertir entre AuctionEntity y Auction)
    private fun auctionEntityToAuction(entity: AuctionEntity): Auction {
        return Auction(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            currentBid = entity.currentBid,
            imageUrl = entity.imageUrl,
            isActive = !entity.isFinished // Asumiendo que isFinished significa que no está activa
        )
    }
}