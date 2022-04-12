package com.zeyalychat.com.activity;



import static com.zeyalychat.com.Application.trimMessage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;
import com.zeyalychat.com.Application;
import com.zeyalychat.com.R;
import com.zeyalychat.com.adpter.ConversationListAdapter;
import com.zeyalychat.com.bean.ConversationBean;
import com.zeyalychat.com.databinding.ConversationlistBinding;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.SectoDate;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageConversationActivity extends AppCompatActivity implements View.OnClickListener {
    ConversationlistBinding binding;
    ConversationListAdapter conversationListAdapter;
    ArrayList<ConversationBean> conversationBeanArrayList;
    String id, type, from, name = "";
    SectoDate sectoDate;

    Session session;
    String convID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.conversationlist);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        type = intent.getStringExtra("type");
        from = intent.getStringExtra("from");
        name = intent.getStringExtra("name");
        binding.name.setText(name);
        intView();

    }

    private void intView() {
        session = new Session(MessageConversationActivity.this);
        sectoDate = new SectoDate(MessageConversationActivity.this);
        binding.chatListRecyclerview.setLayoutManager(new LinearLayoutManager(MessageConversationActivity.this, LinearLayoutManager.VERTICAL, false));
        conversationBeanArrayList = new ArrayList<>();
        conversationListAdapter = new ConversationListAdapter(MessageConversationActivity.this, conversationBeanArrayList);
        binding.chatListRecyclerview.setAdapter(conversationListAdapter);
        binding.chatListRecyclerview.setHasFixedSize(true);

        if (from.equalsIgnoreCase("search")) {
            getId();

        } else {
            Messages(id);
            System.out.println("id value :" + id);
        }
        onItemClickListener();
    }

    private void onItemClickListener() {
        binding.backArraow.setOnClickListener(this);
        binding.sendButtonLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.backArraow:
                finish();
                break;
            case R.id.sendButtonLayout:
                if (!binding.contentEdt.getText().toString().equalsIgnoreCase("")) {
                    if (from.equalsIgnoreCase("search")) {
                        System.out.println("id value : convID" + convID);
                        MessagesPost(convID);
                    } else {
                        MessagesPost(id);
                        System.out.println("id value : :" + id);
                    }
                }

                break;
        }

    }

    private void getId() {
        final JsonObject object = new JsonObject();

        AndroidNetworking.post(URLHelper.messageConvFromSearch + "id=" + id + "&type=" + type)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .addBodyParameter(object)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("json---messageConvFromSearch-------->" + response.toString());

                        try {
                            convID = response.getString("id");
                            Messages(response.getString("id"));
                           /* for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ChatListBean chatListBean = new ChatListBean();
                                chatListBean.setDescription(jsonObject.getString("description"));
                                chatListBean.setId(jsonObject.getString("id"));
                                chatListBean.setImg_url(jsonObject.getString("img_url"));
                                chatListBean.setLast_message(jsonObject.getString("last_message"));
                                chatListBean.setTitle(jsonObject.getString("title"));
                                chatListBean.setUpdated_date(jsonObject.getString("updated_date"));

                                chatListBeanArrayList.add(chatListBean);
                            }
                            chatListAdapter = new ChatListAdapter(MessageListActivity.this, chatListBeanArrayList);
                            binding.chatListRecyclerview.setAdapter(chatListAdapter);*/


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

    private void Messages(String convId) {
        AndroidNetworking.get(URLHelper.conv + convId + "/conversation")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("json---messageConvFromSearch-------->" + response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ConversationBean conversationBean = new ConversationBean();
                                conversationBean.setContent(jsonObject.getString("content"));
                                conversationBean.setId(jsonObject.getString("id"));

                                Double d = Double.parseDouble(jsonObject.getString("created_time").trim());
                                Long l = d.longValue();
                                System.out.println("created_time : "+sectoDate.sectoTime(l));
                                conversationBean.setCreated_time(sectoDate.sectoTime(l));
                                conversationBean.setHas_attachments(jsonObject.getString("has_attachments"));
                                conversationBean.setIs_sender(jsonObject.getString("is_sender"));
                                conversationBean.setMessage_id(jsonObject.getString("message_id"));
                                conversationBean.setThread_id(jsonObject.getString("thread_id"));
                                JSONObject obj = jsonObject.getJSONObject("user");
                                conversationBean.setUserid(obj.getString("id"));
                                conversationBean.setName(obj.getString("name"));
                                conversationBean.setProfile_image_url(obj.getString("profile_image_url"));
                                conversationBeanArrayList.add(conversationBean);
                            }
                            conversationListAdapter = new ConversationListAdapter(MessageConversationActivity.this, conversationBeanArrayList);
                            binding.chatListRecyclerview.setAdapter(conversationListAdapter);
                            binding.chatListRecyclerview.getLayoutManager().scrollToPosition(conversationListAdapter.getItemCount() - 1);

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

    /*   private void MessagesPost(String convId) {
           JSONObject manJson = new JSONObject();
           try {
               manJson.put("content", binding.contentEdt.getText().toString());
               ConversationBean conversationBean = new ConversationBean();
               conversationBean.setContent(binding.contentEdt.getText().toString());
               conversationBean.setIs_sender("true");
               //  conversationBean.setCreated_time(jsonObject.getString("created_time"));
               conversationBeanArrayList.add(conversationBean);

               conversationListAdapter.notifyDataSetChanged();
               binding.chatListRecyclerview.smoothScrollToPosition(conversationBeanArrayList.size());
               binding.contentEdt.setText("");

           } catch (JSONException e) {
               e.printStackTrace();
           }
           AndroidNetworking.post(URLHelper.conv + convId + "/conversation")
                   .addHeaders("Content-Type", "application/json")
                   .addHeaders("Authorization", session.getKEYAuth())
                   .addBodyParameter("content",binding.contentEdt.getText().toString())
                   .setPriority(Priority.MEDIUM)
                   .build()
                   .getAsJSONObject(new JSONObjectRequestListener() {
                       @Override
                       public void onResponse(JSONObject response) {
                           System.out.println("json---content-------->" + response.toString());

                        *//*   try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ConversationBean conversationBean = new ConversationBean();
                                conversationBean.setContent(jsonObject.getString("content"));
                                conversationBean.setId(jsonObject.getString("id"));
                                conversationBean.setCreated_time(jsonObject.getString("created_time"));
                                conversationBean.setHas_attachments(jsonObject.getString("has_attachments"));
                                conversationBean.setIs_sender(jsonObject.getString("is_sender"));
                                conversationBean.setMessage_id(jsonObject.getString("message_id"));
                                conversationBean.setThread_id(jsonObject.getString("thread_id"));
                                JSONObject obj = jsonObject.getJSONObject("user");
                                conversationBean.setUserid(obj.getString("id"));
                                conversationBean.setUserid(obj.getString("name"));
                                conversationBean.setUserid(obj.getString("profile_image_url"));
                                conversationBeanArrayList.add(conversationBean);
                            }
                            conversationListAdapter = new ConversationListAdapter(MessageConversationActivity.this, conversationBeanArrayList);
                            binding.chatListRecyclerview.setAdapter(conversationListAdapter);


                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                        }*//*
     *//*   try {


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*//*

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
    public void MessagesPost(String convId) {
        JSONObject manJson = new JSONObject();
        try {
            manJson.put("content", binding.contentEdt.getText().toString());
           /* ConversationBean conversationBean = new ConversationBean();
            conversationBean.setContent(binding.contentEdt.getText().toString());
            conversationBean.setIs_sender("true");
            //  conversationBean.setCreated_time(jsonObject.getString("created_time"));
            conversationBeanArrayList.add(conversationBean);

            conversationListAdapter.notifyDataSetChanged();
            binding.chatListRecyclerview.smoothScrollToPosition(conversationBeanArrayList.size());*/


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.conv + convId + "/conversation",
                manJson, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    ConversationBean conversationBean = new ConversationBean();
                    conversationBean.setContent(response.getString("content"));
                    conversationBean.setId(response.getString("id"));
                    Double d = Double.parseDouble(response.getString("created_time").trim());
                    Long l = d.longValue();
                    System.out.println("created_time : "+sectoDate.sectoTime(l));
                    conversationBean.setCreated_time(sectoDate.sectoTime(l));
                    conversationBean.setHas_attachments(response.getString("has_attachments"));
                    conversationBean.setIs_sender(response.getString("is_sender"));
                    conversationBean.setMessage_id(response.getString("message_id"));
                    conversationBean.setThread_id(response.getString("thread_id"));
                    JSONObject obj = response.getJSONObject("user");
                    conversationBean.setUserid(obj.getString("id"));
                    conversationBean.setName(obj.getString("name"));
                    conversationBean.setProfile_image_url(obj.getString("profile_image_url"));
                    conversationBeanArrayList.add(conversationBean);
                    adapterScroll();
                    //  binding.chatListRecyclerview.smoothScrollToPosition(conversationBeanArrayList.size());
                    binding.contentEdt.setText("");
                    // binding.chatListRecyclerview.smoothScrollToPosition(conversationBeanArrayList.size()-1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                System.out.println("response" + response.toString());


                // String responses = response.toString();
                //  processResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {

                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));

                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                            try {
                                Toast.makeText(MessageConversationActivity.this, errorObj.optString("message"), Toast.LENGTH_SHORT).show();
                                //displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                // displayMessage(getString(R.string.something_went_wrong));
                            }
                        } else if (response.statusCode == 401) {
                            Toast.makeText(MessageConversationActivity.this, errorObj.optString("message"), Toast.LENGTH_SHORT).show();
                            // displayMessage(errorObj.optString("message"));
                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));
                            if (json != "" && json != null) {
                                // displayMessage(json);
                            } else {
                                //  displayMessage(getString(R.string.please_try_again));
                            }
                        } else if (response.statusCode == 503) {
                            // displayMessage(getString(R.string.server_down));
                        } else {
                            //    displayMessage(getString(R.string.please_try_again));
                        }

                    } catch (Exception e) {

                        //displayMessage(getString(R.string.something_went_wrong));
                    }

                } else {

                    if (error instanceof NoConnectionError) {
                        //displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        //    displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        //  displayMessage(getString(R.string.timed_out));
                    }
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", session.getKEYAuth());
                return headers;
            }
        };

        Application.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private void adapterScroll() {
        conversationListAdapter.notifyDataSetChanged();
        binding.chatListRecyclerview.smoothScrollToPosition(binding.chatListRecyclerview.getAdapter().getItemCount() - 1);

    }
  /*  public void scrollToBottom(){
        binding..scrollVerticallyTo(0);
    }*/
}

