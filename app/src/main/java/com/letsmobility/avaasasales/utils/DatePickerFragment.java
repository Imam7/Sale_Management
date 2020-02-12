package com.letsmobility.avaasasales.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;


import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    DatePickerDialog.OnDateSetListener listener;
    private Date date;
    private Date minDate;
    private boolean isPastDisabled;
    private boolean isFutureDisabled;
    private boolean isCurrentDate;

    public DatePickerFragment() {

    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMinDate(Date date) {
        this.minDate = date;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    public void disablePast() {
        isPastDisabled = true;
    }

    public void disableCurrent() {
        isCurrentDate = true;
    }

    public void disableFuture() {
        isFutureDisabled = true;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year, month, day;
        if (date != null) {
            if (isCurrentDate) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH)+1;
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }
        } else {
            // Use the current date as the default date in the picker
            if (isCurrentDate) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH)+1;
            } else {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            }
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        if(minDate != null) {
            datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
        }
        if (isPastDisabled) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            isPastDisabled=false;
        } else if (isFutureDisabled) {
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            isFutureDisabled=false;
        } else if (isCurrentDate) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()+1*24*60*60*1000);
            isCurrentDate=false;
        }
        // Create a new instance of DatePickerDialog and return it
            return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        listener.onDateSet(view, year, month + 1, day);
    }

}
