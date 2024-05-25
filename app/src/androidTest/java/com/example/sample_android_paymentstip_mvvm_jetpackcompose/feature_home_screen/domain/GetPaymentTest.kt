package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain

import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.database.AppDatabase
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.SavedPaymentEntity
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.repository.PaymentsRepository
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.sources.local.PaymentHistoryDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
class GetPaymentTest {

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
    fun `test_get_payment_success`() = runTest {
        dao.clearAll()

        val entity1 = SavedPaymentEntity(1, System.currentTimeMillis(), 10.0f, 2, 15, null)
        val entity2 = SavedPaymentEntity(2, System.currentTimeMillis(), 20.0f, 3, 10, null)
        val list = listOf(entity1, entity2)

        dao.savePayment(entity1)
        dao.savePayment(entity2)

        // Verify if the payment was saved in the database
        //Todo: Fix this test
        /*val result = payments.getPayments().first()
        assertEquals(true , result is GetPayments.States.Loaded)
        assertEquals(2, (result as GetPayments.States.Loaded).data.list.size)
        assertEquals(30f, (result as GetPayments.States.Loaded).data.totalAmount)*/
    }

    @Test
    fun `test_get_payment_failed`() = runTest {
        dao.clearAll()

        // Verify if the payment was saved in the database
        val result = payments.getPayments().first()
        //Todo: Fix this test
        //assertEquals(true , result is GetPayments.States.Empty)
    }

}