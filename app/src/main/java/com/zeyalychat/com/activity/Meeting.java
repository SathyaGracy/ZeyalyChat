package com.zeyalychat.com.activity;

import static android.view.View.GONE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;

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
import com.zeyalychat.com.adpter.MeetingAdapter;
import com.zeyalychat.com.bean.MeetingBean;
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

public class Meeting extends AppCompatActivity implements OnClickListener {
    HomeWorkBinding binding;
    ArrayList<MeetingBean> meetingBeanArrayList;
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
        session = new Session(Meeting.this);
        binding.title.setText(getResources().getText(R.string.Meeting));
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(Meeting.this, LinearLayoutManager.VERTICAL, false));
        //   binding.syllabusrecyclerview.setLayoutManager(new GridLayoutManager(SyllabusDetailActivity.this, 2));
        //     binding.syllabusrecyclerview.setLayoutManager(new GridLayoutManager(HomeWork.this, 2));
        onItemClickListener();

        Section();


    }


    private void onItemClickListener() {
        binding.backArraow.setOnClickListener(this);
        binding.fab.setOnClickListener(this);
        binding.filter.setOnClickListener(this);
        binding.recyclerview.addOnItemTouchListener(new RecyclerTouchListener(Meeting.this, binding.recyclerview,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(Meeting.this, MeetingDetailActivity.class);
                        intent.putExtra("MeetingId",meetingBeanArrayList.get(position).getId());
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
                Intent intent = new Intent(Meeting.this, MeetingCreate.class);
                startActivity(intent);
                break;
        }


    }

    private void DialogSection() {

        Dialog dialog = new Dialog(Meeting.this);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_list);

        TextView Heading = dialog.findViewById(R.id.Heading);
        Heading.setText(getResources().getText(R.string.SelectSection));

        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout searchRL = dialog.findViewById(R.id.searchRL);
        searchRL.setVisibility(View.GONE);
        RecyclerView mainList = dialog.findViewById(R.id.mainList);
        mainList.setLayoutManager(new LinearLayoutManager(Meeting.this, LinearLayoutManager.VERTICAL, false));


        DialogRadioBeanAdapter sectionAdapter = new DialogRadioBeanAdapter(Meeting.this, SectionBeanArrayList);
        mainList.setAdapter(sectionAdapter);


        mainList.addOnItemTouchListener(new RecyclerTouchListener(Meeting.this, mainList,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        SectionId = SectionBeanArrayList.get(position).getSection_id();
                        GetMeeting(SectionId);

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
                        DialogRadioBeanAdapter sectionAdapter = new DialogRadioBeanAdapter(Meeting.this, SectionBeanArrayList);
                        mainList.setAdapter(sectionAdapter);
                        dialog.dismiss();


                    }


                    @Override
                    public void onLongClick(View view, int position) {
                        //dialogAlert(position);

                    }
                }));


        close_layout.setOnClickListener(new OnClickListener() {
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
                            GetMeeting(SectionId);

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

    private void GetMeeting(int SectionId) {
        meetingBeanArrayList = new ArrayList<>();
        String url = "";
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            url = URLHelper.meeting + "?section_id=" + MainActivity.userInfoArrayList.get(0).getSection_id();
        } else {
            url = URLHelper.meeting + "?staff_id=" + MainActivity.userInfoArrayList.get(0).getId() + "&section_id=" + SectionId;
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
                                MeetingBean meetingBean = new MeetingBean();

                                meetingBean.setMeeting_id(jsonObject.getString("meeting_id"));
                                meetingBean.setTitle(jsonObject.getString("title"));
                                meetingBean.setMeeting_url(jsonObject.getString("meeting_url"));
                                meetingBean.setId(jsonObject.getString("id"));
                                if(jsonObject.has("start_time"))
                                meetingBean.setStart_time(jsonObject.getString("start_time"));
                                if(jsonObject.has("end_time"))
                                meetingBean.setEnd_time(jsonObject.getString("end_time"));

                                JSONObject section = jsonObject.getJSONObject("section");
                                meetingBean.setSection_id(section.getString("id"));
                                meetingBean.setSection_name(section.getString("name"));

                                meetingBeanArrayList.add(meetingBean);


                            }


                           // System.out.println("Meeting"+meetingBeanArrayList);
                            MeetingAdapter meetingAdapter = new MeetingAdapter(Meeting.this, meetingBeanArrayList);
                            binding.recyclerview.setAdapter(meetingAdapter);

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

