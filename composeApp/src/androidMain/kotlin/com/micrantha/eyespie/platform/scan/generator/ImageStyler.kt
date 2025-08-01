package com.micrantha.eyespie.platform.scan.generator

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.SystemClock
import android.util.Log
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.components.ImageGenerator
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.DequantizeOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.min
import kotlin.random.Random

const val PREDICT_ASSET = "stylepredict_magenta_android.tflite"
const val TRANSFER_ASSET = "styletransfer_magenta_android.tflite"

const val STYLE_ASSET_0 = "style1.jpg"
const val STYLE_ASSET_1 = "style2.jpg"

actual class ImageStyler(
    private val context: Context
) : ImageGenerator<CameraImage> {
    private var interpreterPredict: Interpreter
    private var interpreterTransform: Interpreter
    private var outputPredictShape = intArrayOf()
    private var outputTransformShape = intArrayOf()

    init {
        val tfliteOption = Interpreter.Options()
        tfliteOption.numThreads = 2

        interpreterPredict = Interpreter(
            FileUtil.loadMappedFile(
                context,
                PREDICT_ASSET,
            ), tfliteOption
        )

        interpreterTransform = Interpreter(
            FileUtil.loadMappedFile(
                context,
                TRANSFER_ASSET
            ), tfliteOption
        )
    }

    private suspend fun transfer(bitmap: Bitmap, styleImage: Bitmap): Result<Bitmap> {

        // Inference time is the difference between the system time at the start and finish of the
        // process
        var inferenceTime = SystemClock.uptimeMillis()

        val inputTensorImage = processInputImage(
            bitmap,
            bitmap.width,
            bitmap.height
        )

        val styleTensorImage = processInputImage(
            styleImage,
            bitmap.width,
            bitmap.height
        )
        val predictOutput = TensorBuffer.createFixedSize(
            outputPredictShape, DataType.FLOAT32
        )
        // The results of this inference could be reused given the style does not change
        // That would be a good practice in case this was applied to a video stream.
        interpreterPredict.run(styleTensorImage.buffer, predictOutput.buffer)

        val transformInput =
            arrayOf(inputTensorImage.buffer, predictOutput.buffer)
        val outputImage = TensorBuffer.createFixedSize(
            outputTransformShape, DataType.FLOAT32
        )
        interpreterTransform.runForMultipleInputsOutputs(
            transformInput,
            mapOf(Pair(0, outputImage.buffer))
        )
        return suspendCoroutine { continuation ->
            try {
                getOutputImage(outputImage)?.let { outputBitmap ->
                    inferenceTime = SystemClock.uptimeMillis() - inferenceTime
                    Log.d("generator", "Inference time = $inferenceTime ms")
                    continuation.resume(Result.success(outputBitmap))
                }
            } catch (err: Throwable) {
                continuation.resume(Result.failure(err))
            }
        }
    }

    // Preprocess the image and convert it into a TensorImage for
    // transformation.
    private fun processInputImage(
        image: Bitmap,
        targetWidth: Int,
        targetHeight: Int
    ): TensorImage {
        val height = image.height
        val width = image.width
        val cropSize = min(height, width)
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeWithCropOrPadOp(cropSize, cropSize))
            .add(
                ResizeOp(
                    targetHeight,
                    targetWidth,
                    ResizeOp.ResizeMethod.BILINEAR
                )
            )
            .add(NormalizeOp(0f, 255f))
            .build()
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(image)
        return imageProcessor.process(tensorImage)
    }

    // Convert output bytebuffer to bitmap image.
    private fun getOutputImage(output: TensorBuffer): Bitmap {
        val imagePostProcessor = ImageProcessor.Builder()
            .add(DequantizeOp(0f, 255f)).build()
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(output)
        return imagePostProcessor.process(tensorImage).bitmap
    }

    actual override suspend fun generate(from: CameraImage): Result<CameraImage> {
        val styleAsset = if (Random.nextBoolean()) STYLE_ASSET_0 else STYLE_ASSET_1
        val styleImage = context.assets.open(styleAsset).use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
        return transfer(from.toBitmap(), styleImage).map { image ->
            CameraImage(
                _bitmap = image,
                _width = image.width,
                _height = image.height,
                _rotation = from.rotation,
                _timestamp = from.timestamp
            )
        }
    }
}