package com.micrantha.skouter.platform.scan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.data.Log
import com.micrantha.bluebell.platform.Platform
import com.micrantha.skouter.platform.asException
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pin
import kotlinx.cinterop.ptr
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
import platform.CoreVideo.kCVPixelBufferPixelFormatTypeKey
import platform.CoreVideo.kCVPixelFormatType_420YpCbCr8BiPlanarVideoRange
import platform.Foundation.NSError
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_queue_create

@Composable
actual fun CameraScanner(
    modifier: Modifier,
    onCameraImage: CameraScannerDispatch
) {

    val platform by rememberInstance<Platform>()

    val stream = rememberCameraStream(onCameraImage) ?: return

    try {

        stream.setup()

        stream.preview(platform.view)

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

    private val session by lazy { AVCaptureSession() }
    private var preview: AVCaptureVideoPreviewLayer? = null
    private val scope by lazy { MainScope() }
    private val dispatchQueue by lazy { dispatch_queue_create("videoDataOutputQueue", null) }

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
        val pixelBuffer = CMSampleBufferGetImageBuffer(this)?.pin() ?: return null
        return CameraImage(pixelBuffer)
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
            setSampleBufferDelegate(this@CameraStream, dispatchQueue)
            if (availableVideoCVPixelFormatTypes.contains(
                    kCVPixelFormatType_420YpCbCr8BiPlanarVideoRange
                )
            ) {
                setVideoSettings(
                    mapOf(
                        kCVPixelBufferPixelFormatTypeKey?.rawValue to kCVPixelFormatType_420YpCbCr8BiPlanarVideoRange
                    )
                )
            }
        }

        if (session.canAddOutput(output)) {
            session.addOutput(output)
        }
    }

    fun start() {
        dispatch_async(dispatchQueue) {
            session.startRunning()
        }
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
