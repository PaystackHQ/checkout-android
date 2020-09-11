package com.paystack.checkout

import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityOptionsCompat
import androidx.test.core.app.ActivityScenario
import com.nhaarman.mockitokotlin2.verify
import com.paystack.checkout.model.ChargeResult
import com.paystack.checkout.model.Transaction
import com.paystack.checkout.ui.CheckoutActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PaystackCheckoutTest {

    @Mock
    lateinit var resultListener: CheckoutResultListener

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun charge_whenChargeIsSuccessful_invokesResultListenerOnSuccess() {
        ActivityScenario.launch(CheckoutActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val chargeResult = ChargeResult.Success(SUCCESSFUL_TRANSACTION)
                val testRegistry = getTestResultRegistry(chargeResult)
                val paystackCheckout =
                    PaystackCheckout(activity, "pk_test_1234568695", testRegistry)
                paystackCheckout.charge(TEST_EMAIL, 10000, "NGN", resultListener)

                verify(resultListener).onSuccess(SUCCESSFUL_TRANSACTION)
            }
        }
    }

    @Test
    fun charge_whenChargeFails_invokesResultListenerOnError() {
        ActivityScenario.launch(CheckoutActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val chargeResult = ChargeResult.Error(FAILED_TRANSACTION)
                val testRegistry = getTestResultRegistry(chargeResult)
                val paystackCheckout =
                    PaystackCheckout(activity, "pk_test_1234568695", testRegistry)
                paystackCheckout.charge(TEST_EMAIL, 10000, "NGN", resultListener)

                verify(resultListener).onError(FAILED_TRANSACTION)
            }
        }
    }
    @Test
    fun charge_whenCheckoutIsCancelled_invokesResultListenerOnCancelled() {
        ActivityScenario.launch(CheckoutActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val testRegistry = getTestResultRegistry(ChargeResult.Cancelled)
                val paystackCheckout =
                    PaystackCheckout(activity, "pk_test_1234568695", testRegistry)
                paystackCheckout.charge(TEST_EMAIL, 10000, "NGN", resultListener)

                verify(resultListener).onCancelled()
            }
        }
    }

    private fun getTestResultRegistry(chargeResult: ChargeResult): ActivityResultRegistry {
        return object : ActivityResultRegistry() {
            override fun <I, O> onLaunch(
                requestCode: Int,
                contract: ActivityResultContract<I, O>,
                input: I,
                options: ActivityOptionsCompat?
            ) {
                dispatchResult(requestCode, chargeResult)
            }
        }
    }

    companion object {
        const val TEST_EMAIL = "michael@paystack.com"

        val FAILED_TRANSACTION = Transaction(
            id = 1000,
            accessCode = "failed_access",
            amount = 10000,
            currency = "NGN",
            email = TEST_EMAIL,
            transactionStatus = "failed"
        )
        val SUCCESSFUL_TRANSACTION = Transaction(
            id = 1001,
            accessCode = "successful_access",
            amount = 10000,
            currency = "NGN",
            email = TEST_EMAIL,
            transactionStatus = "successful"
        )
    }
}