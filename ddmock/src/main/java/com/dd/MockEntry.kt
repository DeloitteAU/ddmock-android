package com.dd

import okhttp3.MediaType
import java.net.HttpURLConnection


private const val DEFAULT_MOCK_RESPONSE_DELAY_MS = 400L

class MockEntry(
        val path: String,
        val files: ArrayList<String>,
        var selectedFile: Int = 0,
        var statusCode: Int = HttpURLConnection.HTTP_OK,
        var responseTime: Long = DEFAULT_MOCK_RESPONSE_DELAY_MS,
        var mediaType2: MediaType?
) {
        companion object {
                const val CONTENT_TYPE_APPLICATION_JSON = "application/json; charset=utf-8"
                const val CONTENT_TYPE_APPLICATION_PDF = "application/pdf; charset=utf-8"
        }
    val mediaType = MediaType.parse(CONTENT_TYPE_APPLICATION_JSON)
}