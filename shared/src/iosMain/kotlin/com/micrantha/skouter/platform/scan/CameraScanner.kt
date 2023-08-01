package com.micrantha.skouter.platform.scan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.data.Log
import com.micrantha.skouter.platform.asException
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.value
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.kodein.di.compose.rememberInstance
import platform.AVFoundation.AVCaptureConnection
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureOutput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoDataOutput
import platform.AVFoundation.AVCaptureVideoDataOutputSampleBufferDelegateProtocol
import platform.AVFoundation.AVCaptureVideoOrientationPortrait
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.CoreMedia.CMSampleBufferGetImageBuffer
import platform.CoreMedia.CMSampleBufferRef
import platform.CoreVideo.CVPixelBufferGetBaseAddress
import platform.CoreVideo.CVPixelBufferGetBytesPerRow
import platform.CoreVideo.CVPixelBufferGetHeight
import platform.CoreVideo.CVPixelBufferGetWidth
import platform.CoreVideo.CVPixelBufferLockBaseAddress
import platform.CoreVideo.CVPixelBufferUnlockBaseAddress
import platform.Foundation.NSError
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_queue_create

@Composable
actual fun CameraScanner(
    modifier: Modifier,
    onCameraImage: CameraScannerDispatch
) {

    val viewController by rememberInstance<UIViewController>()

    val stream = rememberCameraStream(onCameraImage) ?: return

    try {

        stream.setup()

        stream.preview(viewController.view)

    } catch (err: Throwable) {
        Log.e("camera session", err)
        return
    }

    DisposableEffect(stream) {
        stream.start()

        onDispose {
            stream.stop()
        }
    }
}


@Composable
private fun rememberCameraStream(onCameraImage: CameraScannerDispatch): CameraStream? = remember {
    AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)?.let {
        CameraStream(it, onCameraImage)
    }
}

class CameraStream(
    private val device: AVCaptureDevice,
    private val onCameraImage: CameraScannerDispatch
) : NSObject(), AVCaptureVideoDataOutputSampleBufferDelegateProtocol {

    private val session = AVCaptureSession()
    private var preview: AVCaptureVideoPreviewLayer? = null
    private val scope = MainScope()

    override fun captureOutput(
        output: AVCaptureOutput,
        didOutputSampleBuffer: CMSampleBufferRef?,
        fromConnection: AVCaptureConnection
    ) {
        didOutputSampleBuffer.image()?.let {
            dispatch_async(dispatch_get_main_queue()) {
                scope.launch {
                    onCameraImage(it)
                }
            }
        }
    }

    private fun CMSampleBufferRef?.image(): CameraImage? {
        val pixelBuffer = CMSampleBufferGetImageBuffer(this) ?: return null

        try {
            CVPixelBufferLockBaseAddress(pixelBuffer, 0)

            val baseAddress = CVPixelBufferGetBaseAddress(pixelBuffer) ?: return null

            val width = CVPixelBufferGetWidth(pixelBuffer)
            val height = CVPixelBufferGetHeight(pixelBuffer)
            val bytesPerRow = CVPixelBufferGetBytesPerRow(pixelBuffer)
            val totalBytes = height * bytesPerRow

            val data = baseAddress.readBytes(totalBytes.toInt())

            return CameraImage(data, width.toInt(), height.toInt())
        } finally {
            CVPixelBufferUnlockBaseAddress(pixelBuffer, 0)
        }
    }

    fun setup() {
        val err = memScoped {
            alloc<ObjCObjectVar<NSError?>>()
        }

        val input = AVCaptureDeviceInput(device, err.ptr)
        err.value?.let { throw it.asException() }

        if (session.canAddInput(input).not())
            throw IllegalStateException("cannot add input to video session")

        session.addInput(input)

        val output = AVCaptureVideoDataOutput().apply {
            setSampleBufferDelegate(
                this@CameraStream,
                dispatch_queue_create("videoDataOutputQueue", null)
            )
        }

        if (session.canAddOutput(output)) {
            session.addOutput(output)
        }
    }

    fun start() {
        session.startRunning()
    }

    fun preview(view: UIView) {
        preview = AVCaptureVideoPreviewLayer(session = session).apply {
            videoGravity = AVLayerVideoGravityResizeAspectFill
            frame = view.bounds
            connection?.videoOrientation = AVCaptureVideoOrientationPortrait
            view.layer.addSublayer(this)
        }
    }

    fun stop() {
        session.stopRunning()

        preview?.removeFromSuperlayer()
    }
}
