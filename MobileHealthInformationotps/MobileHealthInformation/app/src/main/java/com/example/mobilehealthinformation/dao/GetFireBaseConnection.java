package com.example.mobilehealthinformation.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GetFireBaseConnection {
    private static FirebaseDatabase firebaseDatabase;
    private static FirebaseStorage firebaseStorage;

    // Get Firebase Database Reference
    public static DatabaseReference getConnection(String database) {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
        }
        return firebaseDatabase.getReference(database);
    }

    // Get Firebase Storage Reference
    public static StorageReference getStorageReference() {
        if (firebaseStorage == null) {
            firebaseStorage = FirebaseStorage.getInstance();
        }
        return firebaseStorage.getReference();
    }
}
