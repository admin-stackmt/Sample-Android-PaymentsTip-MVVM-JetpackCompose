package com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.di

import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.database.AppDatabase
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils.PaymentRecordValidatorImpl
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils.PaymentsCalculator
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils.Validator
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.repository.PaymentsRepository
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.repository.PaymentsRepositoryImpl
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.sources.local.PaymentHistoryDao
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.PaymentsUseCases
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.usecase.AddPayment
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.usecase.DeletePayments
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.usecase.GetPayments
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PaymentHistoryModule {

    @Provides
    @Singleton
    fun providePaymentHistoryDao(database: AppDatabase): PaymentHistoryDao {
        return database.paymentHistoryDao
    }

    @Provides
    @Singleton
    fun provideValidator(): Validator {
        return PaymentRecordValidatorImpl()
    }

    @Provides
    @Singleton
    fun providePaymentsCalculator(validator: Validator): PaymentsCalculator {
        return PaymentsCalculator(validator)
    }

    @Provides
    @Singleton
    fun providePaymentsRepository(
        dao: PaymentHistoryDao,
        calculator: PaymentsCalculator,
        validator: Validator
    ): PaymentsRepository {
        return PaymentsRepositoryImpl(dao, calculator, validator)
    }

    @Provides
    @Singleton
    fun providePaymentsUseCase(
        repository: PaymentsRepository,
        validator: Validator
    ): PaymentsUseCases {
        return PaymentsUseCases(
            GetPayments(repository),
            AddPayment(repository, validator),
            DeletePayments(repository)
        )
    }

}