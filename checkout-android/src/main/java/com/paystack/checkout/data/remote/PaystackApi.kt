package com.paystack.checkout.data.remote

import com.serjltt.moshi.adapters.Wrapped
import retrofit2.http.GET
import retrofit2.http.QueryMap

internal interface PaystackApi {

    @GET("/checkout/request_inline")
    @Wrapped(path = ["data"])
    suspend fun initializeTransaction(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): TransactionInitResponse
}
