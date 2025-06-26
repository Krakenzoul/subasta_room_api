// Archivo: com/example/subasta/data/model/AuctionEntity.kt
package com.example.subasta.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auctions_table")
data class AuctionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val currentBid: Double,
    val isFinished: Boolean, // Este campo es para tu estado local/DB
    val imageUrl: String //
) {
    // Métodos de conversión convenientes para mapear entre Auction y AuctionEntity
    fun toRemoteAuction(): Auction {
        return Auction(
            id = this.id,
            title = this.title,
            description = this.description,
            currentBid = this.currentBid,
            imageUrl = this.imageUrl
        )
    }
}