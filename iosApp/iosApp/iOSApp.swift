import SwiftUI
import app

@main
struct iOSApp: App {

    init() {
        KoinInitializerKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
