package com.fachmi.miweather.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.fachmi.miweather.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    private const val NETWORK_CALL_TIMEOUT = 120L
    private val gson = GsonBuilder().setLenient().create()

    private fun provideHttpInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            val url = chain.request().url.newBuilder()
                .addQueryParameter("app_key", BuildConfig.API_KEY)
                .build()

            val request = chain.request().newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }
    }

    private fun provideLoggingInterceptor(context: Context): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context).build()
    }

    private fun provideHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(provideHttpInterceptor(context))
            if (BuildConfig.DEBUG) {
                addInterceptor(provideLoggingInterceptor(context))
            }
            callTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
        }.build()
    }

    fun provideApiService(context: Context): ApiService {
        return Retrofit.Builder().apply {
            baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(provideHttpClient(context))
        }.build().create(ApiService::class.java)
    }
}