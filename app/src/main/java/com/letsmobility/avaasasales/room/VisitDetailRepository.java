package com.letsmobility.avaasasales.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.letsmobility.avaasasales.model.Visitdetail;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class VisitDetailRepository {

    private String DB_NAME = "db_task";

    private SalesDatabase salesDatabase;

    public VisitDetailRepository(Context context) {
        salesDatabase = Room.databaseBuilder(context, SalesDatabase.class, DB_NAME).build();
    }


    public void insertTask(final Visitdetail VisitDetail) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                salesDatabase.visitDetailDao().addItem(VisitDetail);
                return null;
            }
        }.execute();
    }

    public void updateTask(final Visitdetail visitdetail) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                salesDatabase.visitDetailDao().updateItem(visitdetail);
                return null;
            }
        }.execute();
    }


    public void deleteTask(final Visitdetail visitdetail) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                salesDatabase.visitDetailDao().removeItem(visitdetail);
                return null;
            }
        }.execute();
    }

    public void deleteAllTask() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                salesDatabase.visitDetailDao().removeAll();
                return null;
            }
        }.execute();
    }



   private class ReadTask extends AsyncTask<Void, Void, List<Visitdetail>>{

       @Override
       protected List<Visitdetail> doInBackground(Void... voids) {
           return salesDatabase.visitDetailDao().getAllItems();
       }
   }

    public List<Visitdetail> readTask() throws ExecutionException, InterruptedException {
          return new AsyncTask<Void, Void, List<Visitdetail>>(){

            @Override
            protected List<Visitdetail> doInBackground(Void... voids) {
                return salesDatabase.visitDetailDao().getAllItems();
            }
        }.execute().get();
    }

}