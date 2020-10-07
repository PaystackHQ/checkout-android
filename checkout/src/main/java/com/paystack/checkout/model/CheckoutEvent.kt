package com.paystack.checkout.model

import com.squareup.moshi.JsonClass

enum class CheckoutEventType(val value: String) {
    close("close"),
    redirecting("checkout:redirecting"),
    loadedTransaction("loaded:transaction"),
    success("success")
}

sealed class CheckoutEvent

class CloseEvent : CheckoutEvent()
class RedirectingEvent : CheckoutEvent()
class ErrorEvent(val exception: Throwable): CheckoutEvent()
@JsonClass(generateAdapter = true)
data class SuccessEvent(val data: SuccessEventData) : CheckoutEvent()
class LoadedTransactionEvent : CheckoutEvent()