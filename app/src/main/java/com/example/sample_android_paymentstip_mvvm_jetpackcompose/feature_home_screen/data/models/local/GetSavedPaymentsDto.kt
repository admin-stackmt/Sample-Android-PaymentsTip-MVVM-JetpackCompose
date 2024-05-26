package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local

data class GetSavedPaymentsDto(
    val totalAmount: Float = 0f,
    val totalTip: Float = 0f,
    val totalPerson: Int = 0,
    val list: List<SavedPaymentEntity> = emptyList(),
    val isEmpty: Boolean = true
)

