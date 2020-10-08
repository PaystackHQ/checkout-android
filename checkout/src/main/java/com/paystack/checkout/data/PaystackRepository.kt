package com.paystack.checkout.data

import com.paystack.checkout.model.ChargeParams
import com.paystack.checkout.model.Transaction

internal interface PaystackRepository {
    suspend fun initializeTransaction(params: ChargeParams): ApiResult<Transaction>
}
