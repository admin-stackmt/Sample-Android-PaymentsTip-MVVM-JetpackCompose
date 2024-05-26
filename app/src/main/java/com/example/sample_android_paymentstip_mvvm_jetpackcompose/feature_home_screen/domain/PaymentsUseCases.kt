package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain

import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.usecase.AddPayment
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.usecase.DeletePayments
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.usecase.GetPayments

data class PaymentsUseCases(val getPayments: GetPayments, val addPayment: AddPayment, val deletePayments: DeletePayments)
