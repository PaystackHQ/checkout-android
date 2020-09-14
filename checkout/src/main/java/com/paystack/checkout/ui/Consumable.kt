package com.paystack.checkout.ui

/**
 * Wrapper for an object that should be read only once
 * */
class Consumable<out T>(private val content: T) {

    var hasBeenRead = false
        private set

    /**
     * Returns the content and prevents re-consumption.
     * @return T
     */
    fun readContent(): T? {
        return if (hasBeenRead) {
            null
        } else {
            hasBeenRead = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been used.
     */
    fun peekContent(): T = content
}
