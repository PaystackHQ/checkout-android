# Paystack Checkout Android

## Requirements

- Android 5.0+ (API 21+)
- AndroidX

## Getting Started

### Configuration

Add `checkout-android` to the dependencies in your app level `build.gradle` file

```groovy
dependencies {
    implementation 'com.paystack:checkout-android:0.1.0'
}
```

### Define a public key

Set a public key in your app's manifest

```xml
<meta-data
    android:name="com.paystack.checkout.PublicKey"
    android:value="pk_paystack_public_key"/>
```

### Initiate a charge

In Kotlin

```kotlin
val checkoutResultListener = object : CheckoutResultListener {
    override fun onSuccess(transaction: Transaction) {
        // Executed when transaction is successful
    }

    override fun onError(exception: Throwable) {
        // Executed when an error occurs
    }

    override fun onCancelled() {
        // Executed when the user cancels the payment process
    }
}
val email = "example@example.com"
val amount = 10000L
val currency = "NGN"
PaystackCheckout.Builder(activity, email, amount, currency)
		.build()
    .charge(checkoutResultListener)
```

## Security

If you believe you’ve discovered a bug, kindly get in in touch with the Paystack Security team at [security@paystack.com](mailto:%22security@paystack.com).
We will promptly respond to your report. We request that you not publicly disclose the issue until it's been addressed by Paystack.