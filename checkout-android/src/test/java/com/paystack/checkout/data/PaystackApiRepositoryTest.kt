package com.paystack.checkout.data

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.paystack.checkout.data.remote.PaystackApi
import com.paystack.checkout.data.remote.TransactionInitResponse
import com.paystack.checkout.model.ChargeParams
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
internal class PaystackApiRepositoryTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    lateinit var paystackApi: PaystackApi

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun initializeTransaction_whenInvokedWithParameters_callsPaystackApiWithCorrectData() {
        val paystackApiRepository = create()

        testDispatcher.runBlockingTest {
            whenever(paystackApi.initializeTransaction(TEST_CHARGE_PARAMS.toRequestMap())).thenReturn(
                transactionInitResponse
            )

            paystackApiRepository.initializeTransaction(TEST_CHARGE_PARAMS)
            verify(paystackApi).initializeTransaction(TEST_CHARGE_PARAMS.toRequestMap())
        }
    }

    @Test
    fun initializeTransaction_whenTransactionInitSucceeds_returnsOkResult() {
        val paystackApiRepository = create()
        testDispatcher.runBlockingTest {

            whenever(paystackApi.initializeTransaction(TEST_CHARGE_PARAMS.toRequestMap())).thenReturn(
                transactionInitResponse
            )

            val result = paystackApiRepository.initializeTransaction(TEST_CHARGE_PARAMS)
            assert(result is Ok)
            val transaction = result.get()
            if (transaction == null) {
                fail()
                return@runBlockingTest
            }
            assertEquals(transaction.email, testEmail)
            assertEquals(transaction.amount, testAmount)
            assertEquals(transaction.currency, testCurrency)
            assert(transaction.accessCode.isNotEmpty())
            assertNotEquals(transaction.id, 0)
        }
    }

    private fun create() = PaystackApiRepository(paystackApi)

    companion object {
        const val testPublicKey = "pk_live_123445677555"
        const val testEmail = "michael@paystack.com"
        const val testAmount = 10000L
        const val testCurrency = "NGN"

        val TEST_CHARGE_PARAMS = ChargeParams("pk_test_test_key_value", testEmail, 10000L, "NGN")
        val transactionInitResponse = TransactionInitResponse(
            status = "success",
            message = "Successful",
            id = 75947,
            accessCode = "h2vw29m4vouct40",
            amount = 10000L,
            currency = "NGN",
            email = testEmail,
            transactionStatus = "abandoned"
        )
    }
}
