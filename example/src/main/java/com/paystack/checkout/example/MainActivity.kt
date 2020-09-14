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

                override fun onError(transaction: Transaction) {
                    Log.e(TAG, "Transaction ERROR!!")
                }

                override fun onCancelled() {
                    Log.e(TAG, "Cancelled!")
                }
            }
            PaystackCheckout(this, "pk_test_5dc5329e757ef30cad09c61f19e911f059bdb474")
                .charge("ask4myk@gmail.com", 10000, "NGN", checkoutResultListener)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}