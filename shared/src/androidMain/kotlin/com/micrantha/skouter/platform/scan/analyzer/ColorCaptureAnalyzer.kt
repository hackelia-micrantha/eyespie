package com.micrantha.skouter.platform.scan.analyzer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.palette.graphics.Palette
import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageColor
import com.micrantha.skouter.platform.scan.model.ImageColors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.sqrt

private const val MODEL_ASSET = "colors.csv"

actual class ColorCaptureAnalyzer(
    context: Context,
) : ColorAnalyzer(context), CaptureAnalyzer<ImageColors> {

    actual override suspend fun analyze(image: CameraImage): Result<ImageColors> =
        suspendCoroutine { continuation ->
            try {
                val colors = candidateColors(image.toBitmap())
                continuation.resume(Result.success(colors))
            } catch (err: Throwable) {
                continuation.resume(Result.failure(err))
            }
        }
}

actual class ColorStreamAnalyzer(
    context: Context,
    private val callback: AnalyzerCallback<ImageColors>
) : ColorAnalyzer(context), StreamAnalyzer {
    actual override fun analyze(image: CameraImage) = try {
        callback.onAnalyzerResult(candidateColors(image.toBitmap()))
    } catch (err: Throwable) {
        callback.onAnalyzerError(err)
    }
}

abstract class ColorAnalyzer(context: Context) {

    private val colorNames by lazy { context.readColorNames() }

    protected fun candidateColors(bitmap: Bitmap): ImageColors {
        val palette = Palette.from(bitmap).generate()

        val rgb = palette.dominantSwatch!!.rgb

        val c1 = arrayOf(Color.red(rgb), Color.green(rgb), Color.blue(rgb))

        val colors = colorNames.map {
            it.key to colorDistance(c1, it.value)
        }.sortedBy { it.second }.take(1).map { (key, _) ->
            ImageColor(
                key,
                colorNames[key]!!.let { Color.rgb(it[0], it[1], it[2]) },
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
        assets.open(MODEL_ASSET).bufferedReader().use { reader ->
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
