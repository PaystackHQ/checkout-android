package com.paystack.checkout.model

data class Transaction(
    val id: Int,
    val accessCode: String,
    val amount: Long,
    val currency: String,
    val email: String,
    val transactionStatus: String
)
