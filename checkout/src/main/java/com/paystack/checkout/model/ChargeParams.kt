package com.paystack.checkout.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChargeParams(
    val publicKey: String,
    val email: String,
    val amount: Long,
    val currency: String,
    val channels: List<PaymentChannel>? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val plan: String? = null,
    val subAccount: String? = null,
    val customerCode: String? = null,
    val reference: String? = null,
    val paymentPage: String? = null,
    val paymentRequest: String? = null,
    val bearer: String? = null,
    val metadata: String? = null,
    val transactionCharge: Long? = null,
    val orderId: Long? = null,
) : Parcelable {
    fun toRequestMap() = mapOf(
        "key" to publicKey,
        "email" to email,
        "amount" to amount,
        "currency" to currency,
        "channels" to channels,
        "firstName" to firstName,
        "lastName" to lastName,
        "phone" to phone,
        "plan" to plan,
        "subAccount" to subAccount,
        "customerCode" to customerCode,
        "reference" to reference,
        "paymentPage" to paymentPage,
        "paymentRequest" to paymentRequest,
        "bearer" to bearer,
        "metadata" to metadata,
        "transactionCharge" to transactionCharge,
        "orderId" to orderId,
    ).pruneNullValues()

    private fun <K, V> Map<K, V?>.pruneNullValues(): Map<K, V> {
        return filterValues { it != null } as Map<K, V>
    }
}