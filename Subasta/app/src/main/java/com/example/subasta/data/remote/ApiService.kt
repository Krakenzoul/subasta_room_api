package com.example.subasta.data.remote


// com.example.subasta.data.remote.ApiService
import com.example.subasta.data.model.Auction // Importa la clase de modelo del cliente
import retrofit2.http.Body
import retrofit2.http.DELETE // <-- ¡Añade esta línea!
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT // <-- Si ya añadiste el PUT en el servidor, agrégala aquí también
import retrofit2.http.Path

interface ApiService {
    @GET("auctions")
    suspend fun getAuctions(): List<Auction>

    @POST("auctions")
    suspend fun createAuction(@Body auction: Auction): Auction
    // Si necesitas el Response de Retrofit para manejar códigos de estado, etc.
    // suspend fun createAuction(@Body auction: Auction): Response<Auction> // Ejemplo con Response

    @GET("auctions/{id}")
    suspend fun getAuctionById(@Path("id") id: String): Auction

    @DELETE("auctions/{id}")
    suspend fun deleteAuction(@Path("id") id: String)

    @PUT("auctions/{id}") // <-- Si ya añadiste el PUT en el servidor, agrégala aquí también
    suspend fun updateAuction(@Path("id") id: String, @Body auction: Auction): Auction
}