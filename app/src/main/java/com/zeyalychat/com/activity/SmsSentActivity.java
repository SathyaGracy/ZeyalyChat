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
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.zeyalychat.com.R;
import com.zeyalychat.com.adpter.DialogRadioBeanAdapter;
import com.zeyalychat.com.adpter.SmsAdapter;
import com.zeyalychat.com.bean.SectionBean;
import com.zeyalychat.com.bean.SmsBean;
import com.zeyalychat.com.databinding.SmsListBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SmsSentActivity extends AppCompatActivity implements View.OnClickListener {
    SmsListBinding binding;
    ArrayList<SmsBean> smsBeanArrayList;
    SmsAdapter smsAdapter;
    Session session;

    ArrayList<SectionBean> SectionBeanArrayList;
    int SectionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.sms_list);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }

        intView();
        Section();
       /* if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.fablayout.setVisibility(GONE);
            binding.filter.setVisibility(GONE);
        } else {
            binding.fablayout.setVisibility(VISIBLE);
            binding.filter.setVisibility(VISIBLE);
        }*/
    }

    private void intView() {
        session = new Session(SmsSentActivity.this);
        smsBeanArrayList =new ArrayList<>();
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(SmsSentActivity.this, LinearLayoutManager.VERTICAL, false));


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmsSentActivity.this, SmsActivity.class);
                startActivity(intent);
            }
        });
        binding.backArraow.setOnClickListener(this);
        binding.filter.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backArraow:
                finish();
                break;
            case R.id.filter:
                DialogSection();
                break;
        }
    }
    private void DialogSection() {

        Dialog dialog = new Dialog(SmsSentActivity.this);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_list);

        TextView Heading = dialog.findViewById(R.id.Heading);
        Heading.setText(getResources().getText(R.string.SelectSection));

        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout searchRL = dialog.findViewById(R.id.searchRL);
        searchRL.setVisibility(View.GONE);
        RecyclerView mainList = dialog.findViewById(R.id.mainList);
        mainList.setLayoutManager(new LinearLayoutManager(SmsSentActivity.this, LinearLayoutManager.VERTICAL, false));


        DialogRadioBeanAdapter sectionAdapter = new DialogRadioBeanAdapter(SmsSentActivity.this, SectionBeanArrayList);
        mainList.setAdapter(sectionAdapter);


        mainList.addOnItemTouchListener(new RecyclerTouchListener(SmsSentActivity.this, mainList,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        SectionId = SectionBeanArrayList.get(position).getSection_id();
                        GetSMS(SectionId);

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
                        DialogRadioBeanAdapter sectionAdapter = new DialogRadioBeanAdapter(SmsSentActivity.this, SectionBeanArrayList);
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
    private void GetSMS(int sectionId) {
        System.out.println("sectionId"+sectionId);
        AndroidNetworking.get(URLHelper.SMSGET +sectionId)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("json---SMS-------->" + response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SmsBean smsBean = new SmsBean();
                                smsBean.setId(jsonObject.getInt("id"));
                                smsBean.setContent(jsonObject.getString("content"));
                                // examBean.setStudent_id(jsonObject.getString("student_id"));
                                //   examBean.setTotal_marks(jsonObject.getString("total_marks"));
                                //  JSONObject exam = jsonObject.getJSONObject("exam");
                                smsBean.setReason(jsonObject.getString("reason"));
                                // examBean.setExamid(exam.getString("id"));



                                smsBeanArrayList.add(smsBean);
                            }

                            smsAdapter = new SmsAdapter(SmsSentActivity.this, smsBeanArrayList);
                            binding.recyclerview.setAdapter(smsAdapter);

                            if(jsonArray.length()>0){
                                binding.noDataLayout.setVisibility(View.GONE);
                            }else {
                                binding.noDataLayout.setVisibility(View.VISIBLE);
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
                               // sectionBean.setGrade_id(jsonObject.getInt("id"));
                                sectionBean.setGrade_name("");
                                sectionBean.setSection_id(jsonObject.getInt("id"));
                                sectionBean.setSection_name(jsonObject.getString("name"));
                                //sectionBean.setSection_visibility(jsonObject.getBoolean("section_visibility"));
                                if (i == 0)
                                    sectionBean.setSelect(true);
                                else
                                    sectionBean.setSelect(false);


                                SectionBeanArrayList.add(sectionBean);
                            }
                            SectionId = SectionBeanArrayList.get(0).getSection_id();
                            GetSMS(SectionId);

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
