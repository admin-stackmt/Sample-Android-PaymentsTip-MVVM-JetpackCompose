package com.example.sample_android_paymentstip_mvvm_jetpackcompose.di

import android.content.Context
import androidx.room.Room
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("db_test")
    @Singleton
    fun provideInMemoryDb(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    @Named("repo_test")
    fun providePaymentsRepository(
        dao: PaymentHistoryDao,
        calculator: PaymentsCalculator,
        validator: Validator
    ): PaymentsRepository {
        return PaymentsRepositoryImpl(dao, calculator, validator)
    }

    @Provides
    @Singleton
    @Named("dao_test")
    fun providePaymentHistoryDao(database: AppDatabase): PaymentHistoryDao {
        return database.paymentHistoryDao
    }

    @Provides
    @Singleton
    @Named("validator_test")
    fun provideValidator(): Validator {
        return PaymentRecordValidatorImpl()
    }

    @Provides
    @Singleton
    @Named("calculator_test")
    fun providePaymentsCalculator(validator: Validator): PaymentsCalculator {
        return PaymentsCalculator(validator)
    }

    @Provides
    @Singleton
    @Named("payments_test")
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