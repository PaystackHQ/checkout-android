package com.paystack.checkout.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.paystack.checkout.PaystackCheckout
import com.paystack.checkout.example.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        binding.btnPay.setOnClickListener {
            PaystackCheckout(this, "Hekajfubfubf")
                .charge("ask4myk@gmail.com", 10000, "NGN")
        }

        setContentView(binding.root)
    }
}