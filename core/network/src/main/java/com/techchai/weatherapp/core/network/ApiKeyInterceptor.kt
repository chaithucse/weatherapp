package com.techchai.weatherapp.core.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        chain.let {
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url

            val url = originalUrl.newBuilder()
                .addQueryParameter("appid", "a7e372bb2c3952d2c4f0587200cc7169").build()

            val request = originalRequest.newBuilder().url(url).build()
            return chain.proceed(request)
        }
    }
}