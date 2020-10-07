package com.paystack.checkout.data

import com.paystack.checkout.model.ChargeParams
import com.paystack.checkout.model.Transaction

interface PaystackRepository {
    suspend fun initializeTransaction(params: ChargeParams): ApiResult<Transaction>
}
