package com.letsmobility.avaasasales.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FcmManager {


    private DatabaseReference mainReference;

    public FcmManager() {
        mainReference = FirebaseDatabase.getInstance().getReference();
    }


    public DatabaseReference getVisitReference() {
        return mainReference.child("visit");
    }


}
