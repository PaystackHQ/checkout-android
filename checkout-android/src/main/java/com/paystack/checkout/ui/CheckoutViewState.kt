package com.paystack.checkout.ui

import com.paystack.checkout.model.Transaction

internal data class CheckoutViewState(
    val isLoading: Boolean = true,
    val initError: Consumable<Throwable>? = null,
    val transaction: Consumable<Transaction>? = null,
)
