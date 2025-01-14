package com.alexp.webapi

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

class CustomInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHeaders = originalRequest.headers

        // Modify the Authorization header if present
        val newRequest = if (originalHeaders["Authorization"] != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer ${originalHeaders["Authorization"]}")
                .build()
        } else {
            originalRequest
        }

        val response = chain.proceed(newRequest)
        val responseBody = response.body

        // Handle malformed JSON if the response body is not null
        if (responseBody != null) {
            val contentType = responseBody.contentType()
            val content = responseBody.string()

            val fixedContent = fixMalformedJson(content)
            val fixedResponseBody = fixedContent.toResponseBody(contentType)
            return response.newBuilder().body(fixedResponseBody).build()
        }
        return response
    }

    private fun fixMalformedJson(json: String): String {
        val trimmedJson = json.trim()
        val openBracesCount = trimmedJson.count { it == '{' }
        val closeBracesCount = trimmedJson.count { it == '}' }

        return when {
            openBracesCount > closeBracesCount -> trimmedJson + "}".repeat(openBracesCount - closeBracesCount)
            closeBracesCount > openBracesCount -> trimmedJson.dropLast(closeBracesCount - openBracesCount)
            else -> trimmedJson
        }
    }
}
