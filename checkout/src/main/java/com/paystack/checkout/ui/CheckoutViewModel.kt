package com.paystack.checkout.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.Ok
import com.paystack.checkout.data.PaystackRepository
import com.paystack.checkout.di.Factory
import com.paystack.checkout.model.ChargeParams
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModel(private val paystackRepository: PaystackRepository) : ViewModel() {

    private val _state = MutableStateFlow(CheckoutViewState())
    val state: LiveData<CheckoutViewState>
        get() = _state.asLiveData()

    private fun currentState() = _state.value

    fun initializeTransaction(params: ChargeParams) {
        viewModelScope.launch {
            _state.value = currentState().copy(isLoading = true)
            delay(3000)
            val result = paystackRepository.initializeTransaction(
                params.publicKey,
                params.email,
                params.amount,
                params.currency
            )
            when (result) {
                is Ok -> {
                    _state.value = currentState().copy(
                        isLoading = false,
                        transaction = Consumable(result.value)
                    )
                }
                else -> {
                    _state.value = currentState().copy(
                        isLoading = false,
                        //TODO: Error handling
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