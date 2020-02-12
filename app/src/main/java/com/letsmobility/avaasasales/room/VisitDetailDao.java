package com.letsmobility.avaasasales.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.letsmobility.avaasasales.model.Visitdetail;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface VisitDetailDao extends BaseDao<Visitdetail> {

    @Override
    @Insert(onConflict = REPLACE)
    void addItem(Visitdetail item);

    @Override
    @Update(onConflict = REPLACE)
    void updateItem(Visitdetail item);

    @Override
    @Delete
    void removeItem(Visitdetail item);

    @Query("DELETE FROM Visitdetail")
    void removeAll();

    @Query("SELECT * FROM Visitdetail")
    List<Visitdetail> getAllItems();
}