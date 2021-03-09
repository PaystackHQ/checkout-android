package com.paystack.checkout.ui.di

import com.paystack.checkout.data.di.dataModule
import com.paystack.checkout.ui.CheckoutViewModel.CheckoutViewModelFactory

internal object CheckoutContainer {

    val checkoutViewModelFactory = CheckoutViewModelFactory(dataModule().paystackRepository)
}
