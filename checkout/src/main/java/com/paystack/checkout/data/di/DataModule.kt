package com.paystack.checkout.data.di

import com.paystack.checkout.data.PaystackApiRepository
import com.paystack.checkout.data.PaystackRepository
import com.paystack.checkout.data.remote.di.apiComponent

internal fun dataModule() = DataModule

internal object DataModule : DataComponent {
    override val paystackRepository: PaystackRepository =
        PaystackApiRepository(apiComponent().paystackApi)
}

internal interface DataComponent {
    val paystackRepository: PaystackRepository
}
