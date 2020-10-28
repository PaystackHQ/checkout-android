package com.paystack.checkout.data

import com.github.michaelbull.result.map
import com.github.michaelbull.result.runCatching
import com.paystack.checkout.data.remote.PaystackApi
import com.paystack.checkout.model.ChargeParams
import com.paystack.checkout.model.Transaction

internal class PaystackApiRepository(private val paystackApi: PaystackApi) : PaystackRepository {
    override suspend fun initializeTransaction(params: ChargeParams): ApiResult<Transaction> {
        return runCatching { paystackApi.initializeTransaction(params.toRequestMap()) }
            .map { TransactionResponseMapper.mapFromResponse(it) }
    }
}
