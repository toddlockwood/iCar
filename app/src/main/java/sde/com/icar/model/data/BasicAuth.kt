package sde.com.icar.model.data

import okhttp3.Interceptor
import okhttp3.Response

class BasicAuth(private val token: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().header("x-apikey", "$token").build()
        return chain.proceed(request)
    }
}