package com.zeyalychat.com.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.zeyalychat.com.R;
import com.zeyalychat.com.adpter.DialogRadioBeanAdapter;
import com.zeyalychat.com.adpter.TutorialAdapter;
import com.zeyalychat.com.bean.SectionBean;
import com.zeyalychat.com.bean.TutorialBean;
import com.zeyalychat.com.databinding.SyllabusLayoutBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoListActivity extends AppCompatActivity implements View.OnClickListener {
    SyllabusLayoutBinding binding;
    ArrayList<TutorialBean> tutorialBeanArrayList;
    TutorialAdapter tutorialAdapter;
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
        session = new Session(VideoListActivity.this);
        SectionBeanArrayList = new ArrayList<>();
        //  binding.syllabusrecyclerview.setLayoutManager(new LinearLayoutManager(FeesListActivity.this, LinearLayoutManager.VERTICAL, false));
        //   binding.syllabusrecyclerview.setLayoutManager(new GridLayoutManager(SyllabusDetailActivity.this, 2));
        binding.syllabusrecyclerview.setLayoutManager(new GridLayoutManager(VideoListActivity.this, 2));
        onItemClickListener();
        Section();
        binding.title.setText(getText(R.string.tutorial));
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.filter.setVisibility(View.GONE);
        } else {
            binding.filter.setVisibility(View.VISIBLE);
        }

    }

    private void onItemClickListener() {
        binding.filter.setOnClickListener(this);
        binding.backArraow.setOnClickListener(this);
        binding.syllabusrecyclerview.addOnItemTouchListener(new RecyclerTouchListener(VideoListActivity.this, binding.syllabusrecyclerview,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if (tutorialBeanArrayList.get(position).getIs_external().equalsIgnoreCase("false")) {
                            Intent mainIntent = new Intent(VideoListActivity.this, VideoViewActivity.class);
                            mainIntent.putExtra("url", tutorialBeanArrayList.get(position).getUrl());
                            startActivity(mainIntent);
                        } else {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tutorialBeanArrayList.get(position).getUrl())));
                        }

                    }


                    @Override
                    public void onLongClick(View view, int position) {
                        //dialogAlert(position);

                    }
                }));
        binding.backArraow.setOnClickListener(this);


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

    /*private void feetype() {
        feesBeanArrayList=new ArrayList<>();
        AndroidNetworking.get(URLHelper.feetype)
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
                                FeeTypeBean feeTypeBean = new FeeTypeBean();
                                feeTypeBean.setId(jsonObject.getString("id"));
                                feeTypeBean.setIs_sys(jsonObject.getString("is_sys"));
                                feeTypeBean.setName(jsonObject.getString("name"));


                                feeTypeBeanArrayList.add(feeTypeBean);
                            }
                            feeTypeAdapter = new FeeTypeAdapter(FeesListActivity.this, feeTypeBeanArrayList);
                            binding.syllabusrecyclerview.setAdapter(feeTypeAdapter);


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
                    }
                });
    }*/
    private void videoList(int gradeId) {
        tutorialBeanArrayList = new ArrayList<>();
        AndroidNetworking.get(URLHelper.tutorial)
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
                                TutorialBean tutorialBean = new TutorialBean();
                                tutorialBean.setId(jsonObject.getString("id"));
                                tutorialBean.setApproved_by(jsonObject.getString("approved_by"));
                                tutorialBean.setApproved_date(jsonObject.getString("approved_date"));
                                tutorialBean.setSection_id(jsonObject.getString("section_id"));
                                tutorialBean.setSubject_id(jsonObject.getString("subject_id"));
                                tutorialBean.setTitle(jsonObject.getString("title"));
                                JSONObject attachments = jsonObject.getJSONObject("attachments");
                                tutorialBean.setAttachments_id(attachments.getString("id"));
                                tutorialBean.setIs_external(attachments.getString("is_external"));
                                tutorialBean.setUrl(attachments.getString("url"));

                                tutorialBeanArrayList.add(tutorialBean);
                            }
                            tutorialAdapter = new TutorialAdapter(VideoListActivity.this, tutorialBeanArrayList);
                            binding.syllabusrecyclerview.setAdapter(tutorialAdapter);
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
   /* private void Dialog(int pos) {
        Dialog dialog = new Dialog(FeesListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_more);
        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        TextView textView= dialog.findViewById(R.id.name_txt);
        textView.setText(mdataset.get(pos).getContent());
        dialog.show();
        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }*/

    private void DialogSection() {

        Dialog dialog = new Dialog(VideoListActivity.this);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_list);

        TextView Heading = dialog.findViewById(R.id.Heading);
        Heading.setText(getResources().getText(R.string.SelectSection));

        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout searchRL = dialog.findViewById(R.id.searchRL);
        searchRL.setVisibility(View.GONE);
        RecyclerView mainList = dialog.findViewById(R.id.mainList);
        mainList.setLayoutManager(new LinearLayoutManager(VideoListActivity.this, LinearLayoutManager.VERTICAL, false));


        DialogRadioBeanAdapter sectionAdapter = new DialogRadioBeanAdapter(VideoListActivity.this, SectionBeanArrayList);
        mainList.setAdapter(sectionAdapter);


        mainList.addOnItemTouchListener(new RecyclerTouchListener(VideoListActivity.this, mainList,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        gradeId = SectionBeanArrayList.get(position).getGrade_id();
                        videoList(gradeId);
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
                        DialogRadioBeanAdapter sectionAdapter = new DialogRadioBeanAdapter(VideoListActivity.this, SectionBeanArrayList);
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
                            videoList(gradeId);

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

