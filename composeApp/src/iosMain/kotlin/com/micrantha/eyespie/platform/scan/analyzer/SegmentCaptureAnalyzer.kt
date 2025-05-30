package com.micrantha.eyespie.platform.scan.analyzer

import com.micrantha.eyespie.domain.entities.SegmentClue
import com.micrantha.eyespie.domain.entities.SegmentProof
import com.micrantha.eyespie.platform.scan.CameraAnalyzerConfig
import com.micrantha.eyespie.platform.scan.CameraCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.CameraStreamAnalyzer
import com.micrantha.eyespie.platform.scan.components.AnalyzerCallback
import com.micrantha.eyespie.platform.scan.components.CaptureAnalyzer
import com.micrantha.eyespie.platform.scan.components.StreamAnalyzer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGContextAddPath
import platform.CoreGraphics.CGContextClearRect
import platform.CoreGraphics.CGContextClip
import platform.CoreGraphics.CGContextScaleCTM
import platform.CoreGraphics.CGContextTranslateCTM
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGPathRef
import platform.CoreGraphics.CGRectMake
import platform.CoreVideo.CVImageBufferRef
import platform.CoreVideo.CVPixelBufferGetBaseAddress
import platform.CoreVideo.CVPixelBufferGetBytesPerRow
import platform.CoreVideo.CVPixelBufferGetHeight
import platform.CoreVideo.CVPixelBufferGetWidth
import platform.CoreVideo.CVPixelBufferLockBaseAddress
import platform.CoreVideo.CVPixelBufferUnlockBaseAddress
import platform.Vision.VNContoursObservation
import platform.Vision.VNDetectContoursRequest


typealias SegmentAnalyzerConfig = CameraAnalyzerConfig<SegmentProof, VNDetectContoursRequest, VNContoursObservation>

actual class SegmentCaptureAnalyzer :
    CameraCaptureAnalyzer<SegmentProof, VNDetectContoursRequest, VNContoursObservation>(config()),
    CaptureAnalyzer<SegmentProof>

class SegmentStreamAnalyzer(
    callback: AnalyzerCallback<SegmentProof>,
) : CameraStreamAnalyzer<SegmentProof, VNDetectContoursRequest, VNContoursObservation>(
    config(),
    callback
), StreamAnalyzer


@OptIn(ExperimentalForeignApi::class)
private fun config(): SegmentAnalyzerConfig = object : SegmentAnalyzerConfig {

    override val filter = { results: List<*>? ->
        results?.filterIsInstance<VNContoursObservation>() ?: emptyList()
    }

    override fun request() = VNDetectContoursRequest()

    override fun map(response: List<VNContoursObservation>, image: CameraImage): SegmentProof =
        response.map {
            SegmentClue(
                CameraImage(mask(image.data, it.normalizedPath), image.orientation).toImageBitmap()
            )
        }

    private fun mask(imageBuffer: CVImageBufferRef, path: CGPathRef?): CVImageBufferRef {
        return memScoped {
            CVPixelBufferLockBaseAddress(imageBuffer, 0u)

            val baseAddress = CVPixelBufferGetBaseAddress(imageBuffer)
            val bytesPerRow = CVPixelBufferGetBytesPerRow(imageBuffer)
            val width = CVPixelBufferGetWidth(imageBuffer)
            val height = CVPixelBufferGetHeight(imageBuffer)

            // Create a context for drawing onto the pixel buffer
            val colorSpace = CGColorSpaceCreateDeviceRGB()
            val context = CGBitmapContextCreate(
                baseAddress,
                width,
                height,
                8u,
                bytesPerRow,
                colorSpace,
                CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value
            )

            CGContextTranslateCTM(context, 0.0, height.toDouble())
            CGContextScaleCTM(context, 1.0, -1.0)
            CGContextAddPath(context, path)
            CGContextClip(context)
            CGContextClearRect(
                context,
                CGRectMake(x = 0.0, y = 0.0, width = width.toDouble(), height = height.toDouble())
            )

            // Unlock the pixel buffer
            CVPixelBufferUnlockBaseAddress(imageBuffer, 0u)

            imageBuffer
        }
    }

}
