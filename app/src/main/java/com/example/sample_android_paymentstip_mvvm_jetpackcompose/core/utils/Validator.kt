package com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils

interface Validator {

    enum class Validation(val message: String) {
        Valid(""),
        TimeStampError("Something went wrong! Please try again"),
        AmountError("Please enter an amount greater than 0.0"),
        PeopleError("Please update the number of people (greater than 0)"),
        TipError("Please enter the tip percentage"),
        Error("Something went wrong! Please try again")
    }

    fun getPaymentValidation(
        timestamp: Long?,
        amount: Float?,
        persons: Int?,
        tipPercentage: Int?
    ): Validator.Validation {
        return Validation.Error
    }

    fun isTimeStampValid(timestamp: Long?): Boolean {
        return false
    }

    fun isAmountValid(amount: Float?): Boolean {
        return false
    }

    fun isPersonsValid(persons: Int?): Boolean {
        return false
    }

    fun isTipPercentageValid(tipPercentage: Int?): Boolean {
        return false
    }

    fun isValid(validation: Validation) = validation == Validation.Valid

}