package com.paystack.checkout.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class Transaction(
    val id: Int,
    val accessCode: String,
    val amount: Long,
    val currency: String,
    val email: String,
    val reference: String? = null
) : Parcelable
