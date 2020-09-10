package com.paystack.checkout.data.remote

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class TransactionParams(
    val key: String,
    val email: String,
    val amount: Long,
    val currency: String,
)
