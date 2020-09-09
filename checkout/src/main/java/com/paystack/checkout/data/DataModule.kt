package com.paystack.checkout.data

import com.paystack.checkout.data.remote.apiComponent


object DataModule : DataComponent {
    override val paystackRepository: PaystackRepository =
        PaystackApiRepository(apiComponent().paystackApi)
}

interface DataComponent {
    val paystackRepository: PaystackRepository
}
