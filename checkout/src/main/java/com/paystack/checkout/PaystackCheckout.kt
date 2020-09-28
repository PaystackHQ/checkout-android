package com.paystack.checkout

import androidx.activity.result.ActivityResultRegistry
import androidx.appcompat.app.AppCompatActivity
import com.paystack.checkout.model.ChargeParams
import com.paystack.checkout.model.ChargeResult
import com.paystack.checkout.ui.PayWithCheckout

class PaystackCheckout {
    private val activity: AppCompatActivity
    private val activityResultRegistry: ActivityResultRegistry
    private val publicKey: String

    constructor(activity: AppCompatActivity, publicKey: String) {
        this.activity = activity
        this.activityResultRegistry = activity.activityResultRegistry
        this.publicKey = publicKey
    }

    constructor(
        activity: AppCompatActivity,
        publicKey: String,
        activityResultRegistry: ActivityResultRegistry
    ) {
        this.activity = activity
        this.activityResultRegistry = activityResultRegistry
        this.publicKey = publicKey
    }

    fun charge(
        email: String,
        amount: Long,
        currency: String,
        resultListener: CheckoutResultListener
    ) {
        val params = ChargeParams(publicKey, email, amount, currency)
        activity.registerForActivityResult(
            PayWithCheckout(),
            activityResultRegistry
        ) { chargeResult ->
            when (chargeResult) {
                is ChargeResult.Success -> {
                    resultListener.onSuccess(chargeResult.transaction)
                }
                is ChargeResult.Error -> {
                    resultListener.onError(chargeResult.exception, chargeResult.transaction)
                }
                ChargeResult.Cancelled -> {
                    resultListener.onCancelled()
                }
            }
        }.launch(params)
    }
}