package com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils

class PaymentRecordValidatorImpl : Validator {

    override fun getPaymentValidation(
        timestamp: Long?,
        amount: Float?,
        persons: Int?,
        tipPercentage: Int?
    ): Validator.Validation {
        if(!isTimeStampValid(timestamp)
            && !isAmountValid(amount)
            && !isPersonsValid(persons)
            && !isTipPercentageValid(tipPercentage)) {
            return Validator.Validation.Error
        }
        if(!isTimeStampValid(timestamp)) {
            return Validator.Validation.TimeStampError
        }
        if (!isAmountValid(amount)) {
            return Validator.Validation.AmountError
        }
        if (!isPersonsValid(persons)) {
            return Validator.Validation.PeopleError
        }
        if (!isTipPercentageValid(tipPercentage)) {
            return Validator.Validation.TipError
        }
        return Validator.Validation.Valid
    }

    override fun isTimeStampValid(timestamp: Long?): Boolean {
        if (timestamp == null) {
            return false
        }
        if (timestamp <= 0) {
            return false
        }
        return true
    }

    override fun isAmountValid(amount: Float?): Boolean {
        if (amount == null) {
            return false
        }
        if (amount <= 0.0f) {
            return false
        }
        return true
    }

    override fun isPersonsValid(persons: Int?): Boolean {
        if (persons == null) {
            return false
        }
        if (persons < 1) {
            return false
        }
        return true
    }

    override fun isTipPercentageValid(tipPercentage: Int?): Boolean {
        if (tipPercentage == null) {
            return false
        }
        if (tipPercentage < 0.0f) {
            return false
        }
        return true
    }

}