// Archivo: com/example/subasta/data/localbd/AuctionDao.kt
package com.example.subasta.data.localbd

import androidx.room.*
import com.example.subasta.data.model.AuctionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuctionDao {

    @Query("SELECT * FROM auctions")
    fun getAll(): Flow<List<AuctionEntity>>

    @Query("SELECT * FROM auctions WHERE id = :id") // <--- ¡AÑADIR ESTA LÍNEA!
    suspend fun getById(id: String): AuctionEntity? // <--- ¡AÑADIR ESTA LÍNEA!

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(auction: AuctionEntity)

    @Delete
    suspend fun delete(auction: AuctionEntity)
}