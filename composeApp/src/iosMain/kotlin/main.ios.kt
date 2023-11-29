import androidx.compose.ui.window.ComposeUIViewController
import com.micrantha.skouter.UIApplicationController
import com.micrantha.skouter.UIShow
import platform.UIKit.UIApplication
import platform.UIKit.UIView
import platform.UIKit.UIViewController

class IOSApplication : UIApplicationController {

    private val rootViewController: UIViewController by lazy {
        UIApplication.sharedApplication.keyWindow!!.let {
            it.rootViewController!!
        }
    }

    private var currentViewController: UIViewController? = null

    override val currentView: UIView
        get() = currentViewController?.view ?: rootViewController.view

    override fun createViewController(): UIViewController {
        val newViewController = ComposeUIViewController {
            UIShow(this)
        }
        currentViewController = newViewController
        return newViewController
    }

    override fun update(viewController: UIViewController) = Unit

    override fun finish(
        viewController: UIViewController
    ) = Unit

}
