package com.micrantha.skouter.platform.analyzer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import androidx.palette.graphics.Palette
import com.micrantha.bluebell.platform.toBitmap
import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageColor
import com.micrantha.skouter.platform.ImageColors
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.sqrt

actual class ColorImageAnalyzer(
    context: Context
) : ImageAnalyzer<ImageColors> {

    private val colorNames = context.readColorNames()

    actual override suspend fun analyze(image: CameraImage): Result<ImageColors> =
        suspendCoroutine { continuation ->
            try {
                val bitmap = image.image.toBitmap()!!

                val colors = candidateColors(bitmap)

                continuation.resume(Result.success(colors))
            } catch (err: Throwable) {
                continuation.resumeWithException(err)
            }
        }

    suspend fun analyze(image: CameraImage, bounds: Rect) = suspendCoroutine { continuation ->
        try {
            val bitmap = image.image.toBitmap(bounds)!!

            val colors = candidateColors(bitmap)

            continuation.resume(colors)
        } catch (err: Throwable) {
            continuation.resumeWithException(err)
        }
    }

    private fun candidateColors(bitmap: Bitmap): List<ImageColor> {
        val palette = Palette.from(bitmap).generate()

        val rgb = palette.dominantSwatch!!.rgb

        val c1 = arrayOf(Color.red(rgb), Color.green(rgb), Color.blue(rgb))

        val colors = colorNames.map {
            it.key to colorDistance(c1, it.value)
        }.sortedBy { it.second }.take(5).map { (key, _) ->
            ImageColor(
                key,
                colorNames[key]!!.let { Color.rgb(it[0], it[1], it[2]) }
            )
        }.toList()

        return colors
    }

    private fun colorDistance(c1: Array<Int>, c2: Array<Int>): Double {
        var sum = 0
        for (i in c1.indices) {
            val diff = c1[i] - c2[i]
            sum += (diff * diff)
        }
        return sqrt(sum.toDouble())
    }

    private fun Context.readColorNames() =
        assets.open("color_names.csv").bufferedReader().use { reader ->
            reader.readLine()
            reader.lineSequence()
                .filter { it.isNotBlank() }
                .associate {
                    val (name, _, r, g, b) = it.split(',')
                    val c2 = arrayOf(r.toInt(), g.toInt(), b.toInt())
                    name to c2
                }
        }
}
