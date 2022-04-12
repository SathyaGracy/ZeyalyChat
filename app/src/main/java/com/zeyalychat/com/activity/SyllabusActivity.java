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
import com.zeyalychat.com.adpter.SyllabusAdapter;
import com.zeyalychat.com.bean.SectionBean;
import com.zeyalychat.com.bean.SyllabusBean;
import com.zeyalychat.com.databinding.SyllabusLayoutBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SyllabusActivity extends AppCompatActivity implements View.OnClickListener {
    SyllabusLayoutBinding binding;
    ArrayList<SyllabusBean> syllabusBeanArrayList;
    SyllabusAdapter syllabusAdapter;
    ArrayList<SectionBean> SectionBeanArrayList;
    Session session;
    int gradeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.syllabus_layout);
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
        session = new Session(SyllabusActivity.this);
        SectionBeanArrayList = new ArrayList<>();
        // binding.syllabusrecyclerview.setLayoutManager(new GridLayoutManager(SyllabusActivity.this, 2));
        binding.syllabusrecyclerview.setLayoutManager(new LinearLayoutManager(SyllabusActivity.this, LinearLayoutManager.VERTICAL, false));
        onItemClickListener();
        Section();
        System.out.println("token------->" + session.getKEYAuth());
    }

    private void onItemClickListener() {
        binding.syllabusrecyclerview.addOnItemTouchListener(new RecyclerTouchListener(SyllabusActivity.this, binding.syllabusrecyclerview,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent mainIntent = new Intent(SyllabusActivity.this, SyllabusDetailActivity.class);
                        mainIntent.putExtra("syllabus_id", syllabusBeanArrayList.get(position).getId());
                        mainIntent.putExtra("title", syllabusBeanArrayList.get(position).getName());
                        startActivity(mainIntent);

                    }


                    @Override
                    public void onLongClick(View view, int position) {
                        //dialogAlert(position);

                    }
                }));
        binding.backArraow.setOnClickListener(this);
        binding.filter.setOnClickListener(this);

        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.filter.setVisibility(View.GONE);
        } else {
            binding.filter.setVisibility(View.VISIBLE);
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
        }


    }

    private void syllabusList(int gradeId) {
        syllabusBeanArrayList = new ArrayList<>();
        String url = "";
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            url = URLHelper.syllabuslist + "?grade_id=" + MainActivity.userInfoArrayList.get(0).getGrade_id();
        } else {
            url = URLHelper.syllabuslist + "?staff_id=" + MainActivity.userInfoArrayList.get(0).getId() + "&grade_id=" + gradeId;
        }
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("json---syllabuslist-------->" + response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SyllabusBean syllabusBean = new SyllabusBean();
                                syllabusBean.setId(jsonObject.getString("id"));
                                syllabusBean.setName(jsonObject.getString("name"));
                                syllabusBean.setGrade(jsonObject.getString("grade"));
                                syllabusBean.setSubject(jsonObject.getString("subject"));
                                syllabusBean.setCode(jsonObject.getString("code"));

                                syllabusBeanArrayList.add(syllabusBean);
                            }

                            syllabusAdapter = new SyllabusAdapter(SyllabusActivity.this, syllabusBeanArrayList);
                            binding.syllabusrecyclerview.setAdapter(syllabusAdapter);
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

    private void DialogSection() {

        Dialog dialog = new Dialog(SyllabusActivity.this);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_list);

        TextView Heading = dialog.findViewById(R.id.Heading);
        Heading.setText(getResources().getText(R.string.SelectSection));

        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout searchRL = dialog.findViewById(R.id.searchRL);
        searchRL.setVisibility(View.GONE);
        RecyclerView mainList = dialog.findViewById(R.id.mainList);
        mainList.setLayoutManager(new LinearLayoutManager(SyllabusActivity.this, LinearLayoutManager.VERTICAL, false));


        DialogRadioBeanAdapter sectionAdapter = new DialogRadioBeanAdapter(SyllabusActivity.this, SectionBeanArrayList);
        mainList.setAdapter(sectionAdapter);


        mainList.addOnItemTouchListener(new RecyclerTouchListener(SyllabusActivity.this, mainList,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        gradeId = SectionBeanArrayList.get(position).getGrade_id();
                        syllabusList(gradeId);
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
                        DialogRadioBeanAdapter sectionAdapter = new DialogRadioBeanAdapter(SyllabusActivity.this, SectionBeanArrayList);
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
                            gradeId = SectionBeanArrayList.get(0).getSection_id();
                            syllabusList(gradeId);

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

