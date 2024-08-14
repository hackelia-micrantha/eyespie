//
//  NetworkMonitor.swift
//  iosApp
//
//  Created by Ryan Jennings on 2024-08-04.
//  Copyright © 2024 orgName. All rights reserved.
//
import Network
import composeApp

class iOSNetworkMonitor: NetworkMonitor {
    private let monitor = NWPathMonitor()
    private let queue = DispatchQueue(label: "NetworkMonitor")

    func startMonitoring(onUpdate: @escaping (KotlinBoolean) -> Void) {
        monitor.pathUpdateHandler = { path in
            onUpdate(KotlinBoolean(value: path.status == .satisfied))
        }
        monitor.start(queue: queue)
    }
    
    func stopMonitoring() {
        monitor.cancel()
    }
}
