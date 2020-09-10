package com.paystack.checkout

import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import com.paystack.checkout.ui.CheckoutActivity
import org.junit.After
import org.junit.Before
import org.junit.Test

class PaystackCheckoutTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun charge() {
        ActivityScenario.launch(CheckoutActivity::class.java).use { activity ->
            assert(activity is AppCompatActivity)
        }
    }
}