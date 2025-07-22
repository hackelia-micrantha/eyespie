import androidx.compose.ui.window.ComposeUIViewController
import com.micrantha.bluebell.platform.AppConfigDelegate
import com.micrantha.eyespie.AppDelegate
import com.micrantha.eyespie.UIApplicationController
import com.micrantha.eyespie.UIShow
import platform.UIKit.UIApplication
import platform.UIKit.UIView
import platform.UIKit.UIViewController

class IOSApplication(
    private val component: AppDelegate
) : UIApplicationController {

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
            UIShow(component)
        }
        currentViewController = newViewController
        return newViewController
    }

    override fun update(viewController: UIViewController) = Unit

    override fun finish(
        viewController: UIViewController
    ) = Unit

}
