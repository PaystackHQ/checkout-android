package com.paystack.checkout

import com.paystack.checkout.model.Transaction

interface CheckoutResultListener {
    fun onSuccess(transaction: Transaction)

    fun onError(transaction: Transaction)

    fun onCancelled()
}