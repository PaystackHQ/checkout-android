package com.paystack.checkout.data

import com.github.michaelbull.result.map
import com.github.michaelbull.result.runCatching
import com.paystack.checkout.data.remote.PaystackApi
import com.paystack.checkout.data.remote.TransactionParams
import com.paystack.checkout.model.Transaction

class PaystackApiRepository(private val paystackApi: PaystackApi) : PaystackRepository {
    override suspend fun initializeTransaction(
        publicKey: String,
        email: String,
        amount: Long,
        currency: String
    ): ApiResult<Transaction> {
        val params = TransactionParams(
            publicKey,
            email,
            amount,
            currency,
        )
        return runCatching { paystackApi.initializeTransaction(params) }
            .map { TransactionResponseMapper.mapFromResponse(it) }
    }
}
