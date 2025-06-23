package com.example.subasta.data.localbd


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.subasta.data.model.AuctionEntity
import com.example.subasta.data.localbd.AuctionDao

@Database(entities = [AuctionEntity::class], version = 1, exportSchema = false)
abstract class AuctionDatabase : RoomDatabase() {

    abstract fun auctionDao(): AuctionDao

    companion object {
        @Volatile
        private var INSTANCE: AuctionDatabase? = null

        fun getDatabase(context: Context): AuctionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AuctionDatabase::class.java,
                    "auction_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}