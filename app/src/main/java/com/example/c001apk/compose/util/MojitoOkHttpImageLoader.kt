package com.example.c001apk.compose.util

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import coil.imageLoader
import net.mikaelzero.mojito.loader.ImageLoader
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread

class MojitoOkHttpImageLoader(private val context: Context) : ImageLoader {

    private val client = OkHttpClient.Builder()
        .addInterceptor(AddCookiesInterceptor)
        .build()

    private val callMap = ConcurrentHashMap<Int, Call>()
    private val cacheDir = File(context.cacheDir, "mojito").apply { mkdirs() }

    override fun loadImage(
        requestId: Int,
        uri: Uri,
        onlyRetrieveFromCache: Boolean,
        callback: ImageLoader.Callback
    ) {
        val scheme = uri.scheme?.lowercase()
        val isNetwork = scheme == "http" || scheme == "https"
        val cacheFile = resolveCacheFile(uri)
        callback.onStart()

        if (!isNetwork) {
            val localFile = if (scheme == "file") File(uri.path ?: "") else cacheFile
            if (localFile.exists()) {
                callback.onSuccess(localFile)
            } else {
                callback.onFail(IOException("file not found"))
            }
            callback.onFinish()
            return
        }

        if (onlyRetrieveFromCache) {
            if (usableCachedImage(cacheFile) || importCoilCache(uri, cacheFile)) {
                callback.onSuccess(cacheFile)
            } else {
                callback.onFail(IOException("cache miss"))
            }
            callback.onFinish()
            return
        }

        if (cacheFile.exists()) {
            if (usableCachedImage(cacheFile)) {
                callback.onSuccess(cacheFile)
                callback.onFinish()
                return
            } else {
                cacheFile.delete()
            }
        }
        if (importCoilCache(uri, cacheFile)) {
            callback.onSuccess(cacheFile)
            callback.onFinish()
            return
        }

        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }

        val request = Request.Builder().url(uri.toString()).build()
        val call = client.newCall(request)
        callMap.put(requestId, call)?.cancel()
        thread(name = "mojito-loader-$requestId") {
            var tempFile: File? = null
            try {
                call.execute().use { response ->
                    if (!response.isSuccessful) {
                        throw IOException("http ${response.code}")
                    }
                    val body = response.body ?: throw IOException("empty body")
                    val total = body.contentLength().coerceAtLeast(0L)
                    // Preview, prefetch and target requests may download the same URL concurrently.
                    // Each writer owns a temp file; a cancelled request must not damage another one.
                    val downloadFile = File.createTempFile("${cacheFile.name}.", ".tmp", cacheDir)
                    tempFile = downloadFile
                    var current = 0L
                    body.byteStream().use { input ->
                        FileOutputStream(downloadFile).use { output ->
                            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                            var read: Int
                            while (input.read(buffer).also { read = it } != -1) {
                                output.write(buffer, 0, read)
                                current += read
                                if (total > 0L) {
                                    val progress = ((current * 100) / total).toInt()
                                    callback.onProgress(progress)
                                }
                            }
                            output.flush()
                        }
                    }
                    if (current == 0L || (total > 0L && current != total) ||
                        !hasImageBounds(downloadFile)) {
                        throw IOException("incomplete image download")
                    }
                    if (!call.isCanceled()) {
                        synchronized(cacheDir) {
                            if (cacheFile.length() == 0L && !downloadFile.renameTo(cacheFile)) {
                                throw IOException("cache rename failed")
                            }
                        }
                        callback.onSuccess(cacheFile)
                    }
                }
            } catch (e: Exception) {
                if (call.isCanceled()) {
                    // Cancellation belongs to a closed/replaced view and has no UI result.
                } else if (e !is IOException && e.cause is IOException) {
                    callback.onFail(e.cause as IOException)
                } else {
                    callback.onFail(e)
                }
            } finally {
                tempFile?.delete()
                callback.onFinish()
                callMap.remove(requestId, call)
            }
        }
    }

    private fun hasImageBounds(file: File): Boolean {
        if (file.length() == 0L) return false
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(file.absolutePath, options)
        return options.outWidth > 0 && options.outHeight > 0
    }

    private fun usableCachedImage(file: File): Boolean {
        if (!file.exists()) return false
        if (hasImageBounds(file)) return true
        file.delete()
        return false
    }

    private fun importCoilCache(uri: Uri, destination: File): Boolean = runCatching {
        context.imageLoader.diskCache?.openSnapshot(uri.toString())?.use { snapshot ->
            if (!hasImageBounds(snapshot.data.toFile())) return@use false
            val tempFile = File.createTempFile("coil-preview-", ".tmp", cacheDir)
            try {
                snapshot.data.toFile().copyTo(tempFile, overwrite = true)
                synchronized(cacheDir) {
                    destination.length() > 0L || tempFile.renameTo(destination)
                }
            } finally {
                tempFile.delete()
            }
        } ?: false
    }.getOrDefault(false)

    override fun prefetch(uri: Uri) {
        loadImage(uri.hashCode(), uri, false, object : ImageLoader.Callback {
            override fun onStart() {}
            override fun onProgress(progress: Int) {}
            override fun onFinish() {}
            override fun onSuccess(image: File) {}
            override fun onFail(error: Exception) {}
        })
    }

    override fun cancel(requestId: Int) {
        callMap.remove(requestId)?.cancel()
    }

    override fun cancelAll() {
        callMap.values.forEach { it.cancel() }
        callMap.clear()
    }

    override fun cleanCache() {
        cacheDir.listFiles()?.forEach { it.delete() }
    }

    private fun resolveCacheFile(uri: Uri): File {
        val url = uri.toString()
        val extension = uri.lastPathSegment
            ?.substringBefore('?')
            ?.substringBefore('#')
            ?.substringAfterLast('.', "")
            ?.ifBlank { "img" }
            ?: "img"
        val name = md5(url)
        return File(cacheDir, "$name.$extension")
    }

    private fun md5(value: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val bytes = digest.digest(value.toByteArray())
        val builder = StringBuilder(bytes.size * 2)
        for (b in bytes) {
            builder.append(String.format("%02x", b))
        }
        return builder.toString()
    }
}
