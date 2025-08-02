import SwiftUI
import composeApp

enum ConfigError: Error {
    case general(reason: String)
}

struct ComposeView: UIViewControllerRepresentable {
    var application = IOSApplication(component: AppDelegate(
        networkMonitor: iOSNetworkMonitor(),
        packageId: "com.micrantha.eyespie"
    )
    )

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
        .onAppear {
        }
        .onDisappear {
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
