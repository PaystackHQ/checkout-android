package com.paystack.checkout

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.paystack.checkout.model.ChargeParams
import com.paystack.checkout.ui.PayWithCheckout

class PaystackCheckout(private val activity: AppCompatActivity, private val publicKey: String) {

    fun charge(email: String, amount: Long, currency: String) {
        val params = ChargeParams(publicKey, email, amount, currency)
        activity.registerForActivityResult(PayWithCheckout()) {
            Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
        }.launch(params)
    }
}