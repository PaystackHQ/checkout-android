# Paystack Checkout Android
[ ![Download](https://api.bintray.com/packages/paystack/maven/checkout-android/images/download.svg) ](https://bintray.com/paystack/maven/checkout-android/_latestVersion)
## Requirements

- Android 5.0+ (API 21+)
- AndroidX

## Getting Started

### Configuration

Add `checkout-android` to the dependencies in your app level `build.gradle` file

```groovy
dependencies {
    implementation 'com.paystack.checkout:checkout-android:0.1.0-alpha02'
}
```

You should also add Java 8 support in your build.gradle:
```groovy
android {
    // ... Other configuration code
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    // For kotlin codebases, include
    kotlinOptions {
         jvmTarget = "1.8"
    }
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

In Java

```java
CheckoutResultListener resultListener = new CheckoutResultListener() {
    @Override
    public void onSuccess(@NonNull Transaction transaction) {
        // Executed when transaction is successful
    }

    @Override
    public void onError(@NonNull Throwable exception) {
        // Executed when an error occurs
    }

    @Override
    public void onCancelled() {
        // Executed when the user cancels the payment process
    }
};
String email = "example@example.com";
long amount = 10000;
String currency = "NGN";
PaystackCheckout checkout = new PaystackCheckout.Builder(activity, email,amount, currency)
    .build();
checkout.charge(resultListener);
```

## Security

If you believe you’ve discovered a bug, kindly get in in touch with the Paystack Security team at [security@paystack.com](mailto:%22security@paystack.com).
We will promptly respond to your report. We request that you not publicly disclose the issue until it's been addressed by Paystack.
