package com.zeyalychat.com.activity;

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
import com.zeyalychat.com.adpter.ChatListAdapter;
import com.zeyalychat.com.bean.ChatListBean;
import com.zeyalychat.com.databinding.ChatlistBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.SectoDate;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageListActivity extends AppCompatActivity implements View.OnClickListener {
    ChatlistBinding binding;
    ChatListAdapter chatListAdapter;
    ArrayList<ChatListBean> chatListBeanArrayList;
    SectoDate sectoDate;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.chatlist);
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

    private void intView() {
        binding.actionBarLayout.setVisibility(View.VISIBLE);
        sectoDate = new SectoDate(MessageListActivity.this);
        session = new Session(MessageListActivity.this);
        binding.chatListRecyclerview.setLayoutManager(new LinearLayoutManager(MessageListActivity.this, LinearLayoutManager.VERTICAL, false));
        chatListBeanArrayList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(MessageListActivity.this, chatListBeanArrayList);
        binding.chatListRecyclerview.setAdapter(chatListAdapter);
        onItemClickListener();
        recentMessage();


    }

    private void onItemClickListener() {
        binding.chatListRecyclerview.addOnItemTouchListener(new RecyclerTouchListener(MessageListActivity.this, binding.chatListRecyclerview,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent mainIntent = new Intent(MessageListActivity.this, MessageConversationActivity.class);
                        mainIntent.putExtra("id", chatListBeanArrayList.get(position).getId());
                        mainIntent.putExtra("type", chatListBeanArrayList.get(position).getType());
                        mainIntent.putExtra("from", "message");
                        mainIntent.putExtra("name", chatListBeanArrayList.get(position).getTitle());

                        startActivity(mainIntent);

                    }


                    @Override
                    public void onLongClick(View view, int position) {
                        //dialogAlert(position);

                    }
                }));

        binding.fab.setOnClickListener(this);
        binding.backArraow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent mainIntent = new Intent(MessageListActivity.this, ContactListActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.backArraow:
                finish();
                break;
        }


    }

    private void recentMessage() {

        AndroidNetworking.get(URLHelper.messageList)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("json---messageList-------->" + response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ChatListBean chatListBean = new ChatListBean();
                                chatListBean.setDescription(jsonObject.getString("description"));
                                chatListBean.setId(jsonObject.getString("id"));
                                chatListBean.setImg_url(jsonObject.getString("img_url"));
                                chatListBean.setLast_message(jsonObject.getString("last_message"));
                                chatListBean.setTitle(jsonObject.getString("title"));
                                Double d = Double.parseDouble(jsonObject.getString("updated_date").trim());
                                Long l = d.longValue();
                                System.out.println("updated_date : "+sectoDate.secToDate(l));
                                chatListBean.setUpdated_date(sectoDate.secToDate(l));
                                //String datevalue = sectoDate.secToDate(Long.parseLong(sectodate[0]));
                              //  chatListBean.setUpdated_date(datevalue);
                                chatListBean.setType(jsonObject.getString("type"));
                                chatListBean.setType_id(jsonObject.getString("type_id"));

                                chatListBeanArrayList.add(chatListBean);
                            }
                            chatListAdapter = new ChatListAdapter(MessageListActivity.this, chatListBeanArrayList);
                            binding.chatListRecyclerview.setAdapter(chatListAdapter);


                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                        }

                     /*   try {


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

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
