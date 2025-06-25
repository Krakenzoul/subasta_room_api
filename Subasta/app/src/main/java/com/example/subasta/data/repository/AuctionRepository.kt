package com.example.subasta.data.repository

import com.example.subasta.data.localbd.AuctionDao
import com.example.subasta.data.model.Auction // Modelo para la API (con 'imageUrl')
import com.example.subasta.data.model.AuctionEntity // Modelo para Room (con 'imageUrl' y 'isFinished')
import com.example.subasta.data.remote.RetrofitClient // Para obtener la instancia de ApiService
import com.example.subasta.data.remote.ApiService // Importa tu interfaz ApiService si está en este paquete
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response // Importar Response de Retrofit

class AuctionRepository(
    private val auctionDao: AuctionDao, // Renombrado de 'dao' a 'auctionDao' para claridad
    // Inyección del ApiService: es más limpio y testeable
    private val apiService: ApiService = RetrofitClient.api
) {
    // --- Métodos de la Base de Datos Local (Room) ---

    // Obtener todas las subastas de la DB local como Flow
    fun getLocalAuctions(): Flow<List<AuctionEntity>> {
        return auctionDao.getAll()
    }

    // Insertar una subasta en la DB local
    suspend fun insertLocalAuction(auction: AuctionEntity) = withContext(Dispatchers.IO) {
        auctionDao.insert(auction)
    }

    // Obtener una subasta por ID de la DB local
    // (Asegúrate de que AuctionDao tiene un método getById(id: String): AuctionEntity?)
    suspend fun getLocalAuctionById(id: String): AuctionEntity? = withContext(Dispatchers.IO) {
        auctionDao.getById(id)
    }

    // --- Métodos de la API (Retrofit) ---

    // Obtener todas las subastas de la API
    suspend fun getAuctions(): List<Auction> = withContext(Dispatchers.IO) {
        // La llamada a la API debe ocurrir en un hilo de fondo
        apiService.getAuctions()
    }

    // Obtener detalles de una subasta por ID de la API
    suspend fun getAuctionById(id: String): Auction? = withContext(Dispatchers.IO) {
        try {
            // Maneja potenciales errores de red o 404 de la API
            apiService.getAuctionById(id)
        } catch (e: Exception) {
            println("Error fetching auction by ID from API: ${e.message}")
            // Puedes retornar null o una subasta de la DB local si la API falla
            null
        }
    }

    // Crear una subasta en la API
    suspend fun createAuction(auction: Auction): Response<Auction> = withContext(Dispatchers.IO) {
        try {
            // Envía el objeto 'Auction' (modelo API) al servidor
            val response = apiService.createAuction(auction)
            if (response.isSuccessful) {
                println("Subasta creada en servidor: ${response.body()}")
            } else {
                println("Error al crear subasta en servidor (HTTP ${response.code()}): ${response.errorBody()?.string()}")
            }
            response // Devuelve la respuesta para que el ViewModel pueda verificar el éxito
        } catch (e: Exception) {
            println("Error de red al crear subasta: ${e.message}")
            // Devuelve una respuesta de error para que el ViewModel pueda manejarla
            Response.error(500, okhttp3.ResponseBody.create(null, "Network error: ${e.message}"))
        }
    }
}