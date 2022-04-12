package com.zeyalychat.com.activity;

import static android.view.View.OnClickListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.zeyalychat.com.R;
import com.zeyalychat.com.adpter.CompletedAdapter;
import com.zeyalychat.com.bean.CompletedBean;
import com.zeyalychat.com.databinding.FinishedActivityBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CompletedActivity extends AppCompatActivity implements OnClickListener {
    FinishedActivityBinding binding;
    ArrayList<CompletedBean> finishedBeanArrayList;
    Session session;
    String Id;
    String status;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.finished_activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }
        Intent intent = getIntent();
        Id = intent.getStringExtra("id");
        status = intent.getStringExtra("status");
        System.out.println("Exam completed"+ Id);
        intView();

    }

    @SuppressLint("RestrictedApi")
    private void intView() {
        session = new Session(CompletedActivity.this);
        if(status.equalsIgnoreCase("exam")) {
            binding.statustitle.setText("Finished");
        }else {
            binding.statustitle.setText("Completed");
        }

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(CompletedActivity.this, LinearLayoutManager.VERTICAL, false));
        //   binding.syllabusrecyclerview.setLayoutManager(new GridLayoutManager(SyllabusDetailActivity.this, 2));
        //     binding.syllabusrecyclerview.setLayoutManager(new GridLayoutManager(HomeWork.this, 2));
        onItemClickListener();



        GetCompletedList();
    }


    private void onItemClickListener() {
        binding.backArraow.setOnClickListener(this);

        binding.recyclerview.addOnItemTouchListener(new RecyclerTouchListener(CompletedActivity.this, binding.recyclerview,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(CompletedActivity.this, ViewFileActivity.class);
                        intent.putExtra("url",finishedBeanArrayList.get(position).getFile_url());
                        intent.putExtra("name",finishedBeanArrayList.get(position).getFile_name());
                        startActivity(intent);

                    }


                    @Override
                    public void onLongClick(View view, int position) {
                        //dialogAlert(position);

                    }
                }));

       /* if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.fablayout.setVisibility(GONE);
            binding.filter.setVisibility(GONE);
        } else {
            binding.fablayout.setVisibility(VISIBLE);
            binding.filter.setVisibility(VISIBLE);
        }
*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArraow:
                finish();
                break;

        }


    }



    private void GetCompletedList() {
        finishedBeanArrayList = new ArrayList<>();
        String url;
        if(status.equalsIgnoreCase("exam")) {
            binding.statustitle.setText("Finished");
            url=URLHelper.examstatuscompleted+ Id;

        }else {
            binding.statustitle.setText("Completed");
            url=URLHelper.homeworkstatuscompleted+Id;
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
                               JSONObject student= jsonObject.getJSONObject("student");
                                CompletedBean finishedBean = new CompletedBean();

                                finishedBean.setId(jsonObject.getString("id"));
                                finishedBean.setStudent_id(student.getString("id"));
                                finishedBean.setStudent_name(student.getString("name"));
                                finishedBean.setStudent_profile(student.getString("profile_image_url"));
                                finishedBean.setView_status(jsonObject.getString("view_status"));
                                finishedBean.setFile_name(jsonObject.getString("file_name"));
                                finishedBean.setFile_url(jsonObject.getString("file_url"));

                                finishedBeanArrayList.add(finishedBean);


                            }


                           // System.out.println("Meeting"+meetingBeanArrayList);
                            CompletedAdapter finishedAdapter = new CompletedAdapter(CompletedActivity.this, finishedBeanArrayList);
                            binding.recyclerview.setAdapter(finishedAdapter);

                            if (jsonArray.length() > 0) {
                                binding.noDataLayout.setVisibility(View.GONE);
                                binding.dataRl.setVisibility(View.VISIBLE);
                                binding.totalcount.setText(jsonArray.length()+"");
                            } else {
                                binding.noDataLayout.setVisibility(View.VISIBLE);
                                binding.dataRl.setVisibility(View.GONE);
                                binding.totalcount.setText("0");
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

