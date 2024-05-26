package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tip_history")
data class SavedPaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "amount") val amount: Float,
    @ColumnInfo(name = "people") val persons: Int,
    @ColumnInfo(name = "tip_percentage") val tipPercentage: Int,
    @ColumnInfo(name = "image_path") val imagePath: String?
)
