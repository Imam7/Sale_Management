package com.letsmobility.avaasasales.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.letsmobility.avaasasales.model.Visitdetail;

@Database(entities = {Visitdetail.class}, version = 1,exportSchema = false)
public abstract class SalesDatabase extends RoomDatabase {

    public abstract VisitDetailDao visitDetailDao();

}
