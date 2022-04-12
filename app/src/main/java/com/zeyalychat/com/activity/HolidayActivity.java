package com.zeyalychat.com.activity;

import android.annotation.SuppressLint;
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
import com.zeyalychat.com.adpter.HolidayAdapter;
import com.zeyalychat.com.bean.HolidayBean;
import com.zeyalychat.com.databinding.SyllabusLayoutBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HolidayActivity extends AppCompatActivity implements View.OnClickListener {
    SyllabusLayoutBinding binding;
    ArrayList<HolidayBean> holidayBeanArrayList;
    HolidayAdapter holidayAdapter;

    Session session;


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
        session = new Session(HolidayActivity.this);
        binding.syllabusrecyclerview.setLayoutManager(new LinearLayoutManager(HolidayActivity.this, LinearLayoutManager.VERTICAL, false));
        //   binding.syllabusrecyclerview.setLayoutManager(new GridLayoutManager(SyllabusDetailActivity.this, 2));
        binding.title.setText(getResources().getText(R.string.holiday));
        onItemClickListener();
        syllabusDetail();
        binding.filter.setVisibility(View.GONE);

    }

    private void onItemClickListener() {
        binding.syllabusrecyclerview.addOnItemTouchListener(new RecyclerTouchListener(HolidayActivity.this, binding.syllabusrecyclerview,
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
        holidayBeanArrayList = new ArrayList<>();
        AndroidNetworking.get(URLHelper.holiday)
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
                                HolidayBean holidayBean = new HolidayBean();

                                holidayBean.setId(jsonObject.getString("id"));
                                holidayBean.setDescription(jsonObject.getString("description"));
                                holidayBean.setDate(jsonObject.getString("date"));

                                JSONObject type = jsonObject.getJSONObject("type");
                                holidayBean.setType_id(type.getString("id"));
                                holidayBean.setType_name(type.getString("name"));


                                holidayBeanArrayList.add(holidayBean);
                            }
                            holidayAdapter = new HolidayAdapter(HolidayActivity.this, holidayBeanArrayList);
                            binding.syllabusrecyclerview.setAdapter(holidayAdapter);


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

