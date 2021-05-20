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
        var mediaType: MediaType?
) {
        companion object {
                const val CONTENT_TYPE_APPLICATION_JSON = "application/json; charset=utf-8"
                const val CONTENT_TYPE_APPLICATION_PDF = "application/pdf; charset=utf-8"
                const val CONTENT_TYPE_IMAGE_JPEG = "image/jpeg"
                const val CONTENT_TYPE_IMAGE_PNG = "image/png"
                const val CONTENT_TYPE_IMAGE_GIF = "image/gif"
                const val CONTENT_TYPE_PLAIN_TXT = "application/txt; charset=utf-8"
        }
}