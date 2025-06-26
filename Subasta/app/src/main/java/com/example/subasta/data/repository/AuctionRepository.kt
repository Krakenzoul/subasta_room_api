// com.example.subasta.data.repository.AuctionRepository.kt
package com.example.subasta.data.repository

import com.example.subasta.data.localbd.AuctionDao
import com.example.subasta.data.model.Auction
import com.example.subasta.data.model.AuctionEntity
import com.example.subasta.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuctionRepository(
    private val auctionDao: AuctionDao,
    private val apiService: ApiService // Asegúrate de inyectar ApiService aquí
) {

    // Tus otras funciones existentes
    fun getAuctionsLocally(): Flow<List<AuctionEntity>> {
        return auctionDao.getAllAuctions()
    }

    suspend fun addAuctionLocallyAndRemotely(auction: AuctionEntity) {
        // Primero intenta añadirla remotamente
        try {
            val response = apiService.createAuction(auctionEntityToAuction(auction))
            auctionDao.insertAuction(auction.copy(id = response.id)) // Guarda con el ID del servidor
        } catch (e: Exception) {
            // Si falla la red, solo la guarda localmente (o maneja el error)
            auctionDao.insertAuction(auction)
            println("Error al añadir remotamente: ${e.message}")
        }
    }

    suspend fun fetchAuctionsFromRemote() {
        try {
            val remoteAuctions = apiService.getAuctions()
            // Limpia y vuelve a insertar las subastas locales para sincronizar
            auctionDao.deleteAllAuctions()
            remoteAuctions.forEach { auctionDao.insertAuction(auctionToAuctionEntity(it)) }
        } catch (e: Exception) {
            println("Error al obtener subastas remotas: ${e.message}")
            // Puedes decidir si quieres cargar desde la base de datos local si falla la red
        }
    }

    // --- ¡NUEVAS FUNCIONES PARA ELIMINAR! ---
    suspend fun deleteAuctionRemotely(auctionId: String) {
        apiService.deleteAuction(auctionId)
    }

    suspend fun deleteAuctionLocally(auctionId: String) {
        auctionDao.deleteAuctionById(auctionId)
    }

    // Funciones de mapeo (si las usas para convertir entre AuctionEntity y Auction)
    private fun auctionEntityToAuction(entity: AuctionEntity): Auction {
        return Auction(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            currentBid = entity.currentBid,
            imageUrl = entity.imageUrl,
            isActive = !entity.isFinished
        )
    }

    private fun auctionToAuctionEntity(auction: Auction): AuctionEntity {
        return AuctionEntity(
            id = auction.id,
            title = auction.title,
            description = auction.description,
            currentBid = auction.currentBid,
            isFinished = !auction.isActive,
            imageUrl = auction.imageUrl
        )
    }
}