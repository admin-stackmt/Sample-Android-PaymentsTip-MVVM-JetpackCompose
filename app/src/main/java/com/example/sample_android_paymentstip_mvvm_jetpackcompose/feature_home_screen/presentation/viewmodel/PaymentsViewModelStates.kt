package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.presentation.viewmodel

import androidx.compose.runtime.Stable
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.SavedPaymentEntity

@Stable
sealed class UIEvent {
    data object SavePayment : UIEvent()
    data object IncreaseNumPersons : UIEvent()
    data object DecreaseNumPersons : UIEvent()
    data class ChangeAmount(val amount: String) : UIEvent()
    data class ChangeTipPercentage(val tipPercentage: String) : UIEvent()
    data object ToggleTakePhotoCheckbox : UIEvent()
    data class DeletePayment(val item: SavedPaymentEntity) : UIEvent()
}


@Stable
sealed class PaymentsScreenStates {
    data object Empty: PaymentsScreenStates()
    data object Loaded: PaymentsScreenStates()
}

@Stable
sealed class AddPaymentStates {
    data object Reset: AddPaymentStates()
    data object Added: AddPaymentStates()
    data class Error(val errorMessage: String): AddPaymentStates()
}

@Stable
sealed class DeletePaymentStates {
    data object Reset: DeletePaymentStates()
    data object Started: DeletePaymentStates()
}