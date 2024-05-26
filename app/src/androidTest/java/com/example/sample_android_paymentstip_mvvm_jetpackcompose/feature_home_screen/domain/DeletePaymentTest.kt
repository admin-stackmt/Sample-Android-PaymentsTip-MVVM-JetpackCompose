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
class DeletePaymentTest {

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
    fun `test_delete_payment_success`() = runTest {
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
        val getDto = repository.observePaymentsList().first()
        Assert.assertEquals(1, getDto.list.size)
        repository.deletePayment(getDto.list[0])
        val result = repository.observePaymentsList().first()
        Assert.assertEquals(0f, result.totalAmount)
        Assert.assertEquals(0, result.totalPerson)
    }
}