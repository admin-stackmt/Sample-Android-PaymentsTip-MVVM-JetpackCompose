package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.sources.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.SavedPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentHistoryDao {

    @Query("SELECT * from tip_history order by id DESC")
    fun observePaymentsList(): Flow<List<SavedPaymentEntity>>

    @Insert
    fun savePayment(savedPaymentEntity: SavedPaymentEntity)

    @Delete
    fun deletePayment(item: SavedPaymentEntity)

    @Query("Delete from tip_history")
    fun clearAll()
}