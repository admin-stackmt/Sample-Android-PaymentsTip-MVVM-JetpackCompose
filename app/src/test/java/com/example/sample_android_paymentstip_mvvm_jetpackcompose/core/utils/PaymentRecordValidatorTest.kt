package com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils

import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Test

class PaymentRecordValidatorTest {

    private val validator = PaymentRecordValidatorImpl()

    @Test
    fun testAllFieldsInvalid() {
        val validation = validator.getPaymentValidation(null, null, null, null)
        assertEquals(Validator.Validation.Error, validation)
    }

    @Test
    fun testOnlyTimeStampInvalid() {
        val validation = validator.getPaymentValidation(0, 10.0f, 2, 15)
        assertEquals(Validator.Validation.TimeStampError, validation)
    }

    @Test
    fun testOnlyAmountInvalid() {
        val validation = validator.getPaymentValidation(1L, 0.0f, 2, 15)
        assertEquals(Validator.Validation.AmountError, validation)
    }

    @Test
    fun testOnlyPersonsInvalid() {
        val validation = validator.getPaymentValidation(1L, 10.0f, 0, 15)
        assertEquals(Validator.Validation.PeopleError, validation)
    }

    @Test
    fun testOnlyTipPercentageInvalid() {
        val validation = validator.getPaymentValidation(1L, 10.0f, 2, -1)
        assertEquals(Validator.Validation.TipError, validation)
    }

    @Test
    fun testAllFieldsValid() {
        val validation = validator.getPaymentValidation(1L, 10.0f, 2, 15)
        assertEquals(Validator.Validation.Valid, validation)
    }

    // Tests for individual validation methods

    @Test
    fun testTimeStampValidWithNull() {
        val isValid = validator.isTimeStampValid(null)
        Assert.assertFalse(isValid)
    }

    @Test
    fun testTimeStampValidWithZero() {
        val isValid = validator.isTimeStampValid(0)
        Assert.assertFalse(isValid)
    }

    @Test
    fun testTimeStampValidWithPositiveValue() {
        val isValid = validator.isTimeStampValid(10L)
        Assert.assertTrue(isValid)
    }

    @Test
    fun testAmountValidWithNull() {
        val isValid = validator.isAmountValid(null)
        Assert.assertFalse(isValid)
    }

    @Test
    fun testAmountValidWithZero() {
        val isValid = validator.isAmountValid(0.0f)
        Assert.assertFalse(isValid)
    }

    @Test
    fun testAmountValidWithPositiveValue() {
        val isValid = validator.isAmountValid(10.0f)
        Assert.assertTrue(isValid)
    }

    @Test
    fun testPersonsValidWithNull() {
        val isValid = validator.isPersonsValid(null)
        Assert.assertFalse(isValid)
    }

    @Test
    fun testPersonsValidWithZero() {
        val isValid = validator.isPersonsValid(0)
        Assert.assertFalse(isValid)
    }

    @Test
    fun testPersonsValidWithPositiveValue() {
        val isValid = validator.isPersonsValid(2)
        Assert.assertTrue(isValid)
    }

    @Test
    fun testTipPercentageValidWithNull() {
        val isValid = validator.isTipPercentageValid(null)
        Assert.assertFalse(isValid)
    }

    @Test
    fun testTipPercentageValidWithNegativeValue() {
        val isValid = validator.isTipPercentageValid(-1)
        Assert.assertFalse(isValid)
    }

    @Test
    fun testTipPercentageValidWithPositiveValue() {
        val isValid = validator.isTipPercentageValid(15)
        Assert.assertTrue(isValid)
    }
}