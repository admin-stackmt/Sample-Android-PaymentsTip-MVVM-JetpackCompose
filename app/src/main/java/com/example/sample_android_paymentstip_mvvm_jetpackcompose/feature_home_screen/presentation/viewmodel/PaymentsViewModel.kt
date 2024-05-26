package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.presentation.viewmodel

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils.PaymentsCalculator
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.GetSavedPaymentsDto
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.InputCalculationsHolder
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.InputPaymentDto
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.SavedPaymentEntity
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.PaymentsUseCases
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.usecase.AddPayment
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.domain.usecase.GetPayments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val paymentsUseCases: PaymentsUseCases,
    private val calculator: PaymentsCalculator
) : ViewModel() {

    private var coroutineJob: Job? = null

    //payments list observable
    private val _paymentsDtoObservable = mutableStateOf<GetSavedPaymentsDto>(GetSavedPaymentsDto())
    val paymentsDtoObservable: State<GetSavedPaymentsDto>
        get() = _paymentsDtoObservable

    //add payment states observable
    private val _addPaymentStateObservable =
        mutableStateOf<AddPaymentStates>(AddPaymentStates.Reset)
    val addPaymentStateObservable: State<AddPaymentStates>
        get() = _addPaymentStateObservable

    //delete payment states observable
    private val _deletePaymentStateObservable =
        mutableStateOf<DeletePaymentStates>(DeletePaymentStates.Reset)
    val deletePaymentStateObservable: State<DeletePaymentStates>
        get() = _deletePaymentStateObservable

    //input payment dto observable
    private val _inputPaymentDtoObservable = mutableStateOf<InputPaymentDto>(InputPaymentDto())
    val inputPaymentDtoObservable: State<InputPaymentDto>
        get() = _inputPaymentDtoObservable

    //inputs calculations observable
    private val _inputCalculationsObservable =
        mutableStateOf<InputCalculationsHolder>(InputCalculationsHolder())
    val inputCalculationsObservable: State<InputCalculationsHolder>
        get() = _inputCalculationsObservable


    init {
        observePaymentsList()
    }

    fun callEvent(event: UIEvent) {
        clearCoroutineJob()
        when (event) {
            is UIEvent.SavePayment -> savePayment()
            is UIEvent.DecreaseNumPersons -> decreaseNumPersons()
            is UIEvent.IncreaseNumPersons -> increaseNumPersons()
            is UIEvent.ChangeAmount -> changeAmount(event.amount)
            is UIEvent.ChangeTipPercentage -> changeTipPercentage(event.tipPercentage)
            is UIEvent.ToggleTakePhotoCheckbox -> toggleTakePhotoEnabled()
            is UIEvent.DeletePayment -> deletePayment(event.item)
        }
    }

    private fun toggleTakePhotoEnabled() {
        coroutineJob = viewModelScope.launch {
            val newInputData =
                _inputPaymentDtoObservable.value.copy(isAttachPhotoEnabled = !_inputPaymentDtoObservable.value.isAttachPhotoEnabled)
            _inputPaymentDtoObservable.value = newInputData
        }
    }

    private fun changeAmount(amount: String) {
        coroutineJob = viewModelScope.launch {
            try {
                val newInputData = _inputPaymentDtoObservable.value.copy(amount = amount.toFloat())
                _inputPaymentDtoObservable.value = newInputData
                updateInputCalculations(newInputData)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateInputCalculations(newInputData: InputPaymentDto) {
        coroutineJob = viewModelScope.launch {
            _inputCalculationsObservable.value = InputCalculationsHolder(
                calculator.calculateTipAmount(newInputData.amount, newInputData.tipPercent),
                calculator.getTipPerPerson(
                    newInputData.amount,
                    newInputData.tipPercent,
                    newInputData.numPersons
                )
            )
        }
    }

    private fun changeTipPercentage(tipPercent: String) {
        coroutineJob = viewModelScope.launch {
            try {
                val newInputData =
                    _inputPaymentDtoObservable.value.copy(tipPercent = tipPercent.toInt())
                _inputPaymentDtoObservable.value = newInputData
                updateInputCalculations(newInputData)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun observePaymentsList() {
        viewModelScope.launch(Dispatchers.IO) {
            paymentsUseCases.getPayments().collect {
                when (it) {
                    is GetPayments.States.Fetched -> {
                        viewModelScope.launch {
                            _paymentsDtoObservable.value = it.data
                        }
                    }
                }
            }
        }
    }

    private fun decreaseNumPersons() {
        coroutineJob = viewModelScope.launch {
            if (_inputPaymentDtoObservable.value.numPersons > 1) {
                val numPersons = _inputPaymentDtoObservable.value.numPersons
                val newInputData =
                    _inputPaymentDtoObservable.value.copy(numPersons = numPersons - 1)
                _inputPaymentDtoObservable.value = newInputData
                updateInputCalculations(newInputData)
            }
        }
    }

    private fun increaseNumPersons() {
        coroutineJob = viewModelScope.launch {
            val numPersons = _inputPaymentDtoObservable.value.numPersons
            val newInputData = _inputPaymentDtoObservable.value.copy(numPersons = numPersons + 1)
            _inputPaymentDtoObservable.value = newInputData
            updateInputCalculations(newInputData)
        }
    }

    private fun savePayment() {
        viewModelScope.launch(Dispatchers.IO) {
            paymentsUseCases.addPayment(
                System.currentTimeMillis(),
                _inputPaymentDtoObservable.value.amount,
                _inputPaymentDtoObservable.value.numPersons,
                _inputPaymentDtoObservable.value.tipPercent,
                _inputPaymentDtoObservable.value.imageUri
            ).collect { state ->
                when (state) {
                    is AddPayment.States.Added -> {
                        viewModelScope.launch {
                            _addPaymentStateObservable.value = AddPaymentStates.Added
                            delay(500)
                            resetAddPaymentScreenValues()
                            _addPaymentStateObservable.value = AddPaymentStates.Reset
                        }
                    }

                    is AddPayment.States.Error -> {
                        viewModelScope.launch {
                            _addPaymentStateObservable.value =
                                AddPaymentStates.Error(state.errorMessage)
                            delay(500)
                            _addPaymentStateObservable.value = AddPaymentStates.Reset
                        }
                    }
                }
            }
        }
    }

    private fun deletePayment(
        item: SavedPaymentEntity
    ) {
        coroutineJob = viewModelScope.launch(Dispatchers.IO) {
            paymentsUseCases.deletePayments(item)
        }
    }

    private fun clearCoroutineJob() {
        coroutineJob?.cancel()
        coroutineJob = null
    }

    fun onTakePhotoError() {
        viewModelScope.launch {
            val newInputData =
                _inputPaymentDtoObservable.value.copy(isAttachPhotoEnabled = false, imageUri = null)
            _inputPaymentDtoObservable.value = newInputData
        }
    }

    fun savePaymentWithImage(uri: Uri) {
        if(uri.path == null) {
            onTakePhotoError()
        }else {
            viewModelScope.launch {
                val newInputData = _inputPaymentDtoObservable.value.copy(imageUri = uri.path)
                _inputPaymentDtoObservable.value = newInputData
                savePayment()
            }
        }
    }

    private fun resetAddPaymentScreenValues() {
        viewModelScope.launch {
            _inputPaymentDtoObservable.value = InputPaymentDto()
            _inputCalculationsObservable.value = InputCalculationsHolder()
        }
    }

}