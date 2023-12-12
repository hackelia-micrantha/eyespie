package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraCaptureAnalyzer
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ScanSegments
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


typealias SegmentAnalyzerConfig = CameraAnalyzerConfig<ScanSegments, VNDetectContoursRequest, VNContoursObservation>

actual class SegmentCaptureAnalyzer(
    config: SegmentAnalyzerConfig = config()
) : CameraCaptureAnalyzer<ScanSegments, VNDetectContoursRequest, VNContoursObservation>(config),
    CaptureAnalyzer<ScanSegments>, StreamAnalyzer<ScanSegments>

@OptIn(ExperimentalForeignApi::class)
private fun config(): SegmentAnalyzerConfig = object : SegmentAnalyzerConfig {

    override val filter = { results: List<*>? ->
        results?.filterIsInstance<VNContoursObservation>() ?: emptyList()
    }

    override fun request() = VNDetectContoursRequest()

    override fun map(response: List<VNContoursObservation>, image: CameraImage): ScanSegments =
        response.map {
            CameraImage(mask(image.data, it.normalizedPath), image.orientation)
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
