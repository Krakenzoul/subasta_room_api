
package com.example.subasta.data.localbd

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import com.example.subasta.data.model.AuctionEntity

@Dao
interface AuctionDao {
    @Query("SELECT * FROM auctions_table ORDER BY title ASC") // <-- Corregido el nombre de la tabla aquí
    fun getAllAuctions(): Flow<List<AuctionEntity>>

    @Query("SELECT * FROM auctions_table WHERE id = :id") // <-- Corregido el nombre de la tabla aquí
    suspend fun getById(id: String): AuctionEntity?

    @Query("DELETE FROM auctions_table WHERE id = :auctionId") // <-- Corregido el nombre de la tabla aquí
    suspend fun deleteAuctionById(auctionId: String)

    @Query("DELETE FROM auctions_table") // <-- Corregido el nombre de la tabla aquí
    suspend fun deleteAllAuctions() // Para sincronización

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuction(auction: AuctionEntity) // Nombre de la función más claro

    // Si prefieres usar @Delete para eliminar una entidad completa
    // @Delete
    // suspend fun delete(auction: AuctionEntity)
}