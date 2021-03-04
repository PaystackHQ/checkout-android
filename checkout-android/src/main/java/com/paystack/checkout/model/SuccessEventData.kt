package com.paystack.checkout.model

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
internal data class SuccessEventData(
    val reference: String,
    val status: String
)
