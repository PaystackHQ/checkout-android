package com.paystack.checkout.data.remote

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
internal data class TransactionInitResponse(
    val status: String,
    val message: String,
    val id: Int,
    @Json(name = "access_code")
    val accessCode: String,
    val amount: Long,
    val currency: String,
    val email: String,
    @Json(name = "transaction_status")
    val transactionStatus: String,
)
