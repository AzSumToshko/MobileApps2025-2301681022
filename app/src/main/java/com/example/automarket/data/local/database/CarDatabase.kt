package com.example.automarket.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.automarket.data.local.dao.CarDao
import com.example.automarket.data.local.entity.CarEntity

@Database(entities = [CarEntity::class], version = 1, exportSchema = false)
abstract class CarDatabase : RoomDatabase() {

    abstract fun carDao(): CarDao

    companion object {
        @Volatile
        private var INSTANCE: CarDatabase? = null

        fun getDatabase(context: Context): CarDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CarDatabase::class.java,
                    "automarket_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
