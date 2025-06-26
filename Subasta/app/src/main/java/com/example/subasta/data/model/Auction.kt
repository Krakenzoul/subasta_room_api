// Archivo: com/example/subasta/data/model/Auction.kt
package com.example.subasta.data.model

// Este data class es el que Retrofit usará para comunicarse con tu API Ktor.
// Sus campos DEBEN coincidir con los campos de la 'data class Auction' en tu servidor Ktor.
data class Auction(
    val id: String,
    val title: String,
    val description: String,
    val currentBid: Double,
    val imageUrl: String, // <--- ¡CAMBIO CRÍTICO! Debe ser 'imageUrl' para coincidir con tu servidor Ktor
    val isActive: Boolean = true
)