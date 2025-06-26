package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.*
import io.ktor.serialization.gson.*
import io.ktor.server.plugins.contentnegotiation.*
import java.util.concurrent.ConcurrentHashMap //
import java.util.UUID //

// La clase Auction
data class Auction(
    val id: String,
    val title: String,
    val description: String,
    val currentBid: Double,
    val imageUrl: String,
    val isActive: Boolean = true
)


// Inicializamos con algunas subastas para propósitos de prueba.
val auctionsStore = ConcurrentHashMap<String, Auction>().apply {
    put("1", Auction(id = "1", title = "Coche Antiguo", description = "Un coche clásico de colección.", currentBid = 10000.0, imageUrl = "https://example.com/antique_car.jpg"))
    put("2", Auction(id = "2", title = "Pintura Moderna", description = "Obra de arte abstracta del siglo XX.", currentBid = 500.0, imageUrl = "https://example.com/modern_painting.jpg"))
    put("3", Auction(id = "3", title = "Reloj de Bolsillo", description = "Reloj antiguo de plata.", currentBid = 250.0, imageUrl = "https://example.com/pocket_watch.jpg"))
}

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }

    routing {
        get("/") {
            call.respondText("¡Servidor Ktor corriendo con .properties!")
        }

        // Obtener todas las subastas activas
        get("/auctions") {
            // Convertimos los valores del mapa a una lista y respondemos.
            // Opcionalmente, podrías filtrar solo las subastas activas:
            // val activeAuctions = auctionsStore.values.filter { it.isActive }
            // call.respond(activeAuctions)
            call.respond(auctionsStore.values.toList())
        }

        // Crear una nueva subasta
        post("/auctions") {
            val newAuctionData = call.receive<Auction>()

            val id = newAuctionData.id.ifEmpty { UUID.randomUUID().toString() }
            val newAuction = newAuctionData.copy(id = id) // Creamos una nueva instancia con el ID generado

            auctionsStore[id] = newAuction // Agregamos la nueva subasta al almacenamiento
            println("Subasta recibida y agregada: $newAuction")
            call.respond(HttpStatusCode.Created, newAuction)
        }

        // Obtener una subasta por ID
        get("/auctions/{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText("ID de subasta no especificado", status = HttpStatusCode.BadRequest)
            val foundAuction = auctionsStore[id] // Buscamos la subasta en el mapa

            if (foundAuction != null) {
                call.respond(foundAuction)
            } else {
                call.respondText("Subasta no encontrada con ID: $id", status = HttpStatusCode.NotFound)
            }
        }



        delete("/auctions/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respondText("ID de subasta no especificado", status = HttpStatusCode.BadRequest)
            if (auctionsStore.remove(id) != null) {
                call.respond(HttpStatusCode.NoContent) // 204 No Content para eliminación exitosa
            } else {
                call.respondText("Subasta no encontrada con ID: $id", status = HttpStatusCode.NotFound)
            }
        }


        put("/auctions/{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText("ID de subasta no especificado", status = HttpStatusCode.BadRequest)
            val updatedAuctionData = call.receive<Auction>()
            if (auctionsStore.containsKey(id)) {
                val updatedAuction = updatedAuctionData.copy(id = id) // Aseguramos que el ID sea el de la URL
                auctionsStore[id] = updatedAuction
                call.respond(HttpStatusCode.OK, updatedAuction)
            } else {
                call.respondText("Subasta no encontrada con ID: $id", status = HttpStatusCode.NotFound)
            }
        }
    }
}