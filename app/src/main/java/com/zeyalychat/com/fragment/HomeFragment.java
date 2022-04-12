package com.zeyalychat.com.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;
import com.zeyalychat.com.R;
import com.zeyalychat.com.activity.MainActivity;
import com.zeyalychat.com.adpter.CardRowAdapter;
import com.zeyalychat.com.adpter.TimeTableAdapter;
import com.zeyalychat.com.bean.HomeWorkBean;
import com.zeyalychat.com.bean.TimeTableBean;
import com.zeyalychat.com.databinding.HomeFrBinding;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment implements View.OnClickListener, DiscreteScrollView.OnItemChangedListener {
    HomeFrBinding binding;

    ArrayList<TimeTableBean> timeTableBeanArrayList;
    ArrayList<HomeWorkBean> homeWorkBeanArrayList;
    int appDay = 0;
    Session session;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //   View view = inflater.inflate(R.layout.fragment_bronze, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fr, container, false);


        initView();
        // getActivity().registerReceiver(broadcastReceiver, new IntentFilter("INTERNET_LOST"));

        return binding.getRoot();
    }

    private void initView() {
        session = new Session(getActivity());
        homeWorkBeanArrayList = new ArrayList<>();
        timeTableBeanArrayList = new ArrayList<>();
        binding.recFrame.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.timeRe.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        // binding.recFrame.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.dateLayout.setOnClickListener(this);
        binding.chatLayout.setOnClickListener(this);
        binding.locationLayout.setOnClickListener(this);
        binding.profileLayout.setOnClickListener(this);
        binding.timeLayout.setOnClickListener(this);

        binding.itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        binding.itemPicker.addOnItemChangedListener(this);
        binding.itemPicker.setItemTransitionTimeMillis(100);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.itemPicker);
        binding.itemPicker.setOverScrollEnabled(false);
        binding.itemPicker.smoothScrollToPosition(binding.itemPicker.getCurrentItem());
        binding.itemPicker.setOffscreenItems(2);
        binding.itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        Date myDate = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
        String dayName = simpleDateFormat.format(myDate);
        // binding.title.setText(simpleDateFormat2.format(myDate) + " /" + dayName);
        getDateNumber(dayName);
        //timeTableList(0);
      //    GetCard(0);

       /* RelativeLayout view = getActivity().findViewById(R.id.lt_r_actionbarlayout);
        view.setVisibility(View.VISIBLE);*/


    }



    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

    private void getDateNumber(String datesvalue) {
        switch (datesvalue) {
            case "Monday":
                appDay = 1;
                break;
            case "Tuesday":
                appDay = 2;
                break;
            case "Wednesday":
                appDay = 3;
                break;
            case "Thursday":
                appDay = 4;
                break;
            case "Friday":
                appDay = 5;
                break;
            case "Saturday":
                appDay = 6;
                break;
            case "Sunday":
                appDay = 0;
                break;
            default:
                appDay = 7;
                break;
        }

    }

    public void timeTableList(int position) {

        timeTableBeanArrayList = new ArrayList<>();
        String url = "";
        if (MainActivity.userInfoArrayList.get(position).getRole_name().equalsIgnoreCase("Students")) {
            url = URLHelper.timeTable + "day=" + 2 + "&section_id=" + MainActivity.userInfoArrayList.get(position).getSection_id();
        } else {
            url = URLHelper.timeTable + "day=" + appDay + "&staff_id=" + MainActivity.userInfoArrayList.get(position).getId();
        }
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("json---timeTableList-------->" + response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONObject section = jsonObject.getJSONObject("section");
                                JSONObject staff = jsonObject.getJSONObject("staff");
                                JSONObject subject = jsonObject.getJSONObject("subject");
                                TimeTableBean timeTableBean = new TimeTableBean();
                                timeTableBean.setFrom_time(jsonObject.getString("from_time"));
                                timeTableBean.setId(jsonObject.getString("id"));
                                timeTableBean.setTo_time(jsonObject.getString("to_time"));

                                timeTableBean.setSection_id(section.getString("id"));
                                timeTableBean.setSection_name(section.getString("name"));

                                timeTableBean.setStaff_id(staff.getString("id"));
                                timeTableBean.setStaff_name(staff.getString("name"));

                                timeTableBean.setSubject_id(subject.getString("id"));
                                timeTableBean.setSubject_name(subject.getString("name"));
                                timeTableBean.setSubject_description(subject.getString("description"));

                                timeTableBeanArrayList.add(timeTableBean);
                            }
                            TimeTableAdapter timeTableAdapter = new TimeTableAdapter(getActivity(), timeTableBeanArrayList);
                            binding.timeRe.setAdapter(timeTableAdapter);


                        } catch (
                                JSONException e) {
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

    public void GetCard(int position) {
        // ((MainActivity)(getActivity())).navList();
        // navList();
        homeWorkBeanArrayList = new ArrayList<>();
        String url = "";
        if (MainActivity.userInfoArrayList.get(position).getRole_name().equalsIgnoreCase("Students")) {
            url = URLHelper.homwwork + "?section_id=" + MainActivity.userInfoArrayList.get(position).getSection_id();
        } else {
            url = URLHelper.homwwork + "?staff_id=" + MainActivity.userInfoArrayList.get(position).getId();
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
                            }

                            CardRowAdapter timeTableAdapter = new CardRowAdapter(getActivity(), homeWorkBeanArrayList);
                            binding.recFrame.setAdapter(timeTableAdapter);

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


    @Override
    public void onClick(View v) {

    }
}
