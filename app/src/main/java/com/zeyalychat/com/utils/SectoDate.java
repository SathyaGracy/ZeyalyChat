package com.zeyalychat.com.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SectoDate {
    Context cntx;
    public SectoDate(Context cntx) {
        // TODO Auto-generated constructor stub
        this.cntx = cntx;

    }

    public String secToDate(long miliSecvalue) {

        // milliseconds
        long miliSec = miliSecvalue;

        // Creating date format
        DateFormat simple = new SimpleDateFormat("dd/MM/yyyy");

        // Creating date from milliseconds
        // using Date() constructor
        Date result = new Date(miliSec);

        // Formatting Date according to the
        // given format
        System.out.println(simple.format(result));
        return simple.format(result);
    }
    public String sectoTime(long timemils){
        DateFormat formatter = new SimpleDateFormat("h:mm aa");
        formatter.setTimeZone(TimeZone.getDefault());
        String text = formatter.format(new Date(timemils));
        return text;
    }
    public int GetCurrentdate(){

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());


        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        return currentDay;
    }  public int GetCurrentmonth(){

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());


        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        return currentMonth;
    }public int GetCurrentyear(){

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());


        int currentMonth = calendar.get(Calendar.YEAR) ;

        return currentMonth;
    }
}
