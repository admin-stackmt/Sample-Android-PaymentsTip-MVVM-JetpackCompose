package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain

import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.database.AppDatabase
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.repository.PaymentsRepository
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.sources.local.PaymentHistoryDao
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.usecase.AddPayment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
class AddPaymentTest {

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("db_test")
    lateinit var db: AppDatabase

    @Inject
    @Named("repo_test")
    lateinit var repository: PaymentsRepository

    @Inject
    @Named("dao_test")
    lateinit var dao: PaymentHistoryDao

    @Inject
    @Named("payments_test")
    lateinit var payments: PaymentsUseCases

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `test_add_payment_success`() = runTest {
        dao.clearAll()

        val timestamp = System.currentTimeMillis()
        val amount = 100.0f
        val persons = 3
        val tipPercentage = 15

        val flow = payments.addPayment(timestamp, amount, persons, tipPercentage, null)
        flow.collect { state ->
            Assert.assertTrue(state is AddPayment.States.Added)
        }

        // Verify if the payment was saved in the database
        val result = repository.observePaymentsList().first()
        Assert.assertEquals(1, result.list.size)
        Assert.assertEquals(timestamp, result.list[0].timestamp)
        Assert.assertEquals(amount, result.list[0].amount)
        Assert.assertEquals(persons, result.list[0].persons)
        Assert.assertEquals(tipPercentage, result.list[0].tipPercentage)
        Assert.assertEquals(amount, result.totalAmount)
        Assert.assertEquals(persons, result.totalPerson)
    }

    @Test
    fun `test_add_payment_error_null_values`() = runTest {
        dao.clearAll()

        val timestamp = null
        val amount = null
        val persons: Int? = null
        val tipPercentage = null

        validateErrorState(timestamp, amount, persons, tipPercentage)
    }

    @Test
    fun `test_add_payment_error_negative_amount`() = runTest {
        dao.clearAll()

        // Test with negative amount
        val timestamp1 = System.currentTimeMillis()
        val amount1 = -1f
        val persons1 = 2
        val tipPercentage1 = 10

        validateErrorState(timestamp1, amount1, persons1, tipPercentage1)
    }

    @Test
    fun `test_add_payment_error_zero_amount`() = runTest {
        dao.clearAll()

        // Test with negative amount
        val timestamp1 = System.currentTimeMillis()
        val amount1 = 0f
        val persons1 = 2
        val tipPercentage1 = 10

        validateErrorState(timestamp1, amount1, persons1, tipPercentage1)
    }

    @Test
    fun `test_add_payment_error_negative_persons`() = runTest {
        dao.clearAll()

        // Test with negative amount
        val timestamp1 = System.currentTimeMillis()
        val amount1 = 0f
        val persons1 = -2
        val tipPercentage1 = 10

        validateErrorState(timestamp1, amount1, persons1, tipPercentage1)
    }

    @Test
    fun `test_add_payment_error_zero_persons`() = runTest {
        dao.clearAll()

        // Test with negative amount
        val timestamp1 = System.currentTimeMillis()
        val amount1 = 0f
        val persons1 = 0
        val tipPercentage1 = 10

        validateErrorState(timestamp1, amount1, persons1, tipPercentage1)
    }

    @Test
    fun `test_add_payment_error_negative_tip`() = runTest {
        dao.clearAll()

        // Test with negative amount
        val timestamp1 = System.currentTimeMillis()
        val amount1 = 0f
        val persons1 = 0
        val tipPercentage1 = -10

        validateErrorState(timestamp1, amount1, persons1, tipPercentage1)
    }

    @Test
    fun `test_add_payment_error_zero_tip`() = runTest {
        dao.clearAll()

        // Test with negative amount
        val timestamp1 = System.currentTimeMillis()
        val amount1 = 0f
        val persons1 = 0
        val tipPercentage1 = 0

        validateErrorState(timestamp1, amount1, persons1, tipPercentage1)
    }

    private fun validateErrorState(timestamp: Long?, amount: Float?, persons: Int?, tipPercentage: Int?) = runTest {
        dao.clearAll()

        val flow = payments.addPayment(timestamp, amount, persons, tipPercentage, null)
        flow.collect { state ->
            Assert.assertTrue(state is AddPayment.States.Error)
        }

        // Verify that no payment was saved in the database
        val result = repository.observePaymentsList().first()
        Assert.assertEquals(0, result.list.size)
        Assert.assertEquals(0f, result.totalAmount)
        Assert.assertEquals(0, result.totalPerson)
        Assert.assertEquals(0f, result.totalTip)
    }
}