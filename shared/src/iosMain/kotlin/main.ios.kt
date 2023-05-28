import androidx.compose.ui.window.ComposeUIViewController
import com.micrantha.skouter.UIShow
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { UIShow() }
