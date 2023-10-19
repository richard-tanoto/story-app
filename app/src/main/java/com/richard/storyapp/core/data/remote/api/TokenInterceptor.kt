package com.richard.storyapp.core.data.remote.api

import com.richard.storyapp.core.data.local.preference.PreferencesManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(
    private val preferencesManager: PreferencesManager
) : Interceptor {

    @Volatile
    private var token: String = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        runBlocking { token = preferencesManager.getToken().first() }
        val request = chain.request()
            .newBuilder().apply {
                if (token.isNotEmpty()) header("Authorization", "Bearer $token")
            }
            .build()
        return chain.proceed(request)
    }

}
