//
//  ViewController.swift
//  Cell App
//
//  Created by Luke Yin on 2019-09-13.
//  Copyright © 2019 Cell Group. All rights reserved.
//

import UIKit
import FirebaseFirestore

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        Firestore.firestore().collection("STEP").getDocuments { (_snap, error) in
            guard let snap = _snap else {return}
            
            snap.documents.map { (doc) -> () in
                doc.reference.updateData(["ANSWER_EXPLANATION" : "Good job!"])
                
            }
        }
        // Do any additional setup after loading the view.
    }

    

}

