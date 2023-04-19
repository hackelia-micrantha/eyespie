import androidx.compose.ui.window.ComposeUIViewController
import com.micrantha.skouter.App
import com.micrantha.skouter.UIShow
import platform.UIKit.UIViewController

public fun MainViewController(): UIViewController = ComposeUIViewController { UIShow() }
