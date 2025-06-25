package com.example.subasta.data.localbd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.subasta.data.model.AuctionEntity

@Database(entities = [AuctionEntity::class], version = 2, exportSchema = false)
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
                )
                    .fallbackToDestructiveMigration() // ¡Esta es la línea clave!
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}