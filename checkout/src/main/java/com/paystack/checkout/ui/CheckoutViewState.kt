package com.paystack.checkout.ui

import com.paystack.checkout.model.Transaction

data class CheckoutViewState(
     val isLoading: Boolean = true,
     val transaction: Consumable<Transaction>? = null
)