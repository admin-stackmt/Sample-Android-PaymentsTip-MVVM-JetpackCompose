package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.usecase

import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.SavedPaymentEntity
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.repository.PaymentsRepository

class DeletePayments(private val repository: PaymentsRepository) {

    operator fun invoke(item: SavedPaymentEntity) {
        repository.deletePayment(item)
    }
}