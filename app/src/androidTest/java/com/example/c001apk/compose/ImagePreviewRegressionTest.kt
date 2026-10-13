package com.example.c001apk.compose

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.TextureView
import androidx.exifinterface.media.ExifInterface
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import coil.imageLoader
import com.example.c001apk.compose.ui.main.MainActivity
import com.example.c001apk.compose.util.MojitoOkHttpImageLoader
import net.mikaelzero.mojito.Mojito
import net.mikaelzero.mojito.loader.ImageLoader
import net.mikaelzero.mojito.ui.ImageMojitoActivity
import net.mikaelzero.mojito.impl.DefaultTargetFragmentCover
import net.mikaelzero.mojito.view.sketch.core.SketchImageView
import net.mikaelzero.mojito.view.sketch.SketchImageLoadFactory
import com.example.c001apk.media.MojitoMediaImageFactory
import com.example.c001apk.media.MojitoMediaPlaybackSession
import com.example.c001apk.media.HdrMojitoActivityCoverLoader
import com.example.c001apk.media.RichMediaHint
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.net.ServerSocket
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

@RunWith(AndroidJUnit4::class)
class ImagePreviewRegressionTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext

    @Test fun displayedCoilThumbnailIsAvailableWithoutNetwork() {
        val uri = Uri.parse("https://example.invalid/preview-${System.nanoTime()}.jpg")
        val fixture = fixture(400, 800)
        val cache = requireNotNull(context.imageLoader.diskCache)
        val editor = requireNotNull(cache.openEditor(uri.toString()))
        fixture.copyTo(editor.data.toFile(), overwrite = true)
        editor.commit()
        val result = LoadResult()
        MojitoOkHttpImageLoader(context).loadImage(100, uri, true, result)
        assertTrue(result.finished.await(5, TimeUnit.SECONDS))
        assertNull(result.failure)
        assertArrayEquals(fixture.readBytes(), requireNotNull(result.file).readBytes())
    }

    @Test fun concurrentDownloadsOfOneImageDoNotShareTemporaryFile() {
        val bytes = fixture(400, 800).readBytes()
        val ready = CountDownLatch(2)
        ServerSocket(0).use { server ->
            val serving = thread {
                repeat(2) {
                    val socket = server.accept()
                    thread {
                        socket.use {
                            val input = socket.getInputStream().bufferedReader()
                            while (!input.readLine().isNullOrEmpty()) { }
                            val output = socket.getOutputStream()
                            output.write("HTTP/1.1 200 OK\r\nContent-Type: image/jpeg\r\nContent-Length: ${bytes.size}\r\nConnection: close\r\n\r\n".toByteArray())
                            output.write(bytes, 0, bytes.size / 2)
                            output.flush()
                            ready.countDown()
                            ready.await(5, TimeUnit.SECONDS)
                            output.write(bytes, bytes.size / 2, bytes.size - bytes.size / 2)
                            output.flush()
                        }
                    }
                }
            }
            val uri = Uri.parse("http://127.0.0.1:${server.localPort}/image.jpg")
            val loader = MojitoOkHttpImageLoader(context)
            val first = LoadResult()
            val second = LoadResult()
            loader.loadImage(201, uri, false, first)
            loader.loadImage(202, uri, false, second)
            assertTrue(first.finished.await(10, TimeUnit.SECONDS))
            assertTrue(second.finished.await(10, TimeUnit.SECONDS))
            assertNull(first.failure)
            assertNull(second.failure)
            assertArrayEquals(bytes, requireNotNull(first.file).readBytes())
            assertArrayEquals(bytes, requireNotNull(second.file).readBytes())
            serving.join(1000)
        }
    }

    @Test fun portraitLandscapeRotatedAndLongImagesHaveValidBounds() {
        val portrait = fixture(400, 800)
        val landscape = fixture(1000, 400)
        val rotated = fixture(400, 800).apply {
            ExifInterface(absolutePath).apply {
                setAttribute(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_ROTATE_90.toString())
                saveAttributes()
            }
        }
        val longImage = fixture(300, 2000)
        for ((index, file) in listOf(portrait, landscape, rotated, longImage).withIndex()) {
            val monitor = instrumentation.addMonitor(ImageMojitoActivity::class.java.name, null, false)
            ActivityScenario.launch(MainActivity::class.java).use { scenario ->
                scenario.onActivity { activity -> Mojito.start(activity) { urls(file.absolutePath) } }
                val viewer = instrumentation.waitForMonitorWithTimeout(monitor, 5000)
                    ?: throw AssertionError("Image viewer did not open")
                var valid = false
                repeat(40) {
                    if (!valid) {
                        instrumentation.runOnMainSync {
                            val image = findImage(viewer.window.decorView)
                            val rect = RectF()
                            image?.zoomer?.getDrawRect(rect)
                            valid = image?.drawable != null && rect.width() > 0 && rect.height() > 0
                            if (valid && index < 3) {
                                assertTrue("image is cropped horizontally: $rect", rect.width() <= image!!.width + 2)
                                assertTrue("image is cropped vertically: $rect", rect.height() <= image!!.height + 2)
                            }
                        }
                        if (!valid) Thread.sleep(100)
                    }
                }
                assertTrue("Image $index has no decoded display bounds", valid)
                Thread.sleep(500)
                instrumentation.uiAutomation.takeScreenshot()?.let { screenshot ->
                    val evidence = File(context.getExternalFilesDir(null), "validation/image-$index.png")
                    evidence.parentFile?.mkdirs()
                    evidence.outputStream().use { screenshot.compress(Bitmap.CompressFormat.PNG, 100, it) }
                    screenshot.recycle()
                }
                instrumentation.runOnMainSync { viewer.finish() }
            }
            instrumentation.removeMonitor(monitor)
        }
    }

    @Test fun livePhotoKeepsItsStillVisibleUntilTheVideoHasAFrame() {
        val file = fixture(400, 800)
        file.appendBytes(instrumentation.context.assets.open("live-test.mp4").use { it.readBytes() })
        val monitor = instrumentation.addMonitor(ImageMojitoActivity::class.java.name, null, false)
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val session = MojitoMediaPlaybackSession()
                val factory = MojitoMediaImageFactory(SketchImageLoadFactory(), file.absolutePath,
                    mediaHint = RichMediaHint.LIVE_PHOTO, playbackSession = session)
                Mojito.start(activity) {
                    urls(file.absolutePath)
                    multiContentLoader(providerLoader = { factory }, providerEnableTargetLoad = { true })
                    setActivityCoverLoader(HdrMojitoActivityCoverLoader(session))
                }
            }
            val viewer = instrumentation.waitForMonitorWithTimeout(monitor, 5000)
                ?: throw AssertionError("Live photo viewer did not open")
            var sawImage = false
            var sawVideo = false
            repeat(40) {
                instrumentation.runOnMainSync {
                    val image = findImage(viewer.window.decorView)
                    if (image?.drawable != null) sawImage = true
                    val texture = findTexture(viewer.window.decorView)
                    if (texture != null && texture.visibility == View.VISIBLE && texture.alpha > .5f)
                        sawVideo = true
                }
                if (sawImage) instrumentation.uiAutomation.takeScreenshot()?.let { screenshot ->
                    val pixel = screenshot.getPixel(screenshot.width / 2, screenshot.height / 2)
                    assertTrue("Live photo flashed black", Color.blue(pixel) > 80)
                    screenshot.recycle()
                }
                Thread.sleep(75)
            }
            assertTrue("Still image did not decode", sawImage)
            assertTrue("Live photo video did not render", sawVideo)
            instrumentation.runOnMainSync { viewer.finish() }
        }
        instrumentation.removeMonitor(monitor)
    }

    @Test fun manualOriginalButtonRemainsAvailableWithOnlyAPreview() {
        val preview = fixture(400, 800)
        val monitor = instrumentation.addMonitor(ImageMojitoActivity::class.java.name, null, false)
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                Mojito.start(activity) {
                    urls(preview.absolutePath, "https://example.invalid/uncached-${System.nanoTime()}.jpg")
                    autoLoadTarget(false)
                    fragmentCoverLoader { DefaultTargetFragmentCover() }
                }
            }
            val viewer = instrumentation.waitForMonitorWithTimeout(monitor, 5000)
                ?: throw AssertionError("Image viewer did not open")
            Thread.sleep(800)
            instrumentation.runOnMainSync {
                val button = viewer.findViewById<View>(net.mikaelzero.mojito.R.id.seeTargetImageTv)
                assertTrue("View original is missing while only a thumbnail is cached", button?.isShown == true)
                viewer.finish()
            }
        }
        instrumentation.removeMonitor(monitor)
    }

    private fun fixture(width: Int, height: Int): File {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.BLUE)
        val paint = Paint()
        paint.color = Color.RED
        canvas.drawRect(0f, 0f, width * .1f, height.toFloat(), paint)
        paint.color = Color.GREEN
        canvas.drawRect(width * .9f, 0f, width.toFloat(), height.toFloat(), paint)
        paint.color = Color.YELLOW
        canvas.drawRect(0f, 0f, width.toFloat(), height * .1f, paint)
        paint.color = Color.MAGENTA
        canvas.drawRect(0f, height * .9f, width.toFloat(), height.toFloat(), paint)
        return File.createTempFile("preview-fixture-", ".jpg", context.cacheDir).also { file ->
            file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 95, it) }
            bitmap.recycle()
        }
    }

    private fun findImage(view: View): SketchImageView? {
        if (view is SketchImageView) return view
        if (view is ViewGroup) for (index in 0 until view.childCount) {
            findImage(view.getChildAt(index))?.let { return it }
        }
        return null
    }

    private fun findTexture(view: View): TextureView? {
        if (view is TextureView) return view
        if (view is ViewGroup) for (index in 0 until view.childCount) {
            findTexture(view.getChildAt(index))?.let { return it }
        }
        return null
    }

    private class LoadResult : ImageLoader.Callback {
        val finished = CountDownLatch(1)
        var file: File? = null
        var failure: Exception? = null
        override fun onStart() {}
        override fun onProgress(progress: Int) {}
        override fun onSuccess(image: File) { file = image }
        override fun onFail(error: Exception) { failure = error }
        override fun onFinish() { finished.countDown() }
    }
}
