package com.example.subasta.data.repository


import com.example.subasta.data.model.Auction
import com.example.subasta.data.localbd.AuctionDao
import com.example.subasta.data.model.AuctionEntity
import com.example.subasta.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class AuctionRepository(
    private val dao: AuctionDao
) {
    // Room
    fun getLocalAuctions(): Flow<List<AuctionEntity>> = dao.getAll()
    suspend fun insertLocalAuction(auction: AuctionEntity) = dao.insert(auction)

    // Retrofit (API)
    suspend fun getAuctions(): List<Auction> = RetrofitClient.api.getAuctions()

    suspend fun createAuction(auction: Auction): Response<Auction> {
        return RetrofitClient.api.createAuction(auction)
    }
    suspend fun getAuctionById(id: String): Auction? {
        return try {
            RetrofitClient.api.getAuctionById(id)
        } catch (e: Exception) {
            null
        }
    }
}