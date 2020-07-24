package com.dd

import com.dd.view.Settings
import okhttp3.*
import java.io.IOException

class MockInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!Settings.useRealApi) {
            (DDMock.getEntry(request.url(), request.method()))?.let { mockEntry ->
                return createMockResponse(request, mockEntry)
            }
        }

        return chain.proceed(request)
    }

    @Throws(IOException::class)
    private fun createMockResponse(request: Request, mockEntry: MockEntry): Response {
        val filePath = mockEntry.files[mockEntry.selectedFile]

        try {
            Thread.sleep(mockEntry.responseTime)
        } catch (e: InterruptedException) {
            // nothing to do
        }

        val buffer = DDMock.getBuffer(filePath)

        return Response.Builder()
            .code(mockEntry.statusCode)
            .protocol(Protocol.HTTP_1_1)
            .message("MOCK")
            .header(
                "Content-Type",
                if (mockEntry.mediaType != null) mockEntry.mediaType.toString() else MockEntry.CONTENT_TYPE_APPLICATION_JSON
            )
            .request(request)
            .body(ResponseBody.create(mockEntry.mediaType, buffer.size(), buffer))
            .build()
    }
}