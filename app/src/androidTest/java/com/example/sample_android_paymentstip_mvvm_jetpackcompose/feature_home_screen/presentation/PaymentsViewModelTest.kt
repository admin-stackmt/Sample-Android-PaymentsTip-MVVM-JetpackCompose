package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.presentation

import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.database.AppDatabase
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils.PaymentRecordValidatorImpl
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils.PaymentsCalculator
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.sources.local.PaymentHistoryDao
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.PaymentsUseCases
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.presentation.viewmodel.PaymentsViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class PaymentsViewModelTest {

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("payments_test")
    lateinit var payments: PaymentsUseCases

    @Inject
    @Named("db_test")
    lateinit var db: AppDatabase

    @Inject
    @Named("dao_test")
    lateinit var dao: PaymentHistoryDao

    private lateinit var viewModel: PaymentsViewModel

    private val testCoroutineDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @Before
    fun setup() {
        hiltRule.inject()
        Dispatchers.setMain(testCoroutineDispatcher)
        dao.clearAll()
        viewModel = PaymentsViewModel(payments, PaymentsCalculator(PaymentRecordValidatorImpl()))
    }

    @After
    fun tearDown() {
        db.close()
        Dispatchers.resetMain()
    }


    //Todo: Add test cases

    /* @Test
     fun test_payments_list_empty_state() = runTest {
         dao.clearAll()

         val resultDto = viewModel.paymentsDtoObservable.value
         val resultPaymentScreenState = viewModel.paymentsScreenState.value

         assertEquals(true, resultDto.list.isEmpty())
         assertEquals(0f, resultDto.totalAmount)
         assertEquals(0, resultDto.totalPerson)

         assertEquals(true, resultPaymentScreenState is PaymentsScreenStates.Empty)
     }

     @OptIn(ExperimentalCoroutinesApi::class)
     @Test
     fun test_payments_list_loaded_state() {
         dao.clearAll()

         val entity1 = SavedPaymentEntity(1, System.currentTimeMillis(), 10.0f, 2, 15)
         val entity2 = SavedPaymentEntity(2, System.currentTimeMillis(), 20.0f, 3, 10)
         dao.savePayment(entity1)
         dao.savePayment(entity2)

         var resultDto: GetSavedPaymentsDto? = viewModel.paymentsDtoObservable.value
         var resultPaymentScreenState: PaymentsScreenStates? = viewModel.paymentsScreenState.value

         *//*val job = launch {
            viewModel.paymentsList.collect{
                resultDto = it
            }
            *//**//*viewModel.paymentsScreenState.collect{
                resultPaymentScreenState = it
            }*//**//*
        }*//*

        //advanceUntilIdle()
        assertEquals(false, resultDto!!.list.isEmpty())
        assertEquals(2, resultDto!!.list.size)
        assertEquals(entity1.amount + entity2.amount, resultDto!!.totalAmount)
        assertEquals(entity1.persons + entity2.persons, resultDto!!.totalPerson)
        //assertEquals(true, resultPaymentScreenState is PaymentsScreenStates.Loaded)

        //job.cancel()
    }

    @Test
    fun test_payments_list_loaded_state2() = runTest {
        dao.clearAll()

        val entity1 = SavedPaymentEntity(1, System.currentTimeMillis(), 10.0f, 2, 15)
        val entity2 = SavedPaymentEntity(2, System.currentTimeMillis(), 20.0f, 3, 10)
        dao.savePayment(entity1)
        dao.savePayment(entity2)

        var resultDto: GetSavedPaymentsDto? = viewModel.paymentsDtoObservable.value
        viewModel.paymentsDtoObservable.collectLatest{
            resultDto = it
            assertEquals(false, resultDto!!.list.isEmpty())
            assertEquals(2, resultDto!!.list.size)
            assertEquals(entity1.amount + entity2.amount, resultDto!!.totalAmount)
            assertEquals(entity1.persons + entity2.persons, resultDto!!.totalPerson)
        }
    }

*/


    /*
    @Test
    fun getPaymentsScreenState() {
        dao.clearAll()

    }

    @Test
    fun getAddPaymentStates() {
    }

    @Test
    fun getDeletePaymentStates() {
    }

    @Test
    fun onEvent() {
    }*/
}