package com.paystack.checkout.data

import com.paystack.checkout.model.Transaction


interface PaystackRepository {

    suspend fun initializeTransaction(
        publicKey: String,
        email: String,
        amount: Long,
        currency: String
    ): ApiResult<Transaction>
}
