package com.paystack.checkout

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityOptionsCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.paystack.checkout.model.ChargeParams
import com.paystack.checkout.model.ChargeResult
import com.paystack.checkout.model.Transaction
import com.paystack.checkout.ui.CheckoutActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
internal class PaystackCheckoutTest {

    private val applicationContext: Context
        get() = ApplicationProvider.getApplicationContext()

    @Mock
    lateinit var resultListener: CheckoutResultListener

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun charge_whenChargeIsSuccessful_invokesResultListenerOnSuccess() {
        val intent = Intent(applicationContext, CheckoutActivity::class.java).apply {
            putExtra(CheckoutActivity.EXTRA_CHARGE_PARAMS, TEST_CHARGE_PARAMS)
        }
        ActivityScenario.launch<CheckoutActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                val chargeResult = ChargeResult.Success(TEST_SUCCESSFUL_TRANSACTION)
                val testRegistry = getTestResultRegistry(chargeResult)
                val paystackCheckout = PaystackCheckout.Builder(activity, TEST_EMAIL, 10000L, "NGN")
                    .activityResultRegistry(testRegistry)
                    .publicKey(TEST_CHARGE_PARAMS.publicKey)
                    .build()
                paystackCheckout.charge(resultListener)

                verify(resultListener).onSuccess(TEST_SUCCESSFUL_TRANSACTION)
            }
        }
    }

    @Test
    fun charge_whenChargeFails_invokesResultListenerOnError() {
        val intent = Intent(applicationContext, CheckoutActivity::class.java).apply {
            putExtra(CheckoutActivity.EXTRA_CHARGE_PARAMS, TEST_CHARGE_PARAMS)
        }
        ActivityScenario.launch<CheckoutActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                val chargeResult = ChargeResult.Error(IllegalStateException())
                val testRegistry = getTestResultRegistry(chargeResult)
                val paystackCheckout = PaystackCheckout.Builder(activity, TEST_EMAIL, 10000L, "NGN")
                    .activityResultRegistry(testRegistry)
                    .publicKey(TEST_CHARGE_PARAMS.publicKey)
                    .build()
                paystackCheckout.charge(resultListener)

                verify(resultListener).onError(chargeResult.exception)
            }
        }
    }

    @Test
    fun charge_whenCheckoutIsCancelled_invokesResultListenerOnCancelled() {
        val intent = Intent(applicationContext, CheckoutActivity::class.java).apply {
            putExtra(CheckoutActivity.EXTRA_CHARGE_PARAMS, TEST_CHARGE_PARAMS)
        }
        ActivityScenario.launch<CheckoutActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                val testRegistry = getTestResultRegistry(ChargeResult.Cancelled)
                val paystackCheckout = PaystackCheckout.Builder(activity, TEST_EMAIL, 10000L, "NGN")
                    .activityResultRegistry(testRegistry)
                    .publicKey(TEST_CHARGE_PARAMS.publicKey)
                    .build()
                paystackCheckout.charge(resultListener)

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

        val TEST_SUCCESSFUL_TRANSACTION = Transaction(
            id = 1001,
            accessCode = "successful_access",
            amount = 10000,
            currency = "NGN",
            email = TEST_EMAIL,
        )

        val TEST_CHARGE_PARAMS = ChargeParams("pk_test_test_key_value", TEST_EMAIL, 10000L, "NGN")
    }
}
