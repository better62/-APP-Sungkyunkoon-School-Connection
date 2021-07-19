package com.example.googleloginexample.mento_list;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtils {
    private static FirebaseAuth auth=FirebaseAuth.getInstance();
    public static FirebaseFirestore db=FirebaseFirestore.getInstance();

    public static String getUid(){
        return auth.getCurrentUser().getUid().toString();
    }
    FirebaseFirestore getDb(){
        return db;
    }
}
