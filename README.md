# Paystack Checkout Android
[![CI](https://github.com/PaystackHQ/checkout-android/actions/workflows/ci.yml/badge.svg)](https://github.com/PaystackHQ/checkout-android/actions/workflows/ci.yml)
[ ![Download](https://api.bintray.com/packages/paystack/maven/checkout-android/images/download.svg) ](https://bintray.com/paystack/maven/checkout-android/_latestVersion)
## Requirements

- Android 5.0+ (API 21+)
- AndroidX

## Getting Started

### Configuration

Add `checkout-android` to the dependencies in your app level `build.gradle` file

```groovy
dependencies {
    implementation 'com.paystack.checkout:checkout-android:0.1.0-alpha06'
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

### Parameters
When initiating a charge, the `email`, `amount` and `currency` parameters are mandatory. There are also optional parameters that 
can be used to further customize your request:

#### General
| Param        | Type | Example | Description  |
| ------------- |-------------|-------------|-------------|
| reference     | String | `.reference("ODD_6647jdhdi6363")` | Unique case sensitive transaction reference. Only alphanumeric characters and these characters ( `-`, `.` , `=` ) are  allowed. If you do not pass this parameter, Paystack will generate a unique reference for you |
| channels     | List | `.channel(PaymentChannel.card, PaymentChannel.eft)`  | A list of payment channels to control what channels you want to make available to the user to make a payment with. Available channels include; ['card', 'bank', 'ussd', 'qr', 'mobile_money', 'bank_transfer', 'eft']. You can access the channels from the `PaymentChannel` enum.      |
| label | String | `.label("Diego Tequilla")` | String that replaces the customer email as shown on the checkout form |
| metadata | String | `.metadata("...")` | Object containing any extra information you want recorded with the transaction. You can learn more about metadata on our <a href="https://paystack.com/docs/payments/metadata" target="_blank">documentation</a>. |

The metadata can be crafted using the string literals as follows:

In Kotlin:
```kotlin
val metadata = """{
        "age": "50",
        "custom_fields": [
            {
                "display_name": "Brand",
                "variable_name": "brand",
                "value": "Bongo Kivu"
            }
        ]
    }""".trimIndent()
```

In Java:
```java
String metadata = "{" +
        "\"age\":\"50\"," +
        "\"custom_fields\": [" +
            "{" +
                "\"display_name\": \"Brand\"," +
                "\"variable_name\": \"brand\"," +
                "\"value\": \"Bongo Kivu\"" +
            "}" +
        "]" +
    "}";
```

#### Single Split
| Param        | Type | Example | Description  |
| -------------|-------------|-------------|-------------|
| subAccount     | String | `.subAccount("ACCT_8f4s1eq7ml6rlzj")` | The code for the subaccount that owns the payment |
| transactionCharge | Long | `.transactionCharge(10000)` | A flat fee to charge the subaccount for this transaction (in `kobo`, `pesewas` or `cents`). This overrides the split percentage set when the `subaccount` was created. Ideally, you will need to use this if you are splitting in flat rates (since subaccount creation only allows for percentage split). |
| bearer     | String | `.bearer("subaccount")` | Decide who will bear Paystack transaction charges between `account` and `subaccount`. Defaults to `account`. |

#### Multi Split
| Param        | Type | Example | Description  |
| -------------|-------------|-------------|-------------|
| splitCode     | String | `.splitCode("SPL_98WF13Eb3w")` | The split code of the transaction split. |

#### Subscription
| Param        | Type | Example | Description  |
| -------------|-------------|-------------|-------------|
| plan     | String | `.plan("PLN_xxxxxxxx")` | Plan code generated from creating a plan. This makes the payment become a subscription payment. |
| quantity | Long | `.quantity(2)` | Used to apply a multiple to the amount returned by the plan code above. |


## Security

If you believe you’ve discovered a bug, kindly get in in touch with the Paystack Security team at [security@paystack.com](mailto:%22security@paystack.com).
We will promptly respond to your report. We request that you not publicly disclose the issue until it's been addressed by Paystack.
