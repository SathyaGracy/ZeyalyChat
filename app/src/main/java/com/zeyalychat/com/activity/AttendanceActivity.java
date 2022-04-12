package com.zeyalychat.com.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.zeyalychat.com.R;
import com.zeyalychat.com.adpter.AttendanceAdapter;
import com.zeyalychat.com.adpter.DialogAdapter;
import com.zeyalychat.com.adpter.SectionAdapter;
import com.zeyalychat.com.bean.AttendanceBean;
import com.zeyalychat.com.bean.SectionBean;
import com.zeyalychat.com.databinding.AttendanceListBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class AttendanceActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {
    AttendanceListBinding binding;

    Session session;
    int SectionId = 0;
    String DateValue;
    ArrayList<SectionBean> SectionBeanArrayList;
    ArrayList<AttendanceBean> AttendanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.attendance_list);
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
        session = new Session(AttendanceActivity.this);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(AttendanceActivity.this, LinearLayoutManager.VERTICAL, false));
        onItemClickListener();
        Section();
        attendanceList();

    }

    private void onItemClickListener() {
        binding.backArraow.setOnClickListener(this);
        binding.fab.setOnClickListener(this);
        binding.ToLayout.setOnClickListener(this);
        binding.DateLayout.setOnClickListener(this);
        binding.recyclerview.addOnItemTouchListener(new RecyclerTouchListener(AttendanceActivity.this, binding.recyclerview,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {


                    }


                    @Override
                    public void onLongClick(View view, int position) {
                        //dialogAlert(position);

                    }
                }));
        binding.backArraow.setOnClickListener(this);
       /* if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.fablayout.setVisibility(GONE);
        } else {
            binding.fablayout.setVisibility(VISIBLE);
        }*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArraow:
                finish();
                break;
            case R.id.DateLayout:
                setNormalPicker();
                break;
            case R.id.ToLayout:
                Dialog("Select Section");
                break;
            case R.id.fab:
                Intent intent = new Intent(AttendanceActivity.this, AttendanceCreate.class);
                startActivity(intent);
                break;
        }


    }

    private void Dialog(String heading) {
        final Dialog dialog = new Dialog(AttendanceActivity.this);
        DialogAdapter dialogAdapter;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_list);

        TextView Heading = dialog.findViewById(R.id.Heading);
        Heading.setText(heading);

        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout searchRL = dialog.findViewById(R.id.searchRL);
        searchRL.setVisibility(View.GONE);
        RecyclerView mainList = dialog.findViewById(R.id.mainList);
        mainList.setLayoutManager(new LinearLayoutManager(AttendanceActivity.this, LinearLayoutManager.VERTICAL, false));


        SectionAdapter sectionAdapter = new SectionAdapter(AttendanceActivity.this, SectionBeanArrayList);
        mainList.setAdapter(sectionAdapter);


        mainList.addOnItemTouchListener(new RecyclerTouchListener(AttendanceActivity.this, mainList,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        binding.SelectSectionTxt.setText(SectionBeanArrayList.get(position).getGrade_name()
                                + " " + SectionBeanArrayList.get(position).getSection_name());
                        SectionId = SectionBeanArrayList.get(position).getSection_id();

                        dialog.dismiss();
                        attendanceList();
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


                                SectionBeanArrayList.add(sectionBean);
                            }


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

    private void attendanceList() {
        AttendanceList = new ArrayList<>();

        AndroidNetworking.get(URLHelper.AttendanceList+"type=session"+"&section_id="+SectionId+"&date="+DateValue)
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

                                AttendanceBean attendanceBean = new AttendanceBean();
                                attendanceBean.setAfternoon_session(jsonObject.getString("afternoon_session"));
                                attendanceBean.setMorning_session(jsonObject.getString("morning_session"));
                                attendanceBean.setId(jsonObject.getString("id"));
                                attendanceBean.setAttendance_status(jsonObject.getString("attendance_status"));
                                attendanceBean.setName(jsonObject.getString("name"));
                                AttendanceList.add(attendanceBean);
                            }
                            if(AttendanceList.size()>0) {
                                binding.noDataLayout.setVisibility(GONE);
                                AttendanceAdapter attendanceAdapter = new AttendanceAdapter(AttendanceActivity.this, AttendanceList);
                                binding.recyclerview.setAdapter(attendanceAdapter);
                            }else {
                                binding.noDataLayout.setVisibility(VISIBLE);
                            }

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

    private void setNormalPicker() {
        Calendar now = getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(YEAR), // Initial year selection
                now.get(MONTH), // Initial month selection
                now.get(DAY_OF_MONTH) // Inital day selection
        );
// If you're calling this from a support Fragment
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
        int color = ContextCompat.getColor(AttendanceActivity.this, R.color.green);
        Calendar cal1 = Calendar.getInstance();
        dpd.setAccentColor(color);
        dpd.setMinDate(cal1);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        DateValue = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "";
        binding.DateTxt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + "");
        attendanceList();
    }
}


