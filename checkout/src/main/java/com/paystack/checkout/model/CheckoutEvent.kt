package com.paystack.checkout.model

import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

enum class CheckoutEventType(val value: String) {
    close("close"),
    redirecting("checkout:redirecting"),
    loadedTransaction("loaded:transaction"),
    success("success")
}

sealed class CheckoutEvent

class Close : CheckoutEvent()
class Redirecting : CheckoutEvent()
@JsonClass(generateAdapter = true)
data class Success(val data: SuccessEventData): CheckoutEvent()
class LoadedTransaction : CheckoutEvent()