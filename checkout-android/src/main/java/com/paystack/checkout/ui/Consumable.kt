package com.paystack.checkout.ui

/**
 * Wrapper for an object that should be read only once
 * */
internal class Consumable<out T>(private val content: T) {

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
     * Runs [consumer] if content has not been consumed
     *
     * @return [true] if consumer runs successfully and [false] otherwise
     */
    fun consumeIfAvailable(consumer: (T) -> Unit): Boolean {
        val content = readContent()
        if (content != null) {
            consumer(content)
            return true
        }

        return false
    }

    /**
     * Returns the content, even if it's already been used.
     */
    fun peekContent(): T = content
}
