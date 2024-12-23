import SwiftUI
import composeApp

enum ConfigError : Error {
    case general(reason: String)
}

class iOSConfigDelegate : PlatformConfigDelegate {
    private let envuscator: MobuildEnvuscator = MobuildEnvuscator()

    func requireConfigValue(key: String) -> String throws  {
        do {
            return try envuscator.get(forKey: key)
        } catch {
            throw ConfigError.general(error)
        }
    }

    func getConfigValue(key: String, defaultValue: String) -> String {
       do {
            return try envuscator.get(forKey: key)
       } catch {
            return defaultValue
       }
    }
}

private let app = AppDelegate(
    networkMonitor: iOSNetworkMonitor(),
    appConfig: iOSConfigDelegate()

)

struct ComposeView: UIViewControllerRepresentable {
    var application = IOSApplication(component: app)

    func makeUIViewController(context: Context) -> UIViewController {
        application.createViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        application.update(viewController: uiViewController)
    }

    func dismantleUIViewController(_ uiViewController: Self.UIViewControllerType, coordinator: Self.Coordinator) {
        application.finish(viewController: uiViewController)
    }
}


struct ContentView: View {

    var body: some View {
        ComposeView().ignoresSafeArea(.all, edges: .bottom)
            .onAppear {  }
            .onDisappear {  }
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
