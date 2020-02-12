package com.letsmobility.avaasasales.room;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


public interface BaseDao<T> {

    @Insert(onConflict = REPLACE)
    void addItem(T item);

    @Update(onConflict = REPLACE)
    void updateItem(T item);

    @Delete
    void removeItem(T item);
}
