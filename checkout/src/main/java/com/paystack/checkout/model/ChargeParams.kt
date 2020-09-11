package com.paystack.checkout.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChargeParams(
    val publicKey: String,
    val email: String,
    val amount: Long,
    val currency: String,
) : Parcelable