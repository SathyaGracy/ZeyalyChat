package com.zeyalychat.com.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.zeyalychat.com.R;
import com.zeyalychat.com.adpter.DialogRadioBeanAdapter;
import com.zeyalychat.com.adpter.TimeTableAdapter;
import com.zeyalychat.com.adpter.TimeTableAdapterFull;
import com.zeyalychat.com.bean.SectionBean;
import com.zeyalychat.com.bean.TimeTableBean;
import com.zeyalychat.com.databinding.TimeTableBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.SectoDate;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.threeten.bp.DayOfWeek;


@RequiresApi(api = Build.VERSION_CODES.O)
public class TimeTableActivity extends AppCompatActivity
        implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        OnDateSelectedListener,
        OnMonthChangedListener, OnDateLongClickListener {
    TimeTableBinding binding;
    TimeTableAdapter timeTableAdapter;
    ArrayList<TimeTableBean> timeTableBeanArrayList;
    SectoDate sectoDate;
    Session session;
    int appDay = 0;
    int SectionId;
    ArrayList<SectionBean> SectionBeanArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.time_table);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }
        intView();

    }

    @SuppressLint("RestrictedApi")
    private void intView() {
        sectoDate = new SectoDate(TimeTableActivity.this);
        session = new Session(TimeTableActivity.this);
        binding.timetablerecyclerview.setLayoutManager(new LinearLayoutManager(TimeTableActivity.this, LinearLayoutManager.VERTICAL, false));
        timeTableBeanArrayList = new ArrayList<>();
        SectionBeanArrayList = new ArrayList<>();

        binding.timetablerecyclerview.setAdapter(timeTableAdapter);
        onItemClickListener();
        //  timeTableList();
        Date myDate = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
        String dayName = simpleDateFormat.format(myDate);
        // binding.title.setText(simpleDateFormat2.format(myDate) + " /" + dayName);
        getDateNumber(dayName);

        Section();
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.filter.setVisibility(View.GONE);
        } else {
            binding.filter.setVisibility(View.VISIBLE);
        }

    }

    private void onItemClickListener() {
        CalendarDay today = CalendarDay.from(sectoDate.GetCurrentyear(), sectoDate.GetCurrentmonth(), sectoDate.GetCurrentdate());
        binding.calendarView.setCurrentDate(today);
        binding.calendarView.setSelectedDate(today);

        binding.calendarView.state().edit()
                .setFirstDayOfWeek(DayOfWeek.MONDAY)
                .setMinimumDate(CalendarDay.from(2021, 1, 1))
                .setMaximumDate(CalendarDay.from(2021, 12, 31))
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();
        binding.calendarView.setOnDateChangedListener(this);
        binding.calendarView.setOnMonthChangedListener(this);
        binding.timetablerecyclerview.addOnItemTouchListener(new RecyclerTouchListener(TimeTableActivity.this, binding.timetablerecyclerview,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                      /*  Intent mainIntent = new Intent(TimeTableActivity.this, MessageConversationActivity.class);


                        startActivity(mainIntent);*/

                    }


                    @Override
                    public void onLongClick(View view, int position) {
                        //dialogAlert(position);

                    }
                }));
        binding.backArraow.setOnClickListener(this);
        binding.title.setOnClickListener(this);
        binding.filter.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArraow:
                finish();
                break;
            case R.id.filter:
                DialogSection();
                break;
            case R.id.title:
                //  setNormalPicker();
               /* if (binding.calendarView.getVisibility() == View.VISIBLE) {

                  //  SlideToDown();
                    binding.calendarView.setVisibility(View.GONE);
                } else {

                 //   SlideToAbove();
                    binding.calendarView.setVisibility(View.VISIBLE);
                }*/
                break;
        }


    }


    private void timeTableList(int SectionId) {

        timeTableBeanArrayList = new ArrayList<>();
        String url = "";
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            url = URLHelper.timeTable + "day=" + appDay + "&section_id=" + MainActivity.userInfoArrayList.get(0).getSection_id();
        } else {
            url = URLHelper.timeTable + "day=" + appDay + "&staff_id=" + MainActivity.userInfoArrayList.get(0).getId() + "&section_id=" + SectionId;
        }
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("json---timeTableList-------->" + response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONObject section = jsonObject.getJSONObject("section");
                                JSONObject staff = jsonObject.getJSONObject("staff");
                                JSONObject subject = jsonObject.getJSONObject("subject");
                                TimeTableBean timeTableBean = new TimeTableBean();
                                timeTableBean.setFrom_time(jsonObject.getString("from_time"));
                                timeTableBean.setId(jsonObject.getString("id"));
                                timeTableBean.setTo_time(jsonObject.getString("to_time"));
                                timeTableBean.setSection_id(section.getString("id"));
                                timeTableBean.setSection_name(section.getString("name"));
                                timeTableBean.setStaff_id(staff.getString("id"));
                                timeTableBean.setStaff_name(staff.getString("name"));
                                timeTableBean.setSubject_id(subject.getString("id"));
                                timeTableBean.setSubject_name(subject.getString("name"));
                                timeTableBean.setSubject_description(subject.getString("description"));
                                timeTableBeanArrayList.add(timeTableBean);
                            }
                            TimeTableAdapterFull timeTableAdapter = new TimeTableAdapterFull(TimeTableActivity.this, timeTableBeanArrayList);
                            binding.timetablerecyclerview.setAdapter(timeTableAdapter);
                            if (timeTableBeanArrayList.size() > 0) {
                                binding.noDataLayout.setVisibility(View.GONE);

                            } else {
                                binding.noDataLayout.setVisibility(View.VISIBLE);
                            }


                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                        }


                        // do anything with response
                    }


                    @Override
                    public void onError(ANError error) {
                        // handle error
                        System.out.println(error.getErrorBody());
                        System.out.println(error.getErrorCode());
                        if (error.getErrorCode() == 403) {
                            session.onDestroy();


                        }
                    }
                });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {


        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date myDate = inFormat.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
            String dayName = simpleDateFormat.format(myDate);
            // binding.title.setText(simpleDateFormat2.format(myDate) + " /" + dayName);
            getDateNumber(dayName);
            timeTableList(SectionId);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void getDateNumber(String datesvalue) {
        switch (datesvalue) {
            case "Monday":
                appDay = 1;
                break;
            case "Tuesday":
                appDay = 2;
                break;
            case "Wednesday":
                appDay = 3;
                break;
            case "Thursday":
                appDay = 4;
                break;
            case "Friday":
                appDay = 5;
                break;
            case "Saturday":
                appDay = 6;
                break;
            case "Sunday":
                appDay = 0;
                break;
            default:
                appDay = 7;
                break;
        }

    }


    @Override
    public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        // System.out.println("date  :"+date.getDay()+"");

        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date myDate = inFormat.parse(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
            String dayName = simpleDateFormat.format(myDate);
            // binding.title.setText(simpleDateFormat2.format(myDate) + " /" + dayName);
            getDateNumber(dayName);
            timeTableList(SectionId);
            //  binding.calendarView.setVisibility(View.GONE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
      /*  if (selected) binding.title.setText(FORMATTER.format(date.getDate()));
        else binding.title.setText("No Selection");*/
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }

    private void DialogSection() {

        Dialog dialog = new Dialog(TimeTableActivity.this);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_list);

        TextView Heading = dialog.findViewById(R.id.Heading);
        Heading.setText(getResources().getText(R.string.SelectSection));

        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout searchRL = dialog.findViewById(R.id.searchRL);
        searchRL.setVisibility(View.GONE);
        RecyclerView mainList = dialog.findViewById(R.id.mainList);
        mainList.setLayoutManager(new LinearLayoutManager(TimeTableActivity.this, LinearLayoutManager.VERTICAL, false));


        DialogRadioBeanAdapter sectionAdapter = new DialogRadioBeanAdapter(TimeTableActivity.this, SectionBeanArrayList);
        mainList.setAdapter(sectionAdapter);


        mainList.addOnItemTouchListener(new RecyclerTouchListener(TimeTableActivity.this, mainList,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        SectionId = SectionBeanArrayList.get(position).getSection_id();
                        timeTableList(SectionId);
                        for (int i = 0; i < SectionBeanArrayList.size(); i++) {
                            if (i == position) {
                                if (SectionBeanArrayList.get(i).getSelect()) {
                                    SectionBeanArrayList.get(i).setSelect(false);
                                } else {
                                    SectionBeanArrayList.get(i).setSelect(true);
                                }
                            } else {
                                SectionBeanArrayList.get(i).setSelect(false);
                            }
                        }
                        DialogRadioBeanAdapter sectionAdapter = new DialogRadioBeanAdapter(TimeTableActivity.this, SectionBeanArrayList);
                        mainList.setAdapter(sectionAdapter);
                        dialog.dismiss();


                    }


                    @Override
                    public void onLongClick(View view, int position) {
                        //dialogAlert(position);

                    }
                }));


        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();


    }

    private void Section() {
        SectionBeanArrayList = new ArrayList<>();

        AndroidNetworking.get(URLHelper.section)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                SectionBean sectionBean = new SectionBean();
                                sectionBean.setGrade_id(jsonObject.getInt("grade_id"));
                                sectionBean.setGrade_name(jsonObject.getString("grade_name"));
                                sectionBean.setSection_id(jsonObject.getInt("section_id"));
                                sectionBean.setSection_name(jsonObject.getString("section_name"));
                                sectionBean.setSection_visibility(jsonObject.getBoolean("section_visibility"));
                                if (i == 0)
                                    sectionBean.setSelect(true);
                                else
                                    sectionBean.setSelect(false);


                                SectionBeanArrayList.add(sectionBean);
                            }
                            SectionId = SectionBeanArrayList.get(0).getSection_id();
                            timeTableList(SectionId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // do anything with response
                    }


                    @Override
                    public void onError(ANError error) {
                        // handle error
                        System.out.println(error.getErrorBody());
                        System.out.println(error.getErrorCode());
                        if (error.getErrorCode() == 403) {
                            session.onDestroy();


                        }
                    }
                });
    }
}
