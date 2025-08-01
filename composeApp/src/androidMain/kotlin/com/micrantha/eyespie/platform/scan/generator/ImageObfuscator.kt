package com.micrantha.eyespie.platform.scan.generator

import android.graphics.Bitmap
import androidx.core.graphics.scale
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.components.ImageGenerator

actual class ImageObfuscator : ImageGenerator<CameraImage> {

    private fun pixelateBitmap(bitmap: Bitmap, pixelSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val smallBitmap = bitmap.scale(width / pixelSize, height / pixelSize, false)

        return smallBitmap.scale(width, height, false)
    }

    actual override suspend fun generate(from: CameraImage): Result<CameraImage> = try {
        pixelateBitmap(from.toBitmap(), 4).let {
            CameraImage(
                _bitmap = it,
                _width = it.width,
                _height = it.height,
                _timestamp = from.timestamp
            )
        }
        Result.success(from)
    } catch (err: Throwable) {
        Result.failure(err)
    }
}
