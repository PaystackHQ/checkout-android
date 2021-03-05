package com.paystack.checkout.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

internal sealed class ChargeResult : Parcelable {
    @Parcelize
    data class Success(val transaction: Transaction) : ChargeResult()

    @Parcelize
    data class Error(val exception: Throwable) : ChargeResult()

    @Parcelize
    object Cancelled : ChargeResult()
}
