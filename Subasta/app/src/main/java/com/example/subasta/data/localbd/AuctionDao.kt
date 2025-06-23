package com.example.subasta.data.localbd

import androidx.room.*
import com.example.subasta.data.model.AuctionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuctionDao {

    @Query("SELECT * FROM auctions")
    fun getAll(): Flow<List<AuctionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(auction: AuctionEntity)

    @Delete
    suspend fun delete(auction: AuctionEntity)
}