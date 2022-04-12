package com.zeyalychat.com.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.zeyalychat.com.Application;
import com.zeyalychat.com.R;
import com.zeyalychat.com.adpter.DialogAdapter;
import com.zeyalychat.com.bean.StringBean;
import com.zeyalychat.com.databinding.CreateLeaveBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.zeyalychat.com.Application.trimMessage;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class LeaveCreateActiviity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    CreateLeaveBinding binding;
    ArrayList<StringBean> LeaveTypeArrayList;

    int SelectTypeId;
    int DateFromTo=0;

    String FromDate = "";
    String ToDate = "";

    Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.create_leave);
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
        session = new Session(LeaveCreateActiviity.this);
        onItemClickListener();
        LeaveType();

    }

    private void onItemClickListener() {
        binding.backArraow.setOnClickListener(this);
        binding.TypeLayout.setOnClickListener(this);
        binding.ToDateLayout.setOnClickListener(this);

        binding.DateLayout.setOnClickListener(this);
        binding.submit.setOnClickListener(this);
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.ToDateRl.setVisibility(View.GONE);
            binding.FromDateLbl.setText(getResources().getText(R.string.Date));
        } else {
            binding.ToDateRl.setVisibility(View.VISIBLE);
            binding.FromDateLbl.setText(getResources().getText(R.string.FromDate));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArraow:
                finish();
                break;
            case R.id.TypeLayout:
                Dialog("Leave Type", 0);
                break;
            case R.id.DateLayout:
                DateFromTo=1;
                setNormalPicker();
                break;
            case R.id.ToDateLayout:
                DateFromTo=2;
                setNormalPicker();
                break;
            case R.id.submit:
                CreateLeave();
                break;
        }


    }

    private void setNormalPicker() {
        Calendar now = getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(YEAR), // Initial year selection
                now.get(MONTH), // Initial month selection
                now.get(DAY_OF_MONTH) // Inital day selection
        );
// If you're calling this from a support Fragment
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
        int color = ContextCompat.getColor(LeaveCreateActiviity.this, R.color.green);
        Calendar cal1 = Calendar.getInstance();
        dpd.setAccentColor(color);
        dpd.setMinDate(cal1);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if(DateFromTo==1) {
            FromDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "";
            binding.DateTxt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + "");
        }else {
            ToDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "";
            binding.ToDateTxt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + "");
        }
    }

    private void Dialog(String heading, int val) {
        final Dialog dialog = new Dialog(LeaveCreateActiviity.this);
        DialogAdapter dialogAdapter;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_list);

        TextView Heading = dialog.findViewById(R.id.Heading);
        Heading.setText(heading);

        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout searchRL = dialog.findViewById(R.id.searchRL);
        searchRL.setVisibility(View.GONE);
        RecyclerView mainList = dialog.findViewById(R.id.mainList);
        mainList.setLayoutManager(new LinearLayoutManager(LeaveCreateActiviity.this, LinearLayoutManager.VERTICAL, false));


        if (val == 0) {
            dialogAdapter = new DialogAdapter(LeaveCreateActiviity.this, LeaveTypeArrayList);
            mainList.setAdapter(dialogAdapter);
        }


        mainList.addOnItemTouchListener(new RecyclerTouchListener(LeaveCreateActiviity.this, mainList,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if (val == 0) {
                            binding.SelectTypeTxt.setText(LeaveTypeArrayList.get(position).getText());
                            SelectTypeId = LeaveTypeArrayList.get(position).getId();
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

    private void LeaveType() {
        LeaveTypeArrayList = new ArrayList<>();

        AndroidNetworking.get(URLHelper.Leavetype)
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


                                LeaveTypeArrayList.add(stringBean);
                            }
                           /* DialogAdapter dialogAdapter = new HomeWorkAdapter(LeaveCreateActiviity.this, SessionArrayList);
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


    public void CreateLeave() {
        JSONObject object = null;
        String url=null;
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            object = jsonMake();
            url = URLHelper.levaeListStd;
        } else {
            object = jsonMakeStaff();
            url = URLHelper.LeaveListStaff;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                System.out.println("response" + response.toString());

                finish();

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
                                Toast.makeText(LeaveCreateActiviity.this, errorObj.optString("message"), Toast.LENGTH_SHORT).show();
                                //displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                // displayMessage(getString(R.string.something_went_wrong));
                            }
                        } else if (response.statusCode == 401) {
                            Toast.makeText(LeaveCreateActiviity.this, errorObj.optString("message"), Toast.LENGTH_SHORT).show();
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
   /* private void CreateHomework() {
        JSONObject object = jsonMake();
        AndroidNetworking.post(URLHelper.homwwork)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .addBodyParameter(object.toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        finish();


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
    }*/

    private JSONObject jsonMake() {

        JSONObject form = new JSONObject();

        try {

            form.put("type", SelectTypeId);
            form.put("reason", binding.DescreptionTxt.getText().toString());
            form.put("duration", 8);
            form.put("is_full_day", true);
            form.put("date", FromDate);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("form json" + form.toString());
        return form;
    }

    private JSONObject jsonMakeStaff() {

        JSONObject form = new JSONObject();

        try {

            form.put("type", SelectTypeId);
            form.put("reason", binding.DescreptionTxt.getText().toString());
            form.put("from_date", FromDate);
            form.put("to_date", FromDate);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("form json" + form.toString());
        return form;
    }
}


