package com.example.yoklamauygulamasi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyCalendar extends DialogFragment {

    Calendar calendar=Calendar.getInstance();


    public interface OnCalendarOnClickListener{
        void onClick(int year,int month,int day);
    }
    public OnCalendarOnClickListener onCalendarOnClickListener;

    public void setOnCalendarOnClickListener(OnCalendarOnClickListener onCalendarOnClickListener) {
        this.onCalendarOnClickListener = onCalendarOnClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(),((view,year,month,dayOfMonth) -> {
            onCalendarOnClickListener.onClick(year,month,dayOfMonth);
        }),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }

    void setDate(int year,int month,int day){
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
    }

    String getDate(){
        // return DateFormat.format("mm.dd.y",calendar).toString();
        //DateFormat dateFormat = DateFormat.getDateInstance();
        // return dateFormat.format(calendar.getTime());
       SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy"); //12.02.2023
       return dateFormat.format(calendar.getTime());

    }
}
