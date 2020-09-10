package com.paystack.checkout.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface PaystackApi {

    @GET("/checkout/request_inline")
    suspend fun initializeTransaction(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): TransactionInitResponse
}
