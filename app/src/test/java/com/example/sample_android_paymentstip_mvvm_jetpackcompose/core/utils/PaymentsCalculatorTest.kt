package com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils

import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.SavedPaymentEntity
import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Test

class PaymentsCalculatorTest {

    private val calculator = PaymentsCalculator(PaymentRecordValidatorImpl()) // Real validator

    @Test
    fun testGetSavedPaymentsDtoEmptyList() {
        val resultList = calculator.getSavedPaymentsDto(emptyList())
        Assert.assertEquals(0.0f, resultList.totalAmount)
        Assert.assertEquals(0.0f, resultList.totalTip)
        Assert.assertEquals(0, resultList.totalPerson)
        assertEquals(emptyList<SavedPaymentEntity>(), resultList.list)
    }

    @Test
    fun testGetSavedPaymentsDtoValidPayments() {
        val entity1 = SavedPaymentEntity(1, System.currentTimeMillis(), 2000f, 3, 15, null)
        val entity2 = SavedPaymentEntity(2, System.currentTimeMillis(), 300f, 2, 10, null)
        val list = listOf(entity1, entity2)
        val resultList = calculator.getSavedPaymentsDto(list)

        Assert.assertEquals(2300f, resultList.totalAmount)
        Assert.assertEquals(330f, resultList.totalTip)
        Assert.assertEquals(5, resultList.totalPerson)
        assertEquals(list, resultList.list)
    }

    @Test
    fun testGetSavedPaymentsDtoWithMultipleInvalidPayments() {
        val entity1 = SavedPaymentEntity(1, 0L, 2000f, 3, 15, null) // Invalid amount
        val entity2 =
            SavedPaymentEntity(2, System.currentTimeMillis(), 200f, 0, 10, null) // Invalid persons
        val entity3 =
            SavedPaymentEntity(3, System.currentTimeMillis(), 200f, 2, -5, null) // Invalid tip percentage
        val list = listOf(entity1, entity2, entity3)
        val resultList = calculator.getSavedPaymentsDto(list)

        Assert.assertEquals(0.0f, resultList.totalAmount)
        Assert.assertEquals(0.0f, resultList.totalTip)
        Assert.assertEquals(0, resultList.totalPerson)
        assertEquals(emptyList<SavedPaymentEntity>(), resultList.list)
    }

    @Test
    fun testCalculateTipAmountValid() {
        val tipAmount = calculator.calculateTipAmount(10.0f, 15)
        Assert.assertEquals(1.5f, tipAmount)
    }

    @Test
    fun testCalculateTipAmountInvalidAmount() {
        val tipAmount = calculator.calculateTipAmount(0.0f, 15)
        Assert.assertEquals(0.0f, tipAmount)
    }

    @Test
    fun testCalculateTipAmountInvalidTipPercentage() {
        val tipAmount = calculator.calculateTipAmount(10.0f, -10)
        Assert.assertEquals(0.0f, tipAmount)
    }

    @Test
    fun testGetTipPerPersonValidPersons() {
        val tipAmount = calculator.getTipPerPerson(10.0f, 15, 2)
        Assert.assertEquals(0.75f, tipAmount)
    }

    @Test
    fun testGetTipPerPersonInvalidPersons() {
        val tipAmount = calculator.getTipPerPerson(10.0f, 15, 0)
        Assert.assertEquals(0.0f, tipAmount) // Division by zero avoided
    }
}