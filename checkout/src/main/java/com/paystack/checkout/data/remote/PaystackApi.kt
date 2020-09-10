package com.paystack.checkout.data.remote

interface PaystackApi {

    suspend fun initializeTransaction(params: TransactionParams): TransactionInitResponse
}
