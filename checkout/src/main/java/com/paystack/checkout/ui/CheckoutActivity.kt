package com.paystack.checkout.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.paystack.checkout.model.Transaction
import kotlinx.coroutines.delay

class CheckoutActivity: AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            delay(5000)
            val intent = Intent().apply {
                val transaction = Transaction(
                    745857,
                    "hfjfjsjbwu42y",
                    30000,
                    "NGN",
                    "ask4myk@gmail.com",
                    "successful"
                )
                putExtra(EXTRA_TRANSACTION_RESULT, transaction)
            }
            setResult(Activity.RESULT_OK, )
        }
    }

    companion object {
        const val EXTRA_CHARGE_PARAMS = "charge_params"
        const val EXTRA_TRANSACTION_RESULT = "transaction_result"
    }
}