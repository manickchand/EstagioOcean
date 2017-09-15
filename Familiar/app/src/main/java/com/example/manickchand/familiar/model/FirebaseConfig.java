package com.example.manickchand.familiar.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ocean on 13/09/17.
 */

public class FirebaseConfig {

    private static DatabaseReference dr;
    private static FirebaseAuth auth;

    public static DatabaseReference getFirebase(){

        if(dr==null)dr= FirebaseDatabase.getInstance().getReference();

        return  dr;

    }

    public static FirebaseAuth getAuth(){
        if(auth==null)auth= FirebaseAuth.getInstance();

        return  auth;
    }

}
