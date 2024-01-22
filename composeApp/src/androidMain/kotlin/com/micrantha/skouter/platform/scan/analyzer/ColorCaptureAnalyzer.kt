package com.micrantha.skouter.platform.scan.analyzer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.palette.graphics.Palette
import com.micrantha.bluebell.data.Log
import com.micrantha.skouter.domain.model.ColorClue
import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import kotlin.math.sqrt

private const val MODEL_ASSET = "colors.csv"

actual class ColorCaptureAnalyzer(
    context: Context,
) : CaptureAnalyzer<ColorProof> {

    actual override suspend fun analyze(image: CameraImage): Result<ColorProof> =
        try {
            val colors = candidateColors(image.toBitmap())
            Result.success(colors)
        } catch (err: Throwable) {
            Log.e("analyzer", err) { "unable to get image color" }
            Result.failure(err)
        }


    private val colorNames by lazy { context.readColorNames() }

    private fun candidateColors(bitmap: Bitmap): ColorProof {
        val palette = Palette.from(bitmap).generate()

        val rgb = palette.dominantSwatch!!.rgb

        val c1 = arrayOf(Color.red(rgb), Color.green(rgb), Color.blue(rgb))

        val colors = colorNames.map {
            it.key to colorDistance(c1, it.value)
        }.sortedBy { it.second }.take(1).map { (key, _) ->
            ColorClue(
                key,
                //colorNames[key]!!.let { Color.rgb(it[0], it[1], it[2]) },
            )
        }.toSet()

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
