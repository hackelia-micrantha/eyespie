import SwiftUI
import composeApp

struct ComposeView: UIViewControllerRepresentable {
    var application = IOSApplication()
    
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
