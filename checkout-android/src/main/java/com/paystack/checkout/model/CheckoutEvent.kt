package com.paystack.checkout.model

import com.squareup.moshi.JsonClass

internal enum class CheckoutEventType(val value: String) {
    close("close"),
    redirecting("checkout:redirecting"),
    loadedTransaction("loaded:transaction"),
    success("success")
}

internal sealed class CheckoutEvent

internal class CloseEvent : CheckoutEvent()

internal class RedirectingEvent : CheckoutEvent()

internal class ErrorEvent(val exception: Throwable) : CheckoutEvent()

@JsonClass(generateAdapter = true)
internal data class SuccessEvent(val data: SuccessEventData) : CheckoutEvent()

internal class LoadedTransactionEvent : CheckoutEvent()
