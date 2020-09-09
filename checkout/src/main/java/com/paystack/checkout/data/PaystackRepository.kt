package com.paystack.checkout.data

import com.github.michaelbull.result.Result
import com.paystack.checkout.data.remote.TransactionInitResponse
import com.paystack.checkout.model.Transaction

typealias ApiResult<T> = Result<T, Throwable>

interface PaystackRepository {

    suspend fun initializeTransaction(
        publicKey: String,
        email: String,
        amount: Long,
        currency: String
    ): ApiResult<Transaction>
}