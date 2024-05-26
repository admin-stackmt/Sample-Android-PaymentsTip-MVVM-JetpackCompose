package com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.SavedPaymentEntity
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.sources.local.PaymentHistoryDao

@Database(entities = [SavedPaymentEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val paymentHistoryDao: PaymentHistoryDao

    companion object {
        val DB_NAME = "payments_tip_database"
    }
}