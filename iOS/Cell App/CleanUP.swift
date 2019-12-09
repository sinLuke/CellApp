//
//  Handler.swift
//  Cell App
//
//  Created by Luke on 2019-12-08.
//  Copyright © 2019 Cell Group. All rights reserved.
//

import UIKit
import FirebaseFirestore

class CleanUp {
    static private func removing(collectionName: String, id: [String], idName: String) {
        let db = Firestore.firestore()
        
        db.collection(collectionName).getDocuments { (_snap, error) in
            guard let snap = _snap else {
                print("error \(error?.localizedDescription ?? "Unknown")")
                return
            }
            
            let resultList = snap.documents.compactMap { (snap) -> () in
                if let _id = snap.data()[idName] as? String, !id.contains(_id) {
                    snap.reference.delete()
                }
            }
        }
    }
    
    static private func getIDList(collectionName: String, removing: String, idName: String) {
        let db = Firestore.firestore()
        db.collection(collectionName).getDocuments { (_snap, error) in
            guard let snap = _snap else {
                print("error \(error?.localizedDescription ?? "Unknown")")
                return
            }
            let resultList = snap.documents.compactMap { (snap) -> String? in
                return snap.documentID
            }
            CleanUp.removing(collectionName: removing, id: resultList, idName: idName)
        }
    }
    
    
    
    static func run() {
        CleanUp.getIDList(collectionName: "Users", removing: "UserHistory", idName: "userID")
    }
}

class CallBackArray<T> {
    var array: [(@escaping (Result<T, Error>) -> ()) -> ()]
    var result: [Result<T, Error>]
    
    private init(with array: [(@escaping (Result<T, Error>) -> ()) -> ()], result: [Result<T, Error>]) {
        self.array = array
        self.result = result
    }
    
    init(with array: [(@escaping (Result<T, Error>) -> ()) -> ()]) {
        self.array = array
        self.result = []
    }
    
    func getRestArray() -> (((@escaping (Result<T, Error>) -> ()) -> ())?, CallBackArray) {
        if array.count == 0 {
            return (nil, self)
        } else {
            var rest = array
            let last = rest.remove(at: 0)
            
            return (last, CallBackArray(with: rest, result: []))
        }
    }
    
    func call(with callback: @escaping (Result<[T], Error>) -> ()){
        let (_last, rest) = getRestArray()
        guard let last = _last else {
            var resultT:[T] = []
            for r in self.result {
                switch r {
                case .success(let t):
                    resultT.append(t)
                case .failure(let e):
                    callback(Result.failure(e))
                    return
                }
            }
            callback(Result.success(resultT))
            return
        }
        last() { (result) in
            switch result {
            case .success(let any):
                rest.result.append(result)
                rest.call { (restResult) in
                    
                    if var restResultValue = try? restResult.get() {
                        restResultValue.append(any)
                        callback(Result.success(restResultValue))
                    } else {
                        callback(restResult)
                    }
                }
            case .failure(let error):
                callback(Result.failure(error))
            }
        }
    }
}
