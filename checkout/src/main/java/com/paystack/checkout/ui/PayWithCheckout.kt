package com.paystack.checkout.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.paystack.checkout.model.ChargeParams

class PayWithCheckout : ActivityResultContract<ChargeParams, Boolean>() {
    override fun createIntent(context: Context, input: ChargeParams?): Intent {
        return Intent(context, CheckoutActivity::class.java).apply {
            putExtra(CheckoutActivity.EXTRA_CHARGE_PARAMS, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        if (resultCode != Activity.RESULT_OK) {
            return false
        }

        return true
    }
}