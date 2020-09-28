package com.paystack.checkout.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.webkit.WebMessageCompat
import androidx.webkit.WebMessagePortCompat
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import com.paystack.checkout.BuildConfig
import com.paystack.checkout.databinding.CheckoutActivityBinding
import com.paystack.checkout.model.ChargeParams
import com.paystack.checkout.model.ChargeResult
import com.paystack.checkout.model.CheckoutEvent
import com.paystack.checkout.model.CheckoutEventType
import com.paystack.checkout.model.Close
import com.paystack.checkout.model.LoadedTransaction
import com.paystack.checkout.model.Redirecting
import com.paystack.checkout.model.Success
import com.paystack.checkout.ui.di.CheckoutContainer
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.delay

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: CheckoutActivityBinding
    private val viewModel by lazy {
        CheckoutContainer.checkoutViewModelFactory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding = CheckoutActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val params = intent.getParcelableExtra<ChargeParams>(EXTRA_CHARGE_PARAMS)
            ?: error("Charge parameters not found")
        viewModel.state.observe(this) { state ->
            val transaction = state.transaction?.readContent() ?: return@observe

            // val paymentUrl = "https://ee356cd88250.ngrok.io/${transaction.accessCode}"
            val paymentUrl = "https://checkout-studio.paystack.com/${transaction.accessCode}"
            setupTransactionWebView(paymentUrl)

        }

        viewModel.initializeTransaction(params)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupTransactionWebView(paymentUrl: String) {
        if (!WebViewFeature.isFeatureSupported(WebViewFeature.CREATE_WEB_MESSAGE_CHANNEL)) {
            //TODO: Finish with error result if web message channels aren't supported

            return
        }

        val webView = binding.wbvTransaction
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        // listenToWebEvents(webView)
        webView.apply {
            settings.apply {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                domStorageEnabled = true
            }
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    listenToWebEvents(view)
                    webView.isVisible = true
                }
            }
            loadUrl(paymentUrl)
        }
    }

    @SuppressLint("RequiresFeature")
    private fun listenToWebEvents(webView: WebView) {
        val eventAdapterFactory =
            PolymorphicJsonAdapterFactory.of(CheckoutEvent::class.java, "event")
                .withSubtype(Close::class.java, CheckoutEventType.close.value)
                .withSubtype(Redirecting::class.java, CheckoutEventType.redirecting.value)
                .withSubtype(LoadedTransaction::class.java, CheckoutEventType.loadedTransaction.value)
                .withSubtype(Success::class.java, CheckoutEventType.success.value)
        val eventAdapter = Moshi.Builder()
            .add(eventAdapterFactory)
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter(CheckoutEvent::class.java)

        val (receiver, sender) = WebViewCompat.createWebMessageChannel(webView)
        receiver.setWebMessageCallback(object : WebMessagePortCompat.WebMessageCallbackCompat() {
            override fun onMessage(port: WebMessagePortCompat, message: WebMessageCompat?) {
                val dataStr = message?.data ?: return
                Log.i(TAG, dataStr)
                try {
                    val event = eventAdapter.fromJson(dataStr)
                    event?.let { handleCheckoutEvent(it) }
                } catch (e: JsonDataException) {
                    Log.e(TAG, e.message, e)
                }
            }
        })

        val message = WebMessageCompat("""{"type": "init_port"}""", arrayOf(sender))
        WebViewCompat.postWebMessage(webView, message, Uri.parse("*"))
    }

    private fun handleCheckoutEvent(checkoutEvent: CheckoutEvent) {
        when (checkoutEvent) {
            is Close -> {
                val resultData = Intent()
                resultData.putExtra(EXTRA_TRANSACTION_RESULT, ChargeResult.Cancelled)
                setResult(RESULT_OK, resultData)
                finish()
            }
            is Redirecting -> {
                // Do nothing
            }
            is LoadedTransaction -> {
                // Do nothing
            }
            is Success -> {
                val transaction = viewModel.currentState().transaction?.peekContent()
                if (transaction == null) {
                    finishWithError(IllegalStateException("Transaction not found."))
                    return
                }

                lifecycleScope.launchWhenCreated {
                    val resultData = Intent().apply {
                        val data = ChargeResult.Success(
                            transaction.copy(reference = checkoutEvent.data.reference)
                        )
                        putExtra(EXTRA_TRANSACTION_RESULT, data)
                    }
                    setResult(RESULT_OK, resultData)
                    // Delay for 1 second to allow checkout animation complete
                    delay(1000)
                    finish()
                }
            }
        }
    }

    private fun finishWithError(exception: Throwable) {
        val resultData = Intent().apply {
            putExtra(EXTRA_TRANSACTION_RESULT, ChargeResult.Error(exception, null))
        }
        setResult(RESULT_OK, resultData)
        finish()
    }

    companion object {
        private const val TAG = "CheckoutActivity"
        const val EXTRA_CHARGE_PARAMS = "charge_params"
        const val EXTRA_TRANSACTION_RESULT = "transaction_result"
    }
}