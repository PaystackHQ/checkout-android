package com.paystack.checkout.data.remote.di

import com.paystack.checkout.data.remote.PaystackApi
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal fun apiComponent(): ApiComponent = ApiModule

internal interface ApiComponent {
    val loggingInterceptor: HttpLoggingInterceptor
    val okHttpClient: OkHttpClient
    val moshi: Moshi
    val retrofit: Retrofit
    val paystackApi: PaystackApi
}

internal object ApiModule : ApiComponent {
    override val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    override val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(loggingInterceptor)
        .build()

    override val moshi: Moshi = Moshi.Builder()
        .add(Wrapped.ADAPTER_FACTORY)
        .build()

    override val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://api.paystack.co")
        .build()

    override val paystackApi: PaystackApi = retrofit.create(PaystackApi::class.java)
}
