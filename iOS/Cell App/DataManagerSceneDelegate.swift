//
//  DataManagerSceneDelegate.swift
//  Cell App
//
//  Created by Luke on 2019-12-08.
//  Copyright © 2019 Cell Group. All rights reserved.
//

import UIKit
import Firebase

class DataManagerSceneDelegate: UIResponder, UISceneDelegate {
    
    var window: UIWindow?
    
    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        guard let windowScene = (scene as? UIWindowScene) else { return }
        
        window = UIWindow(windowScene: windowScene)
        
        let initalViewController = ViewController()
        window!.rootViewController = initalViewController
        window!.makeKeyAndVisible()
    }
}
