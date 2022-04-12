package com.zeyalychat.com.activity;

import static com.zeyalychat.com.Application.trimMessage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.zeyalychat.com.Application;
import com.zeyalychat.com.R;
import com.zeyalychat.com.adpter.DialogAdapter;
import com.zeyalychat.com.adpter.SectionAdapter;
import com.zeyalychat.com.bean.SectionBean;
import com.zeyalychat.com.bean.StringBean;
import com.zeyalychat.com.databinding.CreateMeetingBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MeetingCreate extends AppCompatActivity implements View.OnClickListener,TimePickerDialog.OnTimeSetListener {
    CreateMeetingBinding binding;

    ArrayList<SectionBean> SectionBeanArrayList;
    ArrayList<StringBean> StudentList;
    RelativeLayout fileUpload;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    Bitmap bitmap = null;
    int SectionId;
    boolean sectionSelected = false;
    int SyllabusId;
    int ExamType;
    String SessionId;
    ArrayList<StringBean> syllabusBeanArrayList;
    ArrayList<StringBean> ExamTypeBeanArrayList;
    String CompletionDate = "";
    private static final int RESULT_LOAD_IMAGE = 104;
    Session session;
    private static final String LOG_TAG = "Text API";
    Boolean FromTime = true;
    String startTime = "", EndTime = "";
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.create_meeting);
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
        session = new Session(MeetingCreate.this);
        // binding.recyclerview.setLayoutManager(new LinearLayoutManager(MeetingCreate.this, LinearLayoutManager.VERTICAL, false));
        onItemClickListener();
        Subject();
        Section();
        ExamType();


    }

    private void onItemClickListener() {
        binding.backArraow.setOnClickListener(this);
        binding.submit.setOnClickListener(this);
        binding.TimeFromRLayout.setOnClickListener(this);
        binding.TimeToRLayout.setOnClickListener(this);
       // binding.SubjectLayout.setOnClickListener(this);
      //  binding.TypeRLayout.setOnClickListener(this);
        binding.TypeLayout1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArraow:
                finish();
                break;
          /*  case R.id.sectionRLayout:
                Dialog("Section", 1);
                break;*/
            case R.id.TypeLayout1:

                Dialog("Section", 0);
                break;

            case R.id.TimeFromRLayout:
                FromTime = true;
                setTimerPicker();
                break;
            case R.id.TimeToRLayout:
                FromTime = false;
                setTimerPicker();
                break;
            case R.id.submit:
               // if (file != null)
                    CreateMeeting();
                break;

           /* case R.id.SubjectLayout:
                //  if (sectionSelected)
                Dialog("subject", 1);
                break;*/
            case R.id.TypeRLayout:
                Dialog("Type", 2);
                break;
        }


    }





    private void setTimerPicker() {
        Calendar now = Calendar.getInstance();

        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );


        tpd.enableSeconds(true);
        tpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }



    private void Dialog(String heading, int val) {
        final Dialog dialog = new Dialog(MeetingCreate.this);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_list);

        TextView Heading = dialog.findViewById(R.id.Heading);
        Heading.setText(heading);

        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout searchRL = dialog.findViewById(R.id.searchRL);
        searchRL.setVisibility(View.GONE);
        RecyclerView mainList = dialog.findViewById(R.id.mainList);
        mainList.setLayoutManager(new LinearLayoutManager(MeetingCreate.this, LinearLayoutManager.VERTICAL, false));


        if (val == 0) {
            SectionAdapter sectionAdapter = new SectionAdapter(MeetingCreate.this, SectionBeanArrayList);
            mainList.setAdapter(sectionAdapter);
        }/* else if (val == 1) {
            DialogAdapter dialogAdapter = new DialogAdapter(MeetingCreate.this, syllabusBeanArrayList);
            mainList.setAdapter(dialogAdapter);
        }*/ else if (val == 2) {
            DialogAdapter ExamType = new DialogAdapter(MeetingCreate.this, ExamTypeBeanArrayList);
            mainList.setAdapter(ExamType);
        }

        mainList.addOnItemTouchListener(new RecyclerTouchListener(MeetingCreate.this, mainList,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if (val == 0) {
                            binding.SelectTypeTxt.setText(SectionBeanArrayList.get(position).getGrade_name() + "" + SectionBeanArrayList.get(position).getSection_name());
                            SectionId = SectionBeanArrayList.get(position).getGrade_id();
                            sectionSelected = true;
                            //syllabusList(SectionId);

                        } /*else if (val == 1) {

                            binding.SelectSubjectTxt.setText(syllabusBeanArrayList.get(position).getText());
                            SyllabusId = syllabusBeanArrayList.get(position).getId();

                        }*/ else if (val == 2) {
                            binding.typeTxt.setText(ExamTypeBeanArrayList.get(position).getText());
                            ExamType = ExamTypeBeanArrayList.get(position).getId();

                        }
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
                                SectionBeanArrayList.add(sectionBean);

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


    public void CreateMeeting() {
        JSONObject object = jsonMake();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.MeetingCreate,
                object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println("response" + response.toString());
                finish();
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
                                Toast.makeText(MeetingCreate.this, errorObj.optString("message"), Toast.LENGTH_SHORT).show();
                                //displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                // displayMessage(getString(R.string.something_went_wrong));
                            }
                        } else if (response.statusCode == 401) {
                            Toast.makeText(MeetingCreate.this, errorObj.optString("message"), Toast.LENGTH_SHORT).show();
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

    /*private void CreateMeeting() {
        JSONObject object=jsonMake();
        AndroidNetworking.post(URLHelper.meeting)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .addBodyParameter(object)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                        System.out.println("json-----meeting------>" + response.toString());
                       *//* try {

                            //  AccessValue();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*//*
                        // do anything with response

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(MeetingCreate.this, "Error!", Toast.LENGTH_SHORT).show();
                        System.out.println("error value" + error.getErrorBody());
                        System.out.println("error value" + error.getErrorCode());
                    }
                });
    }*/

    private JSONObject jsonMake() {

        JSONObject form = new JSONObject();


        try {

            form.put("section_id", SectionId);
            form.put("meeting_url", binding.MeetingUrlTxt.getText().toString());
            form.put("passcode", binding.passcodeTxt.getText().toString());
            form.put("title", binding.title.getText().toString());


           // form.put("exam_date", CompletionDate);


            form.put("start_time", startTime);
            form.put("end_time", EndTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("form json" + form.toString());
        return form;
    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String am_pm = "";
        Calendar datetime = Calendar.getInstance();

        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";

        //  ((Button)getActivity().findViewById(R.id.btnEventStartTime)).setText( strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm );
        if (FromTime) {
            startTime = hourOfDay + ":" + minute;
            binding.TimeFromTxt.setText(strHrsToShow + ":" + datetime.get(Calendar.MINUTE) + " " + am_pm);
        } else {
            EndTime = hourOfDay + ":" + minute;

            binding.TimeTxt.setText(strHrsToShow + ":" + datetime.get(Calendar.MINUTE) + " " + am_pm);
        }
    }

    private void syllabusList(int gradeId) {
        syllabusBeanArrayList = new ArrayList<>();
        String url = "";
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            url = URLHelper.syllabuslist + "?grade_id=" + MainActivity.userInfoArrayList.get(0).getGrade_id();
        } else {
            url = URLHelper.syllabuslist + "?staff_id=" + MainActivity.userInfoArrayList.get(0).getId() + "&grade_id=" + gradeId;
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
                                StringBean syllabusBean = new StringBean();
                                syllabusBean.setId(jsonObject.getInt("id"));
                                // syllabusBean.setText(jsonObject.getString("name"));
                                //syllabusBean.setGrade(jsonObject.getString("grade"));
                                syllabusBean.setText(jsonObject.getString("name"));
                                syllabusBean.setCode(jsonObject.getString("code"));

                                syllabusBeanArrayList.add(syllabusBean);
                            }
/*
                            syllabusAdapter = new SyllabusAdapter(SyllabusActivity.this, syllabusBeanArrayList);
                            binding.syllabusrecyclerview.setAdapter(syllabusAdapter);
                            if (jsonArray.length() > 0) {
                                binding.noDataLayout.setVisibility(View.GONE);
                            } else {
                                binding.noDataLayout.setVisibility(View.VISIBLE);
                            }*/


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
    private void Subject() {
        syllabusBeanArrayList = new ArrayList<>();

        AndroidNetworking.get(URLHelper.subject)
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

                                StringBean stringBean = new StringBean();
                                stringBean.setId(jsonObject.getInt("id"));
                                stringBean.setText(jsonObject.getString("name"));
                                syllabusBeanArrayList.add(stringBean);
                            }
                           /* DialogAdapter dialogAdapter = new HomeWorkAdapter(HomeWorkCreate.this, SessionArrayList);
                            binding.recyclerview.setAdapter(homeWorkAdapter);*/


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
    private void ExamType() {
        ExamTypeBeanArrayList = new ArrayList<>();
        AndroidNetworking.get(URLHelper.examType)
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
                                StringBean syllabusBean = new StringBean();
                                syllabusBean.setId(jsonObject.getInt("id"));
                                // syllabusBean.setText(jsonObject.getString("name"));
                                //syllabusBean.setGrade(jsonObject.getString("grade"));
                                syllabusBean.setText(jsonObject.getString("name"));


                                ExamTypeBeanArrayList.add(syllabusBean);
                            }
/*
                            syllabusAdapter = new SyllabusAdapter(SyllabusActivity.this, syllabusBeanArrayList);
                            binding.syllabusrecyclerview.setAdapter(syllabusAdapter);
                            if (jsonArray.length() > 0) {
                                binding.noDataLayout.setVisibility(View.GONE);
                            } else {
                                binding.noDataLayout.setVisibility(View.VISIBLE);
                            }*/


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
