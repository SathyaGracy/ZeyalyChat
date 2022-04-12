package com.zeyalychat.com.activity;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

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
import android.widget.Toast;

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
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.zeyalychat.com.R;
import com.zeyalychat.com.adpter.SectionAdapter;
import com.zeyalychat.com.bean.SectionBean;
import com.zeyalychat.com.databinding.SmsActivityBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class SmsActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    SmsActivityBinding binding;


    ArrayList<SectionBean> SectionBeanArrayList;

    int SectionId;
    Session session;
    String AudioDate;
    String Time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.sms_activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }

        session = new Session(SmsActivity.this);
        onClickListener();
    }

    private void onClickListener() {
        binding.TypeLayout1.setOnClickListener(this);
        binding.backArraow.setOnClickListener(this);
        binding.submit.setOnClickListener(this);
        binding.DatewRLayout.setOnClickListener(this);
        binding.timeRl.setOnClickListener(this);

        Section();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TypeLayout1:
                Dialog();
                break;
            case R.id.TypeRLayout:
                Dialog();
                break;
            case R.id.backArraow:
                finish();
                break;
            case R.id.DatewRLayout:
                setNormalPicker();
                break;
            case R.id.timeRl:
                setTimerPicker();
                break;
            case R.id.submit:
                // openWhatsApp();
                SMS();
               /* Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setAction(Intent.ACTION_VIEW);
                sendIntent.setPackage("com.whatsapp");
                String url = "https://api.whatsapp.com/send?phone=" + "+91 (741) 860-1463" + "&text=" + "Hi";
                sendIntent.setData(Uri.parse(url));
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendIntent);
                }*/
                break;
        }
    }

    private void Dialog() {
        final Dialog dialog = new Dialog(SmsActivity.this);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_list);

        TextView Heading = dialog.findViewById(R.id.Heading);
        Heading.setText("Class");

        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout searchRL = dialog.findViewById(R.id.searchRL);
        searchRL.setVisibility(View.GONE);
        RecyclerView mainList = dialog.findViewById(R.id.mainList);
        mainList.setLayoutManager(new LinearLayoutManager(SmsActivity.this, LinearLayoutManager.VERTICAL, false));


        SectionAdapter sectionAdapter = new SectionAdapter(SmsActivity.this, SectionBeanArrayList);
        mainList.setAdapter(sectionAdapter);


        mainList.addOnItemTouchListener(new RecyclerTouchListener(SmsActivity.this, mainList,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        binding.SelectTypeTxt.setText(SectionBeanArrayList.get(position).getGrade_name() + "" + SectionBeanArrayList.get(position).getSection_name());
                        SectionId = SectionBeanArrayList.get(position).getGrade_id();


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


        AndroidNetworking.get(URLHelper.sectionType)
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
                                sectionBean.setSection_id(jsonObject.getInt("id"));
                                sectionBean.setGrade_name("");
                                sectionBean.setSection_name(jsonObject.getString("name"));
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
    private void SMS() {
        AndroidNetworking.upload(URLHelper.SMS)
                .addMultipartParameter("data", jsonMake().toString())
                .addHeaders("Content-Type", "multipart/form-data")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("json-----SMS------>" + response.toString());
                        finish();

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(SmsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        System.out.println("error value" + error.getErrorBody());
                        System.out.println("error value" + error.getErrorCode());
                    }
                });
    }
    private void openWhatsApp() {
        String smsNumber = "7418601463"; // E164 format without '+' sign
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
        sendIntent.setPackage("com.whatsapp");
       /* if (sendIntent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
            return;
        }*/
        startActivity(sendIntent);
    }
    private JSONObject jsonMake() {

        JSONObject form = new JSONObject();


        try {

            form.put("to_type", 2);
            form.put("to", SectionId);
            form.put("content", binding.TitleTxt.getText().toString());
            form.put("reason", binding.descriptionTxt.getText().toString());



        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("form json" + form.toString());
        return form;
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        AudioDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "";
        binding.DateTxt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + "");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String am_pm = "";
        Calendar datetime = Calendar.getInstance();

        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";

        //  ((Button)getActivity().findViewById(R.id.btnEventStartTime)).setText( strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm );

        Time = hourOfDay + ":" + minute;


        binding.timeTxt.setText(strHrsToShow + ":" + datetime.get(Calendar.MINUTE) + " " + am_pm);

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
        int color = ContextCompat.getColor(SmsActivity.this, R.color.green);
        Calendar cal1 = Calendar.getInstance();
        dpd.setAccentColor(color);
        dpd.setMinDate(cal1);
    }

    private void setTimerPicker() {
        Calendar now = Calendar.getInstance();

        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        int color = ContextCompat.getColor(SmsActivity.this, R.color.green);
        tpd.setAccentColor(color);
        tpd.enableSeconds(true);
        tpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }
}
