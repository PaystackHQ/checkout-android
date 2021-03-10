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
import androidx.webkit.WebMessageCompat
import androidx.webkit.WebMessagePortCompat
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import com.paystack.checkout.databinding.CheckoutActivityBinding
import com.paystack.checkout.exception.MissingWebViewException
import com.paystack.checkout.model.ChargeParams
import com.paystack.checkout.model.ChargeResult
import com.paystack.checkout.model.CheckoutEvent
import com.paystack.checkout.model.CheckoutEventType
import com.paystack.checkout.model.CloseEvent
import com.paystack.checkout.model.ErrorEvent
import com.paystack.checkout.model.LoadedTransactionEvent
import com.paystack.checkout.model.RedirectingEvent
import com.paystack.checkout.model.SuccessEvent
import com.paystack.checkout.ui.di.CheckoutContainer
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

internal class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: CheckoutActivityBinding
    private val viewModel: CheckoutViewModel by lazy {
        CheckoutContainer.checkoutViewModelFactory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (WebViewCompat.getCurrentWebViewPackage(this) == null) {
            closeWithError(MissingWebViewException())
            return
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = CheckoutActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val params = intent.getParcelableExtra<ChargeParams>(EXTRA_CHARGE_PARAMS)
            ?: error("Charge parameters not found")
        viewModel.state.observe(this) { state ->
            val errorOccurred = state.initError?.consumeIfAvailable { exception ->
                handleCheckoutEvent(ErrorEvent(exception))
            } ?: false

            if (errorOccurred) return@observe

            state.transaction?.consumeIfAvailable { transaction ->
                val paymentUrl = "https://checkout.paystack.com/${transaction.accessCode}"
                setupTransactionWebView(paymentUrl)
            }
        }

        viewModel.initializeTransaction(params)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupTransactionWebView(paymentUrl: String) {
        if (!WebViewFeature.isFeatureSupported(WebViewFeature.CREATE_WEB_MESSAGE_CHANNEL)) {
            // TODO: Finish with error result if web message channels aren't supported

            return
        }

        val webView = binding.wbvTransaction
        webView.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
        }
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                listenToWebEvents(view)
                webView.isVisible = true
            }
        }
        webView.loadUrl(paymentUrl)
    }

    @SuppressLint("RequiresFeature")
    private fun listenToWebEvents(webView: WebView) {
        val eventAdapterFactory =
            PolymorphicJsonAdapterFactory.of(CheckoutEvent::class.java, "event")
                .withSubtype(CloseEvent::class.java, CheckoutEventType.close.value)
                .withSubtype(RedirectingEvent::class.java, CheckoutEventType.redirecting.value)
                .withSubtype(
                    LoadedTransactionEvent::class.java,
                    CheckoutEventType.loadedTransaction.value
                )
                .withSubtype(SuccessEvent::class.java, CheckoutEventType.success.value)
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
            is CloseEvent -> closeWithResult(ChargeResult.Cancelled)

            is SuccessEvent -> {
                val transaction = viewModel.currentState().transaction?.peekContent()
                if (transaction == null) {
                    closeWithError(IllegalStateException("Transaction not found."))
                    return
                }

                val transactionReference = checkoutEvent.data.reference
                val data = ChargeResult.Success(transaction.copy(reference = transactionReference))
                closeWithResult(data)
            }

            is ErrorEvent -> closeWithError(checkoutEvent.exception)

            else -> Log.d(TAG, checkoutEvent.toString())
        }
    }

    private fun closeWithResult(data: ChargeResult) {
        val resultData = Intent()
        resultData.putExtra(EXTRA_TRANSACTION_RESULT, data)
        setResult(RESULT_OK, resultData)
        finish()
    }

    private fun closeWithError(exception: Throwable) {
        closeWithResult(ChargeResult.Error(exception))
    }

    companion object {
        private const val TAG = "CheckoutActivity"
        const val EXTRA_CHARGE_PARAMS = "charge_params"
        const val EXTRA_TRANSACTION_RESULT = "transaction_result"
    }
}
