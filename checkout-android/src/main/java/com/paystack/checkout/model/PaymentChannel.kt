package com.paystack.checkout.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class PaymentChannel : Parcelable {
    card,
    bank,
    ussd,
    qr,
    mobile_money,
    bank_transfer,
    eft
}
