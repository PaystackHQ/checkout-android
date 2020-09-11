package com.paystack.checkout.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction(
    val id: Int,
    val accessCode: String,
    val amount: Long,
    val currency: String,
    val email: String,
    val transactionStatus: String
): Parcelable
