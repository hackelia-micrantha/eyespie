import SwiftUI
import composeApp

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(iOSAppDelegate.self) var appDelegate

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
