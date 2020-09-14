package com.paystack.checkout.ui

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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

        //TODO: Figure out entrance and exit animation

        // with(window) {
        //     requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        //
        //     val fadeTransition = TransitionInflater.from(this@CheckoutActivity)
        //         .inflateTransition(R.transition.fade)
        //         .apply { duration = 300}
        //     enterTransition = fadeTransition
        //     exitTransition = fadeTransition
        // }

        binding = CheckoutActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val params = intent.getParcelableExtra<ChargeParams>(EXTRA_CHARGE_PARAMS)
            ?: error("Charge parameters not found")
        viewModel.state.observe(this) { state ->
            val transaction = state.transaction?.readContent() ?: return@observe

            val paymentUrl = "https://checkout.paystack.com/${transaction.accessCode}"
            setupTransactionWebView(paymentUrl)

        }

        viewModel.initializeTransaction(params)
    }

    private fun setupTransactionWebView(paymentUrl: String) {
        val webView = binding.wbvTransaction
        webView.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
        }
        webView.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    webView.isVisible = true
                }
            }
            loadUrl(paymentUrl)
        }
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