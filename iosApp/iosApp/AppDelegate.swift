import UIKit

private let app = composeApp.AppDelegate(
    networkMonitor: iOSNetworkMonitor(),
    packageId: "com.micrantha.eyespie"
)

class iOSAppDelegate: NSObject, UIApplicationDelegate {

    var backgroundSessionCompletionHandler: (() -> Void)?

    func application(_ application: UIApplication,
                     handleEventsForBackgroundURLSession identifier: String,
                     completionHandler: @escaping () -> Void) {
        backgroundSessionCompletionHandler = completionHandler
     }

    func urlSessionDidFinishEvents(forBackgroundURLSession session: URLSession) {
        backgroundSessionCompletionHandler?()
        backgroundSessionCompletionHandler = nil
    }
}
