package com.letsmobility.avaasasales.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.letsmobility.avaasasales.model.Visitdetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FirebaseRepository {

    String month, day;

    public FcmManager fcmManager = new FcmManager();

    public void saveVisitInfo(final Visitdetail visitdetail) {
        fcmManager.getVisitReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    getMonth();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //        if (dataSnapshot.hasChild(month)) {
                fcmManager.getVisitReference().child(month).child(day).push().setValue(visitdetail);

                //        }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getMonth() throws ParseException {
        String date = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        Date d = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        month = new SimpleDateFormat("MMM").format(cal.getTime());
        day = new SimpleDateFormat("d").format(cal.getTime());
    }

}
