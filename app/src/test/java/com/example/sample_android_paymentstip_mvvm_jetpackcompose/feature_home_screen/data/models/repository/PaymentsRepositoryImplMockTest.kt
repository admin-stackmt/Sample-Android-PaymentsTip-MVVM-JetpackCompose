package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.repository

import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils.PaymentRecordValidatorImpl
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils.PaymentsCalculator
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.GetSavedPaymentsDto
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.SavedPaymentEntity
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.sources.local.PaymentHistoryDao
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PaymentsRepositoryImplMockTest {

    private val dao: PaymentHistoryDao = mockk()
    private lateinit var validator: PaymentRecordValidatorImpl
    private lateinit var calculator: PaymentsCalculator

    private lateinit var repository: PaymentsRepositoryImpl

    @Before
    fun setUp() {
        validator = PaymentRecordValidatorImpl()
        calculator = PaymentsCalculator(validator)
        repository = PaymentsRepositoryImpl(dao, calculator, validator)
    }

    companion object {
        fun getDto(list: List<SavedPaymentEntity>): GetSavedPaymentsDto {
            var totalAmount: Float = 0.0f
            var totalTip: Float = 0.0f
            var totalPerson = 0
            if (list.isEmpty()) {
                return GetSavedPaymentsDto(totalAmount, totalTip, totalPerson, list)
            }
            val mList = list.toMutableList()
            for (entity in list) {
                totalAmount += entity.amount
                totalTip += (entity.tipPercentage.toFloat() / 100f) * entity.amount
                totalPerson += entity.persons
            }
            return GetSavedPaymentsDto(totalAmount, totalTip, totalPerson, mList)
        }
    }

    @Test
    fun testObservePaymentsListEmptyList() = runTest {
        val emptyList = emptyList<SavedPaymentEntity>()
        every { dao.observePaymentsList() } returns flowOf(emptyList)

        val result = repository.observePaymentsList().first()
        Assert.assertEquals(true, result.list.isEmpty())
        Assert.assertEquals(0f, result.totalAmount)
        Assert.assertEquals(0f, result.totalTip)
        Assert.assertEquals(0, result.totalPerson)
    }

    @Test
    fun testObservePaymentsListValidPayments() = runTest {
        val entity1 = SavedPaymentEntity(1, System.currentTimeMillis(), 10.0f, 2, 15, null)
        val entity2 = SavedPaymentEntity(2, System.currentTimeMillis(), 20.0f, 3, 10, null)
        val list = listOf(entity1, entity2)

        every { dao.observePaymentsList() } returns flowOf(list)

        val result = repository.observePaymentsList().first()
        assertEquals(list.size, result.list.count())
        assertEquals((entity1.amount + entity2.amount), result.totalAmount)
        assertEquals((entity1.persons + entity2.persons), result.totalPerson)
    }

    @Test
    fun testSavePaymentValidEntity() = runTest {
        val entity = SavedPaymentEntity(1, System.currentTimeMillis(), 10.0f, 2, 15, null)
        every { dao.observePaymentsList() } returns flowOf(listOf(entity))
        every { dao.savePayment(entity) } returns Unit

        repository.savePayment(entity)

        val result = repository.observePaymentsList().first()
        verify { dao.savePayment(entity) }
        Assert.assertEquals(false, result.list.isEmpty())
        assertEquals(entity.amount, result.totalAmount)
        assertEquals(entity.persons, result.totalPerson)
    }

    @Test
    fun testSavePaymentInvalidEntity() = runTest {
        val entity = SavedPaymentEntity(1, System.currentTimeMillis(), 0f, 2, 15, null) // Invalid amount
        every { dao.observePaymentsList() } returns flowOf(emptyList())
        every { dao.savePayment(entity) } returns Unit

        repository.savePayment(entity)

        //verify { dao.savePayment(entity) wasNot Called} // Payment not saved
        val result = repository.observePaymentsList().first()
        Assert.assertEquals(true, result.list.isEmpty())
        assertEquals(entity.amount, result.totalAmount)
        Assert.assertEquals(0, result.totalPerson)
    }


    @Test
    fun testDeletePayments() = runTest{
        val entity1 = SavedPaymentEntity(0, System.currentTimeMillis(), 10.0f, 2, 15, null)
        val list = listOf(entity1)
        every { dao.observePaymentsList() } returns flowOf(list)
        every { dao.deletePayment(entity1) } returns Unit

        repository.deletePayment(entity1)

        verify { dao.deletePayment(entity1) }
    }
}