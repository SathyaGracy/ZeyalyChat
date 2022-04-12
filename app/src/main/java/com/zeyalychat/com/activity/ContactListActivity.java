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
import com.zeyalychat.com.adpter.ContactListAdapter;
import com.zeyalychat.com.bean.ContactBean;
import com.zeyalychat.com.databinding.ContactlistBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity implements View.OnClickListener {
    ContactlistBinding binding;
    ContactListAdapter contactListAdapter;
    ArrayList<ContactBean> contactBeanArrayList;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.contactlist);
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
        session = new Session(ContactListActivity.this);
        binding.chatListRecyclerview.setLayoutManager(new LinearLayoutManager(ContactListActivity.this, LinearLayoutManager.VERTICAL, false));
        contactBeanArrayList = new ArrayList<>();
        onItemClickListener();
        contactList();

    }

    private void onItemClickListener() {
        binding.chatListRecyclerview.addOnItemTouchListener(new RecyclerTouchListener(ContactListActivity.this, binding.chatListRecyclerview,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent mainIntent = new Intent(ContactListActivity.this, MessageConversationActivity.class);
                        mainIntent.putExtra("id", contactBeanArrayList.get(position).getId());
                        mainIntent.putExtra("type", contactBeanArrayList.get(position).getType());
                        mainIntent.putExtra("from", "search");
                        mainIntent.putExtra("name", contactBeanArrayList.get(position).getName());

                        startActivity(mainIntent);

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

    private void contactList() {

        AndroidNetworking.get(URLHelper.contactList)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("json---contactList-------->" + response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ContactBean contactBean = new ContactBean();
                                contactBean.setId(jsonObject.getString("id"));
                                contactBean.setName(jsonObject.getString("name"));
                                contactBean.setPicture_url(jsonObject.getString("picture_url"));
                                contactBean.setType(jsonObject.getString("type"));

                                contactBeanArrayList.add(contactBean);
                            }
                            contactListAdapter = new ContactListAdapter(ContactListActivity.this, contactBeanArrayList);
                            binding.chatListRecyclerview.setAdapter(contactListAdapter);


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
}
