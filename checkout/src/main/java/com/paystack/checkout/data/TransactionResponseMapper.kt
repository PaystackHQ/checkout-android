package com.paystack.checkout.data

import com.paystack.checkout.data.remote.TransactionInitResponse
import com.paystack.checkout.model.Transaction

internal object TransactionResponseMapper {

    fun mapFromResponse(response: TransactionInitResponse): Transaction {
        return Transaction(
            id = response.id,
            accessCode = response.accessCode,
            amount = response.amount,
            currency = response.currency,
            email = response.email,
        )
    }
}
