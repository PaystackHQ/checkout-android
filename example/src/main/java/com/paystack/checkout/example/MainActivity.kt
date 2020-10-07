package com.paystack.checkout.example

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.paystack.checkout.CheckoutResultListener
import com.paystack.checkout.PaystackCheckout
import com.paystack.checkout.example.databinding.MainActivityBinding
import com.paystack.checkout.model.Transaction

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPay.setOnClickListener {
            val checkoutResultListener = object : CheckoutResultListener {
                override fun onSuccess(transaction: Transaction) {
                    Toast.makeText(
                        this@MainActivity,
                        "Transaction complete - Ref: ${transaction.reference}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.i(TAG, "Transaction Success")
                }

                override fun onError(exception: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Transaction failed",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e(TAG, exception.message.orEmpty(), exception)
                }

                override fun onCancelled() {
                    Toast.makeText(
                        this@MainActivity,
                        "Transaction cancelled by user",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e(TAG, "Cancelled!")
                }
            }
            val checkout = PaystackCheckout.Builder(
                this,
                publicKey = "pk_test_d8a7e69bada4d0a25d0e62b571b259208c9f4328",
                activityResultRegistry,
                "ask4myk@gmail.com",
                10000,
                "NGN",
                "07019133867",
            ).build()

            checkout.charge(checkoutResultListener)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}