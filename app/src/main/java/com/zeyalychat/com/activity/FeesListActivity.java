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
import com.zeyalychat.com.adpter.FeeTypeAdapter;
import com.zeyalychat.com.bean.FeesBean;
import com.zeyalychat.com.bean.SectionBean;
import com.zeyalychat.com.databinding.SyllabusLayoutBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeesListActivity extends AppCompatActivity implements View.OnClickListener {
    SyllabusLayoutBinding binding;
    ArrayList<FeesBean> feesBeanArrayList;
    FeeTypeAdapter feeTypeAdapter;

    Session session;
    int SectionId;
    ArrayList<SectionBean> SectionBeanArrayList;

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
        session = new Session(FeesListActivity.this);
        SectionBeanArrayList = new ArrayList<>();
        binding.syllabusrecyclerview.setLayoutManager(new LinearLayoutManager(FeesListActivity.this, LinearLayoutManager.VERTICAL, false));
        //   binding.syllabusrecyclerview.setLayoutManager(new GridLayoutManager(SyllabusDetailActivity.this, 2));
        //     binding.syllabusrecyclerview.setLayoutManager(new GridLayoutManager(FeesListActivity.this, 2));
        onItemClickListener();
        Section();
        binding.title.setText(getText(R.string.fee));
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.filter.setVisibility(View.GONE);
        }else {
            binding.filter.setVisibility(View.VISIBLE);
        }
    }

    private void onItemClickListener() {
        binding.syllabusrecyclerview.addOnItemTouchListener(new RecyclerTouchListener(FeesListActivity.this, binding.syllabusrecyclerview,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent mainIntent = new Intent(FeesListActivity.this, FeeSubListActivity.class);
                        mainIntent.putExtra("fee_id", feesBeanArrayList.get(position).getId());
                        mainIntent.putExtra("total", feesBeanArrayList.get(position).getAmount());
                        startActivity(mainIntent);

                    }


                    @Override
                    public void onLongClick(View view, int position) {
                        //dialogAlert(position);

                    }
                }));
        binding.backArraow.setOnClickListener(this);
        binding.filter.setOnClickListener(this);

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

    private void DialogSection() {

        Dialog dialog = new Dialog(FeesListActivity.this);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_list);

        TextView Heading = dialog.findViewById(R.id.Heading);
        Heading.setText(getResources().getText(R.string.SelectSection));

        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout searchRL = dialog.findViewById(R.id.searchRL);
        searchRL.setVisibility(View.GONE);
        RecyclerView mainList = dialog.findViewById(R.id.mainList);
        mainList.setLayoutManager(new LinearLayoutManager(FeesListActivity.this, LinearLayoutManager.VERTICAL, false));


        DialogRadioBeanAdapter sectionAdapter = new DialogRadioBeanAdapter(FeesListActivity.this, SectionBeanArrayList);
        mainList.setAdapter(sectionAdapter);


        mainList.addOnItemTouchListener(new RecyclerTouchListener(FeesListActivity.this, mainList,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        SectionId = SectionBeanArrayList.get(position).getSection_id();
                        feeList(SectionId);
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
                        DialogRadioBeanAdapter sectionAdapter = new DialogRadioBeanAdapter(FeesListActivity.this, SectionBeanArrayList);
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
                            feeList(SectionId);

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
    private void feeList(int SectionId) {
        feesBeanArrayList = new ArrayList<>();
        String url = "";
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            url = URLHelper.fee;
        } else {
            url = URLHelper.fee+ "?section_id=" +SectionId;
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
                                FeesBean feesBean = new FeesBean();
                                feesBean.setId(jsonObject.getString("id"));
                                feesBean.setAmount(jsonObject.getString("amount"));
                                feesBean.setApproved_by(jsonObject.getString("approved_by"));
                                feesBean.setDescription(jsonObject.getString("description"));
                                feesBean.setDisplay_description(jsonObject.getString("display_description"));
                                feesBean.setIssued_by(jsonObject.getString("issued_by"));
                                feesBean.setIssued_date(jsonObject.getString("issued_date"));
                                feesBean.setLast_date(jsonObject.getString("last_date"));
                                JSONObject type = jsonObject.getJSONObject("type");
                                feesBean.setType_id(type.getString("id"));
                                feesBean.setName(type.getString("name"));


                                feesBeanArrayList.add(feesBean);
                            }
                            feeTypeAdapter = new FeeTypeAdapter(FeesListActivity.this, feesBeanArrayList);
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
}

