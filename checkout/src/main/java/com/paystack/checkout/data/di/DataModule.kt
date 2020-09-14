package com.paystack.checkout.data.di

import com.paystack.checkout.data.PaystackApiRepository
import com.paystack.checkout.data.PaystackRepository
import com.paystack.checkout.data.remote.di.apiComponent

fun dataModule() = DataModule

object DataModule : DataComponent {
    override val paystackRepository: PaystackRepository =
        PaystackApiRepository(apiComponent().paystackApi)
}

interface DataComponent {
    val paystackRepository: PaystackRepository
}
