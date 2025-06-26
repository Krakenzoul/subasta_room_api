// com.example.subasta.data.localbd.AppDatabase.kt
// com.example.subasta.data.localbd.AppDatabase.kt
package com.example.subasta.data.localbd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.subasta.data.model.AuctionEntity // Asegúrate de importar AuctionEntity

@Database(entities = [AuctionEntity::class], version = 3, exportSchema = false) // <-- ¡Incrementa la versión!
abstract class AppDatabase : RoomDatabase() {
    abstract fun auctionDao(): AuctionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "auction_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}