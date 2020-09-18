package com.paystack.checkout.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.webkit.WebMessageCompat
import androidx.webkit.WebMessagePortCompat
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import com.paystack.checkout.BuildConfig
import com.paystack.checkout.databinding.CheckoutActivityBinding
import com.paystack.checkout.model.ChargeParams
import com.paystack.checkout.ui.di.CheckoutContainer

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
        val (receiver, sender) = WebViewCompat.createWebMessageChannel(webView)
        receiver.setWebMessageCallback(object : WebMessagePortCompat.WebMessageCallbackCompat() {
            override fun onMessage(port: WebMessagePortCompat, message: WebMessageCompat?) {
                Log.e("HA", message?.data.orEmpty())

                Toast.makeText(this@CheckoutActivity, message?.data, Toast.LENGTH_LONG).show()
            }
        })

        val message = WebMessageCompat("""{"type": "init_port"}""", arrayOf(sender))
        WebViewCompat.postWebMessage(webView, message, Uri.parse("*"))
        // val initMessage  = WebMessageCompat("""{type: "init"}""", arrayOf(sender))
        // WebViewCompat.postWebMessage(webView, initMessage, Uri.parse("*"))
    }

    // override fun onResume() {
    //     super.onResume()
    //     lifecycleScope.launchWhenResumed {
    //         delay(5000)
    //         val intent = Intent().apply {
    //             val transaction = Transaction(
    //                 745857,
    //                 "hfjfjsjbwu42y",
    //                 30000,
    //                 "NGN",
    //                 "ask4myk@gmail.com",
    //                 "successful"
    //             )
    //             putExtra(EXTRA_TRANSACTION_RESULT, transaction)
    //         }
    //         setResult(Activity.RESULT_OK, intent)
    //     }
    // }

    companion object {
        const val EXTRA_CHARGE_PARAMS = "charge_params"
        const val EXTRA_TRANSACTION_RESULT = "transaction_result"
    }
}