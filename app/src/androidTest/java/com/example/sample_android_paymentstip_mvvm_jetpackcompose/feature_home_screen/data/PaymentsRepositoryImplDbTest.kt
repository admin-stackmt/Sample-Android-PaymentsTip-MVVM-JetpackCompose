package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data

import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.database.AppDatabase
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.GetSavedPaymentsDto
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.SavedPaymentEntity
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.repository.PaymentsRepository
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.sources.local.PaymentHistoryDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class PaymentsRepositoryImplDbTest {

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

    @Before
    fun setUp() {
        hiltRule.inject()
        dao.clearAll()
    }

    @After
    fun tearDown() {
        db.close()
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
        dao.clearAll()

        val result = repository.observePaymentsList().first()
        Assert.assertEquals(true, result.list.isEmpty())
        Assert.assertEquals(0f, result.totalAmount)
        Assert.assertEquals(0f, result.totalTip)
        Assert.assertEquals(0, result.totalPerson)
    }

    @Test
    fun testObservePaymentsListValidPayments() = runTest {
        dao.clearAll()

        val entity1 = SavedPaymentEntity(1, System.currentTimeMillis(), 10.0f, 2, 15, null)
        val entity2 = SavedPaymentEntity(2, System.currentTimeMillis(), 20.0f, 3, 10, null)
        val list = listOf(entity1, entity2)

        repository.savePayment(entity1)
        repository.savePayment(entity2)

        val result = repository.observePaymentsList().first()
        println(result.list)
        assertEquals(list.size, result.list.count())
        assertEquals((entity1.amount + entity2.amount), result.totalAmount)
        assertEquals((entity1.persons + entity2.persons), result.totalPerson)
    }


    @Test
    fun testSavePaymentValidEntity() = runTest {
        dao.clearAll()

        val entity = SavedPaymentEntity(3, System.currentTimeMillis(), 100.0f, 2, 15, null)
        repository.savePayment(entity)

        val result = repository.observePaymentsList().first()
        Assert.assertEquals(false, result.list.isEmpty())
        assertEquals(entity.amount, result.totalAmount)
        assertEquals(entity.persons, result.totalPerson)
    }


    @Test
    fun testSavePaymentInvalidEntity() = runTest {
        dao.clearAll()

        val entity = SavedPaymentEntity(4, System.currentTimeMillis(), 0f, 2, 15, null) // Invalid amount

        repository.savePayment(entity)

        val result = repository.observePaymentsList().first()
        Assert.assertEquals(true, result.list.isEmpty())
        assertEquals(entity.amount, result.totalAmount)
        Assert.assertEquals(0, result.totalPerson)
    }

    @Test
    fun testDeletePayments() = runTest{
        dao.clearAll()

        val entity1 = SavedPaymentEntity(5, System.currentTimeMillis(), 10.0f, 2, 15, null)
        repository.savePayment(entity1)

        repository.deletePayment(entity1)
        val result = repository.observePaymentsList().first()
        Assert.assertEquals(true, result.list.isEmpty())
        Assert.assertEquals(0f, result.totalAmount)
        Assert.assertEquals(0, result.totalPerson)
    }
}