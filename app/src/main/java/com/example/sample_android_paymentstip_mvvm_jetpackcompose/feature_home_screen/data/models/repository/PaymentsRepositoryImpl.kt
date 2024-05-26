package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.repository

import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils.PaymentsCalculator
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils.Validator
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.SavedPaymentEntity
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.sources.local.PaymentHistoryDao
import kotlinx.coroutines.flow.map

class PaymentsRepositoryImpl(
    private val paymentsDao: PaymentHistoryDao,
    private val calculator: PaymentsCalculator,
    private val validator: Validator
) : PaymentsRepository {
    override fun observePaymentsList() = paymentsDao.observePaymentsList().map {
        calculator.getSavedPaymentsDto(it)
    }

    override fun savePayment(savedPaymentEntity: SavedPaymentEntity) {
        val validation = validator.getPaymentValidation(
            savedPaymentEntity.timestamp,
            savedPaymentEntity.amount,
            savedPaymentEntity.persons,
            savedPaymentEntity.tipPercentage
        )
        if (validator.isValid(validation)) {
            paymentsDao.savePayment(savedPaymentEntity)
        }
    }

    override fun deletePayment(item: SavedPaymentEntity) {
        paymentsDao.deletePayment(item)
    }

}