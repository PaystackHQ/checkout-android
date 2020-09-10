package com.paystack.checkout.data.remote

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class TransactionInitResponse(
    val status: String,
    val message: String,
    val id: Int,
    @Json(name = "access_code")
    val accessCode: String,
    val amount: Long,
    val currency: String,
    val email: String,
    @Json(name = "merchant_id")
    val merchantId: Int,
    @Json(name = "merchant_key")
    val merchantKey: String,
    @Json(name = "merchant_name")
    val merchantName: String,
    @Json(name = "transaction_status")
    val transactionStatus: String,
    @Json(name = "testmode")
    val testMode: Boolean
)
