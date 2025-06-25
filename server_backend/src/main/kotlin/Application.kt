package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.*
// --- ¡NUEVAS IMPORTACIONES NECESARIAS! ---
import io.ktor.serialization.gson.* // Para gson {} dentro de ContentNegotiation
import io.ktor.server.plugins.contentnegotiation.* // Para ContentNegotiation

// --- ¡DEFINICIÓN DE LA CLASE AUCTION EN EL PROYECTO DEL SERVIDOR! ---
// Esta clase DEBE estar definida en el proyecto del servidor para que pueda ser resuelta.
// No puedes importarla directamente de tu proyecto de cliente Android a menos que sea un módulo compartido.
data class Auction(
    val id: String,
    val title: String,
    val description: String,
    val currentBid: Double,
    val imageUrl: String
)

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

        get("/auctions") {
            val auctions = listOf(
                Auction(id = "1", title = "Coche Antiguo", description = "Un coche clásico", currentBid = 10000.0, imageUrl = "url1"),
                Auction(id = "2", title = "Pintura Moderna", description = "Obra de arte abstracta", currentBid = 500.0, imageUrl = "url2")
            )
            call.respond(auctions)
        }

        post("/auctions") {
            // Asegúrate de que esta línea esté bien tipada
            val newAuction = call.receive<Auction>() // Este 'receive' viene de ktor-server-request
            println("Subasta recibida: $newAuction")
            call.respond(HttpStatusCode.Created, newAuction)
        }

        get("/auctions/{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText("ID de subasta no especificado", status = HttpStatusCode.BadRequest)
            val foundAuction = listOf(
                Auction(id = "1", title = "Coche Antiguo", description = "Un coche clásico", currentBid = 10000.0, imageUrl = "url1"),
                Auction(id = "2", title = "Pintura Moderna", description = "Obra de arte abstracta", currentBid = 500.0, imageUrl = "url2")
            ).find { it.id == id }

            if (foundAuction != null) {
                call.respond(foundAuction)
            } else {
                call.respondText("Subasta no encontrada con ID: $id", status = HttpStatusCode.NotFound)
            }
        }
    }
}