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
import com.zeyalychat.com.adpter.FeeSubTypeAdapter;
import com.zeyalychat.com.bean.FeeSubBean;
import com.zeyalychat.com.databinding.FeeListBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeeSubListActivity extends AppCompatActivity implements View.OnClickListener {
    FeeListBinding binding;
    ArrayList<FeeSubBean>feeSubBeanArrayList;
    FeeSubTypeAdapter feeTypeAdapter;

    Session session;
    String fee_id="",total="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fee_list);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }
        Intent intent = getIntent();
        fee_id = intent.getStringExtra("fee_id");
        total = intent.getStringExtra("total");
        intView();

    }

    @SuppressLint("RestrictedApi")
    private void intView() {
        session = new Session(FeeSubListActivity.this);
        binding.syllabusrecyclerview.setLayoutManager(new LinearLayoutManager(FeeSubListActivity.this, LinearLayoutManager.VERTICAL, false));
     //   binding.syllabusrecyclerview.setLayoutManager(new GridLayoutManager(SyllabusDetailActivity.this, 2));

        onItemClickListener();
        feeSubList();
        binding.title.setText(getText(R.string.fee));
        binding.total.setText(total);

    }

    private void onItemClickListener() {
        binding.syllabusrecyclerview.addOnItemTouchListener(new RecyclerTouchListener(FeeSubListActivity.this, binding.syllabusrecyclerview,
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
    private void feeSubList() {
        feeSubBeanArrayList=new ArrayList<>();
        AndroidNetworking.get(URLHelper.feeSublist+fee_id+"&action=subfee")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("json---feeSublist-------->" + response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                FeeSubBean feesBean = new FeeSubBean();
                                feesBean.setId(jsonObject.getString("id"));
                                feesBean.setAmount(jsonObject.getString("amount"));

                                feesBean.setDescription(jsonObject.getString("description"));


                                feeSubBeanArrayList.add(feesBean);
                            }
                            feeTypeAdapter = new FeeSubTypeAdapter(FeeSubListActivity.this, feeSubBeanArrayList);
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

