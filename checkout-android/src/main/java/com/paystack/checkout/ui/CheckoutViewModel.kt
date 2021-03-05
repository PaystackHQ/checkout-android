package com.paystack.checkout.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.paystack.checkout.data.PaystackRepository
import com.paystack.checkout.di.Factory
import com.paystack.checkout.model.ChargeParams
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class CheckoutViewModel(private val paystackRepository: PaystackRepository) : ViewModel() {

    private val _state = MutableStateFlow(CheckoutViewState())
    val state: LiveData<CheckoutViewState>
        get() = _state.asLiveData()

    fun currentState() = _state.value

    fun initializeTransaction(params: ChargeParams) {
        viewModelScope.launch {
            _state.value = currentState().copy(isLoading = true)
            when (val result = paystackRepository.initializeTransaction(params)) {
                is Ok -> {
                    _state.value = currentState().copy(
                        isLoading = false,
                        transaction = Consumable(result.value)
                    )
                }
                is Err -> {
                    _state.value = currentState().copy(
                        isLoading = false,
                        initError = Consumable(result.error)
                    )
                }
            }
        }
    }

    class CheckoutViewModelFactory(private val paystackRepository: PaystackRepository) :
        Factory<CheckoutViewModel> {
        override fun create(): CheckoutViewModel {
            return CheckoutViewModel(paystackRepository)
        }
    }
}
