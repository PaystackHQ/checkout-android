package com.paystack.checkout.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class ChargeResult : Parcelable {
    @Parcelize
    data class Success(val transaction: Transaction) : ChargeResult()

    @Parcelize
    data class Error(val transaction: Transaction ) : ChargeResult()

    @Parcelize
    object Cancelled : ChargeResult()
}