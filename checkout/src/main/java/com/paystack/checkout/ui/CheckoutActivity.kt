package com.paystack.checkout.ui

import androidx.appcompat.app.AppCompatActivity

class CheckoutActivity: AppCompatActivity() {

    override fun onResume() {
        super.onResume()
    }

    companion object {
        const val EXTRA_CHARGE_PARAMS = "charge_params"
    }
}