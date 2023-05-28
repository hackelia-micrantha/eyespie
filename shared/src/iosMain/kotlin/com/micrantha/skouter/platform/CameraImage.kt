package com.micrantha.skouter.platform

import platform.UIKit.UIImage
import platform.CoreImage.CIImage
import platform.UIKit.UIImagePNGRepresentation

actual data class CameraImage(
    val data: UIImage
) {
    actual fun toByteArray() = ByteArray(0)
}
