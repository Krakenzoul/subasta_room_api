package com.example.subasta.data.model

data class Auction(
    val id: String,
    val title: String,
    val description: String,
    val currentBid: Double,
    val isFinished: Boolean
)