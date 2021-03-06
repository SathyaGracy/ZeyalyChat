package com.zeyalychat.com.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.zeyalychat.com.R;
import com.zeyalychat.com.adpter.TimeLineAdapter;
import com.zeyalychat.com.bean.TimeLineBean;
import com.zeyalychat.com.databinding.TimeLineBinding;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.SectoDate;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimeLineListFragment extends Fragment implements View.OnClickListener, TimeLineAdapter.OnItemClickListener {
    TimeLineBinding binding;
    TimeLineAdapter timeLineAdapter;
    ArrayList<TimeLineBean> timeLineBeanArrayList;
    Session session;
    SectoDate sectoDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //   View view = inflater.inflate(R.layout.fragment_bronze, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.time_line, container, false);


        intView();
        // getActivity().registerReceiver(broadcastReceiver, new IntentFilter("INTERNET_LOST"));

        return binding.getRoot();
    }

    @SuppressLint("RestrictedApi")
    private void intView() {
        session = new Session(getActivity());
        sectoDate = new SectoDate(getActivity());
        binding.timelinerecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        timeLineBeanArrayList = new ArrayList<>();
        onItemClickListener();
        timeLineList();


    }

    private void onItemClickListener() {
       /* binding.chatListRecyclerview.addOnItemTouchListener(new RecyclerTouchListener(TimeLineListActivity.this, binding.chatListRecyclerview,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent mainIntent = new Intent(TimeLineListActivity.this, MessageConversationActivity.class);
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
                }));*/
        binding.backArraow.setVisibility(View.GONE);
        binding.postLayout.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArraow:

                break;
            case R.id.post_layout:
                CreatePost();
                break;
        }


    }

    private void CreatePost() {

        JSONObject data = new JSONObject();
        try {
            data.put("content", binding.postEdt.getText().toString());
            data.put("type", 1);
            data.put("is_draft", false);
            data.put("is_pinned", false);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        AndroidNetworking.upload(URLHelper.timeline)
                /*    .addMultipartFile("img",imgFile)*/
                .addMultipartParameter("data", data.toString())
                .addHeaders("Content-Type", "multipart/form-data")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i("onResponse: ", jsonObject.toString());
                        binding.postEdt.setText("");


                        try {
                            JSONObject type = jsonObject.getJSONObject("type");
                            JSONObject created_by = jsonObject.getJSONObject("created_by");
                            TimeLineBean timeLineBean = new TimeLineBean();
                            timeLineBean.setId(jsonObject.getString("id"));
                            timeLineBean.setContent(jsonObject.getString("content"));

                            Double d = Double.parseDouble(jsonObject.getString("created_date").trim());
                            Long l = d.longValue();
                            System.out.println("updated_date : " + sectoDate.secToDate(l));
                            timeLineBean.setCreated_date(sectoDate.secToDate(l));


                            //   timeLineBean.setCreated_date(jsonObject.getString("created_date"));
                            timeLineBean.setGroup(jsonObject.getString("group"));
                            timeLineBean.setGroup_type(jsonObject.getString("group_type"));
                            timeLineBean.setIs_draft(jsonObject.getString("is_draft"));
                            timeLineBean.setIs_pinned(jsonObject.getString("is_pinned"));
                            timeLineBean.setLink(jsonObject.getString("link"));
                            timeLineBean.setType_id(type.getString("id"));
                            timeLineBean.setType_name(type.getString("name"));
                            timeLineBean.setCreated_by_id(created_by.getString("id"));
                            timeLineBean.setCreated_by_name(created_by.getString("name"));
                            timeLineBean.setComments_count(jsonObject.getInt("comments_count"));
                            timeLineBean.setLikes_count(jsonObject.getInt("likes_count"));
                            timeLineBean.setIslike(false);
                            timeLineBean.setProfile_image_url(created_by.getString("profile_image_url"));

                            timeLineBeanArrayList.add(timeLineBean);
                            timeLineAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError error) {
                        Log.i("**** ",
                                "\n ErrorDetail " + error.getErrorDetail()
                                        + "\n ErrorResponse " + error.getResponse());
                        if (error.getErrorCode() == 403) {
                            session.onDestroy();


                        }

                    }
                });
    }


    public void CreateComment(String comment, String id, int pos) {

        JSONObject data = new JSONObject();
        try {
            data.put("content", comment);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        AndroidNetworking.upload(URLHelper.comments + id)
                /*    .addMultipartFile("img",imgFile)*/
                .addMultipartParameter("data", data.toString())
                .addHeaders("Content-Type", "multipart/form-data")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i("onResponse: ", jsonObject.toString());
                        //binding.postEdt.setText("");
                        timeLineBeanArrayList.get(pos).setComments_count(timeLineBeanArrayList.get(pos).getComments_count() + 1);

                        AdapterCall(timeLineBeanArrayList);
                      /*  try {
                            JSONObject type = jsonObject.getJSONObject("type");
                            JSONObject created_by = jsonObject.getJSONObject("created_by");
                            TimeLineBean timeLineBean = new TimeLineBean();
                            timeLineBean.setId(jsonObject.getString("id"));
                            timeLineBean.setContent(jsonObject.getString("content"));

                            Double d = Double.parseDouble(jsonObject.getString("created_date").trim());
                            Long l = d.longValue();
                            System.out.println("updated_date : " + sectoDate.secToDate(l));
                            timeLineBean.setCreated_date(sectoDate.secToDate(l));


                            //   timeLineBean.setCreated_date(jsonObject.getString("created_date"));
                            timeLineBean.setGroup(jsonObject.getString("group"));
                            timeLineBean.setGroup_type(jsonObject.getString("group_type"));
                            timeLineBean.setIs_draft(jsonObject.getString("is_draft"));
                            timeLineBean.setIs_pinned(jsonObject.getString("is_pinned"));
                            timeLineBean.setLink(jsonObject.getString("link"));
                            timeLineBean.setType_id(type.getString("id"));
                            timeLineBean.setType_name(type.getString("name"));
                            timeLineBean.setCreated_by_id(created_by.getString("id"));
                            timeLineBean.setCreated_by_name(created_by.getString("name"));
                            timeLineBean.setComments_count(jsonObject.getString("comments_count"));
                            timeLineBean.setLikes_count(jsonObject.getString("likes_count"));
                            timeLineBean.setProfile_image_url(created_by.getString("profile_image_url"));

                            timeLineBeanArrayList.add(timeLineBean);
                            timeLineAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

*/
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.i("**** ",
                                "\n ErrorDetail " + error.getErrorDetail()
                                        + "\n ErrorResponse " + error.getResponse());
                        if (error.getErrorCode() == 403) {
                            session.onDestroy();


                        }

                    }
                });

    }
private void AdapterCall(ArrayList<TimeLineBean> timeLineBeanArrayList){
    timeLineAdapter = new TimeLineAdapter(getActivity(), timeLineBeanArrayList,this);
    binding.timelinerecyclerview.setAdapter(timeLineAdapter);
}

    @Override
    public void postComment(String comment, String id, int pos) {
       CreateComment(comment,id,pos);
    }

    @Override
    public void unlike(String id, int pos) {
        unike(id, pos);
    }

    @Override
    public void CreateLike(String id, int pos) {
        Like(id, pos);
    }

    public void Like(String id, int pos) {
        AndroidNetworking.post(URLHelper.like + id + "&type=1")
                /*    .addMultipartFile("img",imgFile)*/

                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i("onResponse: ", jsonObject.toString());
                        //binding.postEdt.setText("");
                        timeLineBeanArrayList.get(pos).setIslike(true);
                        timeLineBeanArrayList.get(pos).setLikes_count(timeLineBeanArrayList.get(pos).getLikes_count() + 1);
                        AdapterCall(timeLineBeanArrayList);


                      /*  try {
                            JSONObject type = jsonObject.getJSONObject("type");
                            JSONObject created_by = jsonObject.getJSONObject("created_by");
                            TimeLineBean timeLineBean = new TimeLineBean();
                            timeLineBean.setId(jsonObject.getString("id"));
                            timeLineBean.setContent(jsonObject.getString("content"));

                            Double d = Double.parseDouble(jsonObject.getString("created_date").trim());
                            Long l = d.longValue();
                            System.out.println("updated_date : " + sectoDate.secToDate(l));
                            timeLineBean.setCreated_date(sectoDate.secToDate(l));


                            //   timeLineBean.setCreated_date(jsonObject.getString("created_date"));
                            timeLineBean.setGroup(jsonObject.getString("group"));
                            timeLineBean.setGroup_type(jsonObject.getString("group_type"));
                            timeLineBean.setIs_draft(jsonObject.getString("is_draft"));
                            timeLineBean.setIs_pinned(jsonObject.getString("is_pinned"));
                            timeLineBean.setLink(jsonObject.getString("link"));
                            timeLineBean.setType_id(type.getString("id"));
                            timeLineBean.setType_name(type.getString("name"));
                            timeLineBean.setCreated_by_id(created_by.getString("id"));
                            timeLineBean.setCreated_by_name(created_by.getString("name"));
                            timeLineBean.setComments_count(jsonObject.getString("comments_count"));
                            timeLineBean.setLikes_count(jsonObject.getString("likes_count"));
                            timeLineBean.setProfile_image_url(created_by.getString("profile_image_url"));

                            timeLineBeanArrayList.add(timeLineBean);
                            timeLineAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

*/
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.i("**** ",
                                "\n ErrorDetail " + error.getErrorDetail()
                                        + "\n ErrorResponse " + error.getResponse());
                        if (error.getErrorCode() == 403) {
                            session.onDestroy();


                        }

                    }
                });
    }

    public void unike(String id, int pos) {
        AndroidNetworking.delete(URLHelper.like + id + "&type=1")
                /*    .addMultipartFile("img",imgFile)*/
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i("onResponse: ", jsonObject.toString());
                        //binding.postEdt.setText("");

                        timeLineBeanArrayList.get(pos).setIslike(false);
                        timeLineBeanArrayList.get(pos).setLikes_count(timeLineBeanArrayList.get(pos).getLikes_count() - 1);
                        AdapterCall(timeLineBeanArrayList);
                      /*  try {
                            JSONObject type = jsonObject.getJSONObject("type");
                            JSONObject created_by = jsonObject.getJSONObject("created_by");
                            TimeLineBean timeLineBean = new TimeLineBean();
                            timeLineBean.setId(jsonObject.getString("id"));
                            timeLineBean.setContent(jsonObject.getString("content"));

                            Double d = Double.parseDouble(jsonObject.getString("created_date").trim());
                            Long l = d.longValue();
                            System.out.println("updated_date : " + sectoDate.secToDate(l));
                            timeLineBean.setCreated_date(sectoDate.secToDate(l));


                            //   timeLineBean.setCreated_date(jsonObject.getString("created_date"));
                            timeLineBean.setGroup(jsonObject.getString("group"));
                            timeLineBean.setGroup_type(jsonObject.getString("group_type"));
                            timeLineBean.setIs_draft(jsonObject.getString("is_draft"));
                            timeLineBean.setIs_pinned(jsonObject.getString("is_pinned"));
                            timeLineBean.setLink(jsonObject.getString("link"));
                            timeLineBean.setType_id(type.getString("id"));
                            timeLineBean.setType_name(type.getString("name"));
                            timeLineBean.setCreated_by_id(created_by.getString("id"));
                            timeLineBean.setCreated_by_name(created_by.getString("name"));
                            timeLineBean.setComments_count(jsonObject.getString("comments_count"));
                            timeLineBean.setLikes_count(jsonObject.getString("likes_count"));
                            timeLineBean.setProfile_image_url(created_by.getString("profile_image_url"));

                            timeLineBeanArrayList.add(timeLineBean);
                            timeLineAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

*/
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.i("**** ",
                                "\n ErrorDetail " + error.getErrorDetail()
                                        + "\n ErrorResponse " + error.getResponse());
                        if (error.getErrorCode() == 403) {
                            session.onDestroy();


                        }

                    }
                });
    }


    private void timeLineList() {

        AndroidNetworking.get(URLHelper.timeline)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("json---timelineList-------->" + response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONObject type = jsonObject.getJSONObject("type");
                                JSONObject created_by = jsonObject.getJSONObject("created_by");
                                TimeLineBean timeLineBean = new TimeLineBean();
                                timeLineBean.setId(jsonObject.getString("id"));
                                timeLineBean.setContent(jsonObject.getString("content"));

                                Double d = Double.parseDouble(jsonObject.getString("created_date").trim());
                                Long l = d.longValue();
                                System.out.println("updated_date : " + sectoDate.secToDate(l));
                                timeLineBean.setCreated_date(sectoDate.secToDate(l));


                                //   timeLineBean.setCreated_date(jsonObject.getString("created_date"));
                                timeLineBean.setGroup(jsonObject.getString("group"));
                                timeLineBean.setGroup_type(jsonObject.getString("group_type"));
                                timeLineBean.setIs_draft(jsonObject.getString("is_draft"));
                                timeLineBean.setIs_pinned(jsonObject.getString("is_pinned"));
                                timeLineBean.setLink(jsonObject.getString("link"));
                                timeLineBean.setType_id(type.getString("id"));
                                timeLineBean.setType_name(type.getString("name"));
                                timeLineBean.setCreated_by_id(created_by.getString("id"));
                                timeLineBean.setCreated_by_name(created_by.getString("name"));
                                timeLineBean.setProfile_image_url(created_by.getString("profile_image_url"));
                                timeLineBean.setComments_count(jsonObject.getInt("comments_count"));
                                timeLineBean.setLikes_count(jsonObject.getInt("likes_count"));
                                timeLineBean.setIslike(false);
                                timeLineBeanArrayList.add(timeLineBean);
                            }
                            AdapterCall(timeLineBeanArrayList);


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
