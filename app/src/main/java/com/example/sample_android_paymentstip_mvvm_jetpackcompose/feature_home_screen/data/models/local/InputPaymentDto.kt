package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local

import androidx.compose.runtime.Stable

@Stable
data class InputPaymentDto(
    var amount: Float = 0.0f,
    var numPersons: Int = 1,
    var tipPercent: Int = 10,
    var isAttachPhotoEnabled: Boolean = false,
    var imageUri: String? = null
)
