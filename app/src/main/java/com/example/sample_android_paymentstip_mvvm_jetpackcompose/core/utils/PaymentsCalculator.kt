package com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils

import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.GetSavedPaymentsDto
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.SavedPaymentEntity

class PaymentsCalculator(private val validator: Validator) {

    fun getSavedPaymentsDto(list: List<SavedPaymentEntity>): GetSavedPaymentsDto {
        var totalAmount: Float = 0.0f
        var totalTip: Float = 0.0f
        var totalPerson = 0
        if (list.isEmpty()) {
            return GetSavedPaymentsDto(totalAmount, totalTip, totalPerson, list)
        }
        val mList = list.toMutableList()
        for (entity in list) {
            val validation = validator.getPaymentValidation(
                entity.timestamp,
                entity.amount,
                entity.persons,
                entity.tipPercentage
            )
            if (validator.isValid(validation)) {
                totalAmount += entity.amount
                totalTip += calculateTipAmount(entity.amount, entity.tipPercentage)
                totalPerson += entity.persons
            } else {
                mList.remove(entity)
            }
        }
        return GetSavedPaymentsDto(totalAmount, totalTip, totalPerson, mList)
    }

    fun calculateTipAmount(amount: Float, tipPercentage: Int): Float {
        if (!validator.isAmountValid(amount) || !validator.isTipPercentageValid(tipPercentage)) {
            return 0f
        }
        val res = (tipPercentage.toFloat() / 100f) * amount
        try {
            return "%.2f".format(res).toFloat()
        }catch (e: Exception) {
            return 0.0f
        }
    }

    fun getTipPerPerson(amount: Float, tipPercentage: Int, persons: Int): Float {
        if (!validator.isPersonsValid(persons)) {
            return 0f
        }
        val res = calculateTipAmount(amount, tipPercentage) / persons
        try {
            return "%.2f".format(res).toFloat()
        }catch (e: Exception) {
            return 0.0f
        }
    }
}