/*package com.micrantha.eyespie.platform.scan.generator

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
            styleImage.width,
            styleImage.height
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
}*/
package com.micrantha.eyespie.platform.scan.generator

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.SystemClock
import android.util.Log
import android.util.LruCache
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

    // Cache for preprocessed style predictions
    private val stylePredictionCache = LruCache<String, TensorBuffer>(2)

    // Reusable output buffer (input tensors need to be created per request due to size variations)
    private lateinit var reusableOutputBuffer: TensorBuffer

    // Pre-loaded style images
    private val preloadedStyleImages = mutableMapOf<String, Bitmap>()

    // Image processors (reusable)
    private lateinit var inputImageProcessor: ImageProcessor
    private lateinit var outputImageProcessor: ImageProcessor

    init {
        val tfliteOption = Interpreter.Options().apply {
            numThreads = 4 // Increase threads for better performance
            useNNAPI = true // Use Android Neural Networks API if available
        }

        interpreterPredict = Interpreter(
            FileUtil.loadMappedFile(context, PREDICT_ASSET),
            tfliteOption
        )

        interpreterTransform = Interpreter(
            FileUtil.loadMappedFile(context, TRANSFER_ASSET),
            tfliteOption
        )

        initializeReusableComponents()
        preloadStyleImages()
        precomputeStylePredictions()
    }

    private fun initializeReusableComponents() {
        // Get tensor shapes
        outputPredictShape = interpreterPredict.getOutputTensor(0).shape()
        outputTransformShape = interpreterTransform.getOutputTensor(0).shape()

        // Initialize reusable output buffer only (input tensors vary in size)
        reusableOutputBuffer = TensorBuffer.createFixedSize(outputTransformShape, DataType.FLOAT32)

        // Initialize reusable image processors
        inputImageProcessor = ImageProcessor.Builder()
            .add(NormalizeOp(0f, 255f))
            .build()

        outputImageProcessor = ImageProcessor.Builder()
            .add(DequantizeOp(0f, 255f))
            .build()
    }

    private fun preloadStyleImages() {
        listOf(STYLE_ASSET_0, STYLE_ASSET_1).forEach { asset ->
            try {
                context.assets.open(asset).use { inputStream ->
                    preloadedStyleImages[asset] = BitmapFactory.decodeStream(inputStream)
                }
            } catch (e: Exception) {
                Log.e("ImageStyler", "Failed to preload style image: $asset", e)
            }
        }
    }

    private fun precomputeStylePredictions() {
        preloadedStyleImages.forEach { (assetName, bitmap) ->
            try {
                val styleTensorImage = processStyleImage(bitmap)
                val predictOutput =
                    TensorBuffer.createFixedSize(outputPredictShape, DataType.FLOAT32)

                // Debug logging to help diagnose size issues
                Log.d(
                    "ImageStyler",
                    "Style image tensor buffer size: ${styleTensorImage.buffer.remaining()}"
                )
                Log.d(
                    "ImageStyler",
                    "Expected predict input size: ${
                        interpreterPredict.getInputTensor(0).numBytes()
                    }"
                )

                interpreterPredict.run(styleTensorImage.buffer, predictOutput.buffer)
                stylePredictionCache.put(assetName, predictOutput)
                Log.d("ImageStyler", "Precomputed style prediction for: $assetName")
            } catch (e: Exception) {
                Log.e("ImageStyler", "Failed to precompute style prediction for: $assetName", e)
                // Log tensor shape information for debugging
                try {
                    val inputTensor = interpreterPredict.getInputTensor(0)
                    Log.e(
                        "ImageStyler",
                        "Predict model expects: ${
                            inputTensor.shape().contentToString()
                        }, ${inputTensor.numBytes()} bytes"
                    )
                } catch (ex: Exception) {
                    Log.e("ImageStyler", "Could not get predict model input info", ex)
                }
            }
        }
    }

    private suspend fun transfer(bitmap: Bitmap, styleAsset: String): Result<Bitmap> {
        var inferenceTime = SystemClock.uptimeMillis()

        return suspendCoroutine { continuation ->
            try {
                // Get cached style prediction
                val stylePredict = stylePredictionCache.get(styleAsset)
                if (stylePredict == null) {
                    continuation.resume(Result.failure(IllegalStateException("Style prediction not found for: $styleAsset")))
                    return@suspendCoroutine
                }

                // Process input image efficiently
                val inputTensorImage = processInputImageEfficient(bitmap)

                // Run style transfer
                val transformInput = arrayOf(inputTensorImage.buffer, stylePredict.buffer)

                // Reuse output buffer
                reusableOutputBuffer.buffer.rewind()
                interpreterTransform.runForMultipleInputsOutputs(
                    transformInput,
                    mapOf(0 to reusableOutputBuffer.buffer)
                )

                val outputBitmap = getOutputImageEfficient(reusableOutputBuffer)

                inferenceTime = SystemClock.uptimeMillis() - inferenceTime
                Log.d("ImageStyler", "Inference time = $inferenceTime ms")

                continuation.resume(Result.success(outputBitmap))

            } catch (err: Throwable) {
                Log.e("ImageStyler", "Style transfer failed", err)
                continuation.resume(Result.failure(err))
            }
        }
    }

    // Optimized input image processing with consistent sizing
    private fun processInputImageEfficient(image: Bitmap): TensorImage {
        val height = image.height
        val width = image.width
        val cropSize = min(height, width)

        // Use transform model's expected input size
        val targetSize = getTransformModelInputSize()

        val processor = ImageProcessor.Builder()
            .add(ResizeWithCropOrPadOp(cropSize, cropSize))
            .add(ResizeOp(targetSize, targetSize, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()

        // Create new tensor image for each request to avoid size conflicts
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(image)
        return processor.process(tensorImage)
    }

    // Get the expected input size for the transform model (content images)
    private fun getTransformModelInputSize(): Int {
        return try {
            val inputShape = interpreterTransform.getInputTensor(0).shape()
            // Shape is typically [1, height, width, channels]
            if (inputShape.size >= 3) {
                inputShape[1] // height dimension
            } else {
                384 // common default for style transfer
            }
        } catch (e: Exception) {
            Log.w("ImageStyler", "Could not determine transform model input size, using default", e)
            384
        }
    }

    // Get the expected input size for the predict model (style images)
    private fun getPredictModelInputSize(): Int {
        return try {
            val inputShape = interpreterPredict.getInputTensor(0).shape()
            // Shape is typically [1, height, width, channels]
            if (inputShape.size >= 3) {
                inputShape[1] // height dimension
            } else {
                256 // common default for style prediction
            }
        } catch (e: Exception) {
            Log.w("ImageStyler", "Could not determine predict model input size, using default", e)
            256
        }
    }

    // Process style image (used only during initialization)
    private fun processStyleImage(image: Bitmap): TensorImage {
        val height = image.height
        val width = image.width
        val cropSize = min(height, width)

        // Use predict model's expected input size (different from transform model)
        val targetSize = getPredictModelInputSize()

        val processor = ImageProcessor.Builder()
            .add(ResizeWithCropOrPadOp(cropSize, cropSize))
            .add(ResizeOp(targetSize, targetSize, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()

        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(image)
        return processor.process(tensorImage)
    }

    // Optimized output image conversion
    private fun getOutputImageEfficient(output: TensorBuffer): Bitmap {
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(output)
        return outputImageProcessor.process(tensorImage).bitmap
    }

    actual override suspend fun generate(from: CameraImage): Result<CameraImage> {
        val styleAsset = if (Random.nextBoolean()) STYLE_ASSET_0 else STYLE_ASSET_1

        return transfer(from.toBitmap(), styleAsset).map { image ->
            CameraImage(
                _bitmap = image,
                _width = image.width,
                _height = image.height,
                _timestamp = from.timestamp
            )
        }
    }

    // Clean up resources
    fun cleanup() {
        interpreterPredict.close()
        interpreterTransform.close()
        preloadedStyleImages.clear()
        stylePredictionCache.evictAll()
    }
}
