package com.example.subasta.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subasta.data.model.Auction
import com.example.subasta.data.model.AuctionEntity
import com.example.subasta.data.repository.AuctionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
class AuctionViewModel(
    private val repository: AuctionRepository) : ViewModel() {

    private val _auctions = MutableStateFlow<List<AuctionEntity>>(emptyList())
    val auctions: StateFlow<List<AuctionEntity>> = _auctions.asStateFlow()

    init {
        loadAuctionsFromApi()
        observeLocalAuctions()
    }

    private fun loadAuctionsFromApi() {
        viewModelScope.launch {
            try {
                val remote = repository.getAuctions() // Retrofit
                remote.forEach {
                    repository.insertLocalAuction(
                        AuctionEntity(
                            id = it.id,
                            title = it.title,
                            description = it.description,
                            currentBid = it.currentBid,
                            isFinished = it.isFinished
                        )
                    )
                }
            } catch (e: Exception) {
                println("Error API: ${e.message}")
            }
        }
    }

    private fun observeLocalAuctions() {
        viewModelScope.launch {
            repository.getLocalAuctions().collect {
                _auctions.value = it
            }
        }
    }
    private val _selectedAuction = MutableStateFlow<Auction?>(null)
    val selectedAuction: StateFlow<Auction?> = _selectedAuction

    fun loadAuctionDetail(id: String) {
        viewModelScope.launch {
            _selectedAuction.value = repository.getAuctionById(id)
        }
    }
    fun addAuctionLocallyAndRemotely(auction: AuctionEntity) {
        viewModelScope.launch {
            repository.insertLocalAuction(auction) // Room del local
            try {
                val remote = Auction(
                    id = auction.id,
                    title = auction.title,
                    description = auction.description,
                    currentBid = auction.currentBid,
                    isFinished = auction.isFinished
                )
                repository.createAuction(remote) // Retrofit POST para la api :D
            } catch (e: Exception) {
                println("Error al crear en servidor: ${e.message}")
            }
        }
    }
}