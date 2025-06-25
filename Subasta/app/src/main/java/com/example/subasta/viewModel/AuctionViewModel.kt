// Archivo: com/example/subasta/viewModel/AuctionViewModel.kt
package com.example.subasta.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subasta.data.model.Auction // Modelo de la API
import com.example.subasta.data.model.AuctionEntity // Modelo de la DB local
import com.example.subasta.data.repository.AuctionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuctionViewModel(
    private val repository: AuctionRepository) : ViewModel() {

    private val _auctions = MutableStateFlow<List<AuctionEntity>>(emptyList())
    val auctions: StateFlow<List<AuctionEntity>> = _auctions.asStateFlow()

    private val _selectedAuction = MutableStateFlow<AuctionEntity?>(null) // Cambiado a AuctionEntity?
    val selectedAuction: StateFlow<AuctionEntity?> = _selectedAuction

    init {
        loadAuctionsFromApi() // Carga inicial desde la API
        observeLocalAuctions() // Siempre observa la DB local
    }

    private fun loadAuctionsFromApi() {
        viewModelScope.launch {
            try {
                val remoteAuctions = repository.getAuctions() // Obtiene de la API
                remoteAuctions.forEach { remoteAuction ->
                    // Convierte Auction (API) a AuctionEntity (DB local)
                    repository.insertLocalAuction(
                        AuctionEntity(
                            id = remoteAuction.id,
                            title = remoteAuction.title,
                            description = remoteAuction.description,
                            currentBid = remoteAuction.currentBid,
                            imageUrl = remoteAuction.imageUrl, // Mapea imageUrl
                            isFinished = false // Asume un valor por defecto o lógica si isFinished no viene de la API
                        )
                    )
                }
            } catch (e: Exception) {
                // Si falla la API, puedes notificar al usuario o intentar cargar solo desde local
                println("Error API al cargar subastas: ${e.message}")
            }
        }
    }

    private fun observeLocalAuctions() {
        viewModelScope.launch {
            repository.getLocalAuctions().collect {
                _auctions.value = it // Actualiza el Flow con los datos de la DB local
            }
        }
    }

    fun loadAuctionDetail(id: String) {
        viewModelScope.launch {
            // Intenta cargar de la API primero
            val remoteAuction = repository.getAuctionById(id)
            if (remoteAuction != null) {
                _selectedAuction.value = AuctionEntity( // Convierte a AuctionEntity para el detalle local
                    id = remoteAuction.id,
                    title = remoteAuction.title,
                    description = remoteAuction.description,
                    currentBid = remoteAuction.currentBid,
                    imageUrl = remoteAuction.imageUrl,
                    isFinished = _selectedAuction.value?.isFinished ?: false // Mantén el estado actual o un valor por defecto
                )
            } else {
                // Si la API falla, intenta cargar desde la DB local

            }
        }
    }

    fun addAuctionLocallyAndRemotely(auctionEntity: AuctionEntity) { // Renombrado a auctionEntity para claridad
        viewModelScope.launch {
            repository.insertLocalAuction(auctionEntity) // Primero a Room

            try {
                // Convierte AuctionEntity (DB local) a Auction (API)
                val remoteAuction = auctionEntity.toRemoteAuction() // Usa el método de conversión
                repository.createAuction(remoteAuction) // Luego a la API
            } catch (e: Exception) {
                println("Error al crear en servidor: ${e.message}")
            }
        }
    }
}