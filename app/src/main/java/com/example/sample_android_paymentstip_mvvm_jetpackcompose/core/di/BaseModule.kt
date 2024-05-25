package com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.di

import android.app.Application
import androidx.room.Room
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BaseModule {

    @Singleton
    @Provides
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app, AppDatabase::class.java, AppDatabase.DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

}