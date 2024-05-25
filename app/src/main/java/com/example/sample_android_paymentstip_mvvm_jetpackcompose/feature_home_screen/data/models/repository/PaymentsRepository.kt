package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.repository

import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.GetSavedPaymentsDto
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.SavedPaymentEntity
import kotlinx.coroutines.flow.Flow

interface PaymentsRepository {

    fun observePaymentsList(): Flow<GetSavedPaymentsDto>

    fun savePayment(savedPaymentEntity: SavedPaymentEntity)

    fun deletePayment(item: SavedPaymentEntity)

}