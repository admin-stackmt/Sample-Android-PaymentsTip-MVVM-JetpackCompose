package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.usecase

import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils.Validator
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.SavedPaymentEntity
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.repository.PaymentsRepository
import kotlinx.coroutines.flow.flow

class AddPayment(
    private val repository: PaymentsRepository,
    private val validator: Validator
) {

    sealed class States {
        data class Error(val errorMessage: String) : States()
        data object Added : States()
    }

    operator fun invoke(
        timestamp: Long?,
        amount: Float?,
        persons: Int?,
        tipPercentage: Int?,
        imagePath: String?
    ) = flow<States> {
        val validation = validator.getPaymentValidation(
            timestamp,
            amount,
            persons,
            tipPercentage
        )
        if (validator.isValid(validation)) {
            val paymentEntity = SavedPaymentEntity(
                0,
                timestamp!!,
                amount!!,
                persons!!,
                tipPercentage!!,
                imagePath
            )
            repository.savePayment(paymentEntity)
            emit(States.Added)
        } else {
            emit(States.Error(validation.message))
        }
    }

}