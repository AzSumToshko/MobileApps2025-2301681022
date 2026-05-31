package com.example.automarket.di

import android.content.Context
import androidx.room.Room
import com.example.automarket.data.local.dao.CarDao
import com.example.automarket.data.local.database.CarDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CarDatabase =
        Room.databaseBuilder(
            context,
            CarDatabase::class.java,
            "automarket_database"
        ).build()

    @Provides
    fun provideCarDao(database: CarDatabase): CarDao = database.carDao()
}
