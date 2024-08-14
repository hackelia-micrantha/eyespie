import SwiftUI
import composeApp

class iOSConfigDelegate : PlatformConfigDelegate {
    private let envuscator: MobuildEnvuscator = MobuildEnvuscator()
    
    func getConfigValue(key: String) -> String  {
        return try! envuscator.getConfigValue(forKey: key)
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
