import androidx.compose.ui.window.ComposeUIViewController
import com.micrantha.skouter.UIShow
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow


fun MainViewController(): UIViewController = ComposeUIViewController {
    val viewController = UIApplication.sharedApplication.windows.first().let {
        val window = it as UIWindow
        window.rootViewController!!
    }
    UIShow(viewController)
}
