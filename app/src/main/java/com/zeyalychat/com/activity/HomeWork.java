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
import com.zeyalychat.com.adpter.HomeWorkAdapter;
import com.zeyalychat.com.bean.HomeWorkBean;
import com.zeyalychat.com.bean.SectionBean;
import com.zeyalychat.com.databinding.HomeWorkBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.*;

public class HomeWork extends AppCompatActivity implements OnClickListener {
    HomeWorkBinding binding;
    ArrayList<HomeWorkBean> homeWorkBeanArrayList;
    Session session;
    int SectionId;
    ArrayList<SectionBean> SectionBeanArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.home_work);
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
        session = new Session(HomeWork.this);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(HomeWork.this, LinearLayoutManager.VERTICAL, false));
        //   binding.syllabusrecyclerview.setLayoutManager(new GridLayoutManager(SyllabusDetailActivity.this, 2));
        //     binding.syllabusrecyclerview.setLayoutManager(new GridLayoutManager(HomeWork.this, 2));
        onItemClickListener();

        Section();

    }

    private void onItemClickListener() {
        binding.backArraow.setOnClickListener(this);
        binding.fab.setOnClickListener(this);
        binding.filter.setOnClickListener(this);
        binding.recyclerview.addOnItemTouchListener(new RecyclerTouchListener(HomeWork.this, binding.recyclerview,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(HomeWork.this, HomeWorkDetailActivity.class);
                        intent.putExtra("HomeWorkId",homeWorkBeanArrayList.get(position).getId());
                        intent.putExtra("SectionId",SectionId);
                        startActivity(intent);

                    }


                    @Override
                    public void onLongClick(View view, int position) {
                        //dialogAlert(position);

                    }
                }));

        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.fablayout.setVisibility(GONE);
            binding.filter.setVisibility(GONE);
        } else {
            binding.fablayout.setVisibility(VISIBLE);
            binding.filter.setVisibility(VISIBLE);
        }

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
            case R.id.fab:
                Intent intent = new Intent(HomeWork.this, HomeWorkCreate.class);
                startActivity(intent);
                break;
        }


    }

    private void DialogSection() {

        Dialog dialog = new Dialog(HomeWork.this);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_list);

        TextView Heading = dialog.findViewById(R.id.Heading);
        Heading.setText(getResources().getText(R.string.SelectSection));

        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout searchRL = dialog.findViewById(R.id.searchRL);
        searchRL.setVisibility(View.GONE);
        RecyclerView mainList = dialog.findViewById(R.id.mainList);
        mainList.setLayoutManager(new LinearLayoutManager(HomeWork.this, LinearLayoutManager.VERTICAL, false));


        DialogRadioBeanAdapter sectionAdapter = new DialogRadioBeanAdapter(HomeWork.this, SectionBeanArrayList);
        mainList.setAdapter(sectionAdapter);


        mainList.addOnItemTouchListener(new RecyclerTouchListener(HomeWork.this, mainList,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        SectionId = SectionBeanArrayList.get(position).getSection_id();
                        GetHomeWork(SectionId);

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
                        DialogRadioBeanAdapter sectionAdapter = new DialogRadioBeanAdapter(HomeWork.this, SectionBeanArrayList);
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
                            GetHomeWork(SectionId);

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

    private void GetHomeWork(int SectionId) {
        homeWorkBeanArrayList = new ArrayList<>();
        String url = "";
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            url = URLHelper.homwwork + "?section_id=" + MainActivity.userInfoArrayList.get(0).getSection_id();
        } else {
            url = URLHelper.homwwork + "?staff_id=" + MainActivity.userInfoArrayList.get(0).getId() + "&section_id=" + SectionId;
        }
        AndroidNetworking.get(url)
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
                                HomeWorkBean homeWorkBean = new HomeWorkBean();
                                homeWorkBean.setAssigned_by(jsonObject.getString("assigned_by"));
                                homeWorkBean.setComplete_by(jsonObject.getString("complete_by"));
                                homeWorkBean.setContent(jsonObject.getString("content"));
                                homeWorkBean.setId(jsonObject.getString("id"));
                                homeWorkBean.setTitle(jsonObject.getString("title"));
                                homeWorkBean.setTo(jsonObject.getString("to"));
                                homeWorkBean.setTo_type(jsonObject.getString("to_type"));
                                JSONObject section = jsonObject.getJSONObject("section");
                                homeWorkBean.setSection_id(section.getString("id"));
                                homeWorkBean.setSection_name(section.getString("name"));
                                JSONObject subject = jsonObject.getJSONObject("subject");
                                homeWorkBean.setSubject_description(subject.getString("description"));
                                homeWorkBean.setSubject_id(subject.getString("id"));
                                homeWorkBean.setSubject_name(subject.getString("name"));
                                homeWorkBean.setSubject_type(subject.getString("type"));
                                JSONObject type = jsonObject.getJSONObject("type");
                                homeWorkBean.setType_id(type.getString("id"));
                                homeWorkBean.setType_name(type.getString("name"));
                                homeWorkBeanArrayList.add(homeWorkBean);
                                System.out.println("assigned_by :::  " + i + "  " + jsonObject.getString("assigned_by"));
                            }
                            HomeWorkAdapter homeWorkAdapter = new HomeWorkAdapter(HomeWork.this, homeWorkBeanArrayList);
                            binding.recyclerview.setAdapter(homeWorkAdapter);

                            if (jsonArray.length() > 0) {
                                binding.noDataLayout.setVisibility(View.GONE);
                            } else {
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
}

