package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.usecase

import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.GetSavedPaymentsDto
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.repository.PaymentsRepository
import kotlinx.coroutines.flow.flow

class GetPayments(private val repository: PaymentsRepository) {

    sealed class States {
        data class Fetched(val data: GetSavedPaymentsDto) : States()
    }

    operator fun invoke() = flow<States> {
        repository.observePaymentsList().collect {
            emit(States.Fetched(it))
        }
    }
}