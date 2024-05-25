package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local

import androidx.compose.runtime.Stable

@Stable
data class InputCalculationsHolder(
    var totalTipAmount: Float = 0.0f,
    var perPersonTipAmount: Float = 0.0f
)
