package com.paystack.checkout.example

import android.os.Bundle
import android.util.Log
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
                    Log.e(TAG, "Transaction Success")
                }

                override fun onError(exception: Throwable?, transaction: Transaction?) {
                    Log.e(TAG, "Transaction ERROR!!")
                }

                override fun onCancelled() {
                    Log.e(TAG, "Cancelled!")
                }
            }
            PaystackCheckout(this, "pk_test_d8a7e69bada4d0a25d0e62b571b259208c9f4328")
                .charge("ask4myk@gmail.com", 10000, "NGN", checkoutResultListener)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}