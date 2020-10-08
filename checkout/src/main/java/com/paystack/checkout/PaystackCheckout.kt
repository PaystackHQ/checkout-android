package com.paystack.checkout

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultRegistry
import androidx.appcompat.app.AppCompatActivity
import com.paystack.checkout.model.ChargeParams
import com.paystack.checkout.model.ChargeResult
import com.paystack.checkout.model.PaymentChannel
import com.paystack.checkout.ui.PayWithCheckout

class PaystackCheckout private constructor(
    private val activity: AppCompatActivity,
    private val resultRegistry: ActivityResultRegistry,
    private val publicKey: String,
    private val email: String,
    private val amount: Long,
    private val currency: String,
    private val channels: List<PaymentChannel>?,
    private val firstName: String?,
    private val lastName: String?,
    private val phone: String?,
    private val plan: String?,
    private val subAccount: String?,
    private val customerCode: String?,
    private val reference: String?,
    private val paymentPage: String?,
    private val paymentRequest: String?,
    private val bearer: String?,
    private val metadata: String?,
    private val transactionCharge: Long?,
    private val orderId: Long?,
) {
    private val chargeParams: ChargeParams
        get() = ChargeParams(
            publicKey,
            email,
            amount,
            currency,
            channels,
            firstName,
            lastName,
            phone,
            plan,
            subAccount,
            customerCode,
            reference,
            paymentPage,
            paymentRequest,
            bearer,
            metadata,
            transactionCharge,
            orderId
        )

    fun charge(resultListener: CheckoutResultListener) {
        activity.registerForActivityResult(PayWithCheckout(), resultRegistry) { chargeResult ->
            when (chargeResult) {
                is ChargeResult.Success -> resultListener.onSuccess(chargeResult.transaction)
                is ChargeResult.Error -> resultListener.onError(chargeResult.exception)
                ChargeResult.Cancelled -> resultListener.onCancelled()
            }
        }.launch(chargeParams)
    }

    class Builder(
        private val activity: AppCompatActivity,
        private val email: String,
        private val amount: Long,
        private val currency: String,
    ) {
        private var activityResultRegistry: ActivityResultRegistry = activity.activityResultRegistry
        private var publicKey = getPublicKeyFromManifest(activity)

        private var channels: List<PaymentChannel>? = null
        private var firstName: String? = null
        private var lastName: String? = null
        private var phone: String? = null
        private var plan: String? = null
        private var subAccount: String? = null
        private var customerCode: String? = null
        private var reference: String? = null
        private var paymentPage: String? = null
        private var paymentRequest: String? = null
        private var bearer: String? = null
        private var metadata: String? = null
        private var transactionCharge: Long? = null
        private var orderId: Long? = null

        fun activityResultRegistry(resultRegistry: ActivityResultRegistry): Builder {
            this.activityResultRegistry = resultRegistry
            return this
        }

        fun publicKey(publicKey: String): Builder {
            this.publicKey = publicKey
            return this
        }

        fun firstName(firstName: String): Builder {
            this.firstName = firstName
            return this
        }

        fun lastName(lastName: String): Builder {
            this.lastName = lastName
            return this
        }

        fun phone(phone: String): Builder {
            this.phone = phone
            return this
        }

        fun reference(reference: String): Builder {
            this.reference = reference
            return this
        }

        fun channels(vararg channels: PaymentChannel): Builder {
            this.channels = channels.toList()
            return this
        }

        fun plan(plan: String): Builder {
            this.plan = plan
            return this
        }

        fun subAccount(subAccount: String): Builder {
            this.subAccount = subAccount
            return this
        }

        fun customerCode(customerCode: String): Builder {
            this.customerCode = customerCode
            return this
        }

        fun paymentPage(paymentPage: String): Builder {
            this.paymentPage = paymentPage
            return this
        }

        fun paymentRequest(paymentRequest: String): Builder {
            this.paymentRequest = paymentRequest
            return this
        }

        fun bearer(bearer: String): Builder {
            this.bearer = bearer
            return this
        }

        fun metadata(metadata: String): Builder {
            this.metadata = metadata
            return this
        }

        fun transactionCharge(transactionCharge: Long): Builder {
            this.transactionCharge = transactionCharge
            return this
        }

        fun orderId(orderId: Long): Builder {
            this.orderId = orderId
            return this
        }

        fun build(): PaystackCheckout {
            return PaystackCheckout(
                activity,
                activityResultRegistry,
                publicKey,
                email,
                amount,
                currency,
                channels,
                firstName,
                lastName,
                phone,
                plan,
                subAccount,
                customerCode,
                reference,
                paymentPage,
                paymentRequest,
                bearer,
                metadata,
                transactionCharge,
                orderId,
            )
        }

        private fun getPublicKeyFromManifest(context: Context): String {
            val applicationInfo = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            return applicationInfo.metaData?.getString(KEY_PUBLIC_KEY_PROP).orEmpty()
        }
    }

    companion object {
        const val KEY_PUBLIC_KEY_PROP = "com.paystack.checkout.PublicKey"
    }
}