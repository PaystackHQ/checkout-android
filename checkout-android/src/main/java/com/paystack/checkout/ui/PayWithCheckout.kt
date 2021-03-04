package com.paystack.checkout.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.paystack.checkout.model.ChargeParams
import com.paystack.checkout.model.ChargeResult

internal class PayWithCheckout : ActivityResultContract<ChargeParams, ChargeResult>() {
    override fun createIntent(context: Context, input: ChargeParams?): Intent {
        return Intent(context, CheckoutActivity::class.java).apply {
            putExtra(CheckoutActivity.EXTRA_CHARGE_PARAMS, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ChargeResult {
        if (resultCode == Activity.RESULT_CANCELED) {
            return ChargeResult.Cancelled
        }

        if (resultCode == Activity.RESULT_OK && intent != null) {
            return intent.getParcelableExtra(CheckoutActivity.EXTRA_TRANSACTION_RESULT)
                ?: error(NO_RESULT_ERROR)
        }

        error(NO_RESULT_ERROR)
    }

    companion object {
        const val NO_RESULT_ERROR =
            "No transaction result. This should never happen. Please report issue to Paystack"
    }
}
