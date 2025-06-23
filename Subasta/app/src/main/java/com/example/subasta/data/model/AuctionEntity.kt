package com.example.subasta.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auctions")
data class AuctionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val currentBid: Double,
    val isFinished: Boolean
)