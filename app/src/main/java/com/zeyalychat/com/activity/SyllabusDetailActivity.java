package com.zeyalychat.com.activity;

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
import com.zeyalychat.com.adpter.SyllabusDetailAdapter;
import com.zeyalychat.com.bean.SyllabusDetailBean;
import com.zeyalychat.com.databinding.SyllabusDetailBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SyllabusDetailActivity extends AppCompatActivity implements View.OnClickListener {
    SyllabusDetailBinding binding;
    ArrayList<SyllabusDetailBean>syllabusDetailBeanArrayList;
    SyllabusDetailAdapter syllabusAdapter;

    Session session;
    String syllabus_id="",title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.syllabus_detail);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }
        Intent intent = getIntent();
        syllabus_id = intent.getStringExtra("syllabus_id");
        title = intent.getStringExtra("title");
        intView();

    }

    @SuppressLint("RestrictedApi")
    private void intView() {
        session = new Session(SyllabusDetailActivity.this);
        binding.syllabusrecyclerview.setLayoutManager(new LinearLayoutManager(SyllabusDetailActivity.this, LinearLayoutManager.VERTICAL, false));
     //   binding.syllabusrecyclerview.setLayoutManager(new GridLayoutManager(SyllabusDetailActivity.this, 2));

        onItemClickListener();
        syllabusDetail();
        binding.title.setText(title);

    }

    private void onItemClickListener() {
        binding.syllabusrecyclerview.addOnItemTouchListener(new RecyclerTouchListener(SyllabusDetailActivity.this, binding.syllabusrecyclerview,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        /*Intent mainIntent = new Intent(SyllabusActivity.this, MessageConversationActivity.class);
                        mainIntent.putExtra("id", contactBeanArrayList.get(position).getId());
                        mainIntent.putExtra("type", contactBeanArrayList.get(position).getType());
                        mainIntent.putExtra("from", "search");
                        mainIntent.putExtra("name", contactBeanArrayList.get(position).getName());

                        startActivity(mainIntent);*/

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
        }


    }

    private void syllabusDetail() {
        syllabusDetailBeanArrayList=new ArrayList<>();
        AndroidNetworking.get(URLHelper.syllabusDetail+syllabus_id+"&type=details")
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
                                SyllabusDetailBean syllabusBean = new SyllabusDetailBean();
                                syllabusBean.setId(jsonObject.getString("id"));
                                syllabusBean.setChapter(jsonObject.getString("chapter"));
                                syllabusBean.setDescription(jsonObject.getString("description"));


                                syllabusDetailBeanArrayList.add(syllabusBean);
                            }
                            syllabusAdapter = new SyllabusDetailAdapter(SyllabusDetailActivity.this, syllabusDetailBeanArrayList);
                            binding.syllabusrecyclerview.setAdapter(syllabusAdapter);


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

