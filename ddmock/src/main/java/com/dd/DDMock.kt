package com.dd

import android.app.Application
import android.content.res.AssetManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.MediaType
import okio.Buffer
import java.io.IOException
import java.util.regex.Pattern

const val MOCK_FILE_DIRECTORY = "mockfiles"

object DDMock {
    private lateinit var application: Application
    private lateinit var assetManager: AssetManager
    private val mockEntries = HashMap<String, MockEntry>()

    @JvmStatic
    fun install(application: Application) {
        this.application = application
        this.assetManager = application.assets
        CoroutineScope(Dispatchers.IO).launch {
            mockEntries.putAll(listAssetFiles(MOCK_FILE_DIRECTORY))
        }
    }

    private fun listAssetFiles(path: String): HashMap<String, MockEntry> {
        val result = HashMap<String, MockEntry>()
        assetManager.list(path)?.forEach { file ->
            val innerFiles = listAssetFiles("$path/$file")
            if (!innerFiles.isEmpty()) {
                result.putAll(innerFiles)
            } else {
                val fullFilePath = "$path/$file"
                val key = path.replace(MOCK_FILE_DIRECTORY, "")
                val extension = fullFilePath.split("/", ".").last()
                if (result.contains(key)) {
                    result[key]?.files?.add(fullFilePath)
                } else {
                    result[key] = com.dd.MockEntry(key, arrayListOf(fullFilePath), mediaType = getMediaType(extension))
                }
            }
        }
        return result
    }

    fun getEntry(url: HttpUrl, method: String): MockEntry? {
        val urlPathBuilder = StringBuilder("")
        val pathSegments = url.pathSegments()
        var i = 0
        val size = pathSegments.size
        while (i < size) {
            if (i == size - 1) {
                urlPathBuilder.append('/').append(pathSegments[i]).append('/').append(method.toLowerCase())
            } else {
                urlPathBuilder.append('/').append(pathSegments[i])
            }
            i++
        }
        val path = urlPathBuilder.toString()
        val mockEntry = mockEntries[path]
        return mockEntry ?: getRegexEntry(path)
    }

    private fun getMediaType(extension: String): MediaType? {
        extension.toLowerCase()
        when (extension) {
            "json" -> return MediaType.parse(MockEntry.CONTENT_TYPE_APPLICATION_JSON)
            "pdf" -> return MediaType.parse(MockEntry.CONTENT_TYPE_APPLICATION_PDF)
            "jpeg", "jpg" -> return MediaType.parse(MockEntry.CONTENT_TYPE_IMAGE_JPEG)
            "png" -> return MediaType.parse(MockEntry.CONTENT_TYPE_IMAGE_PNG)
            "txt" -> return MediaType.parse(MockEntry.CONTENT_TYPE_PLAIN_TXT)
            "gif" -> return MediaType.parse(MockEntry.CONTENT_TYPE_IMAGE_GIF)
        }
        return MediaType.parse(MockEntry.CONTENT_TYPE_APPLICATION_JSON)
    }

    private fun getRegexEntry(path: String): MockEntry? {
        for (key in mockEntries.keys) {
            if (key.contains("{")) {
                val regexKey = key.replace(Regex("\\{.*\\}"), ".*")
                val pattern = Pattern.compile(regexKey)
                val matcher = pattern.matcher(path)
                if (matcher.find()) {
                    return mockEntries[key]
                }
            }
        }
        return null
    }

    @Throws(IOException::class)
    fun getBuffer(filePath: String): Buffer {
        val buffer = Buffer()
        val inputStream = assetManager.open(filePath)
        buffer.readFrom(inputStream)
        return buffer
    }

    fun getEntries() = mockEntries

    fun getEntry(key: String): MockEntry? = mockEntries[key]
}