package com.zeyalychat.com.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

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
import com.zeyalychat.com.Application;
import com.zeyalychat.com.Database.DatabaseHelper;
import com.zeyalychat.com.R;
import com.zeyalychat.com.databinding.LoginLayoutBinding;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.zeyalychat.com.Application.trimMessage;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    LoginLayoutBinding binding;
    Session session;
    DatabaseHelper databaseHelper;
    String Id, Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.login_layout);
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
        session = new Session(LoginActivity.this);
        databaseHelper = new DatabaseHelper(LoginActivity.this);
        binding.signinLayout.setOnClickListener(this);
        binding.forgotLtR.setOnClickListener(this);
        binding.hideShowLayout.setOnClickListener(this);
        binding.hideShowImg.setTag(0);
        binding.passwordEdt.setTransformationMethod(new PasswordTransformationMethod());
        binding.passwordEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.errorTxtPassword.getVisibility() == View.VISIBLE) {
                    binding.errorTxtPassword.setVisibility(View.GONE);
                }
            }
        });
        binding.usernameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.errorTxtUsername.getVisibility() == View.VISIBLE) {
                    binding.errorTxtUsername.setVisibility(View.GONE);
                }
            }
        });

    }

    private boolean checkValidation() {
        boolean result = true;
        if (binding.usernameEdt.getText().toString().trim().length() == 0) {
            binding.errorTxtUsername.setText(getString(R.string.enter_email));
            binding.errorTxtUsername.setVisibility(View.VISIBLE);
            //  v1.setBackgroundColor(ContextCompat.getColor(context, R.color.red_color));
            result = false;
        } /*else if (Utility.isEmailValid(binding.usernameEdt.getText().toString().trim())) {
            binding.errorTxtUsername.setText(getString(R.string.enter_valid_email));
            binding.errorTxtUsername.setVisibility(View.VISIBLE);
            //     v1.setBackgroundColor(ContextCompat.getColor(context, R.color.red_color));
            result = false;

        }*/
        if (binding.passwordEdt.getText().toString().trim().length() == 0) {
            binding.errorTxtPassword.setText(getString(R.string.enter_password));
            //   v2.setBackgroundColor(ContextCompat.getColor(context, R.color.red_color));
            result = false;
            binding.errorTxtPassword.setVisibility(View.VISIBLE);
        } /*else if (binding.passwordEdt.getText().toString().trim().length() < 6) {
            binding.errorTxtPassword.setText(getString(R.string.password_must_six_characters));
          //  v2.setBackgroundColor(ContextCompat.getColor(context, R.color.red_color));
            result = false;
            binding.errorTxtPassword.setVisibility(View.VISIBLE);
        }*/
        return result;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hide_show_layout:
                int eye = (int) binding.hideShowImg.getTag();
                if (eye == 0) {
                    binding.hideShowImg.setImageResource(R.mipmap.hide_image);
                    binding.passwordEdt.setTransformationMethod(null);
                    binding.passwordEdt.setTransformationMethod(null);
                    binding.hideShowImg.setTag(1);
                } else {
                    binding.hideShowImg.setImageResource(R.mipmap.hide_password);
                    binding.passwordEdt.setTransformationMethod(new PasswordTransformationMethod());
                    binding.passwordEdt.setTransformationMethod(new PasswordTransformationMethod());
                    binding.hideShowImg.setTag(0);
                }
                break;
            case R.id.signin_layout:
                if (checkValidation()) {
                    logIN();

                }

                break;
            case R.id.forgot_lt_R:
              /*  Intent intent = new Intent(LoginActivity.this, MobileVerficationActivity.class);
                startActivity(intent);*/
                break;
        }

    }

    /*   private void logIN() {

           AndroidNetworking.post(URLHelper.createaccount)
                   .addHeaders("Content-Type", "application/json")
                   .addBodyParameter("username", "mani")
                   .addBodyParameter("password", "test")
                   .addBodyParameter("phone","9566082053")
                   .addBodyParameter("email", "allformani@gmail.com")
                   .setPriority(Priority.MEDIUM)
                   .build()
                   .getAsJSONObject(new JSONObjectRequestListener() {
                       @Override
                       public void onResponse(JSONObject response) {
                           System.out.println("json----------->" + response.toString());
                           *//*try {
                            if (response.has("access")) {
                                System.out.println("Access Token: "+response.getString("access"));
                                //                                session.setKEYAuth("Bearer  " + response.getString("access"));
                                //                                // session.setKEY_REFRESH("Bearer  " + response.getString("refresh"));
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }


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
    public void logIN() {
        JSONObject object = jsonMake();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.auth,
                object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("response" + response.toString());

                    if (response.getString("status").equalsIgnoreCase("200")) {
                        response.getString("email");
                        response.getString("expires_in");
                        Id = response.getString("id");
                        Name = response.getString("name");
                        response.getString("phone");
                      /*  if(session.getEntry().equalsIgnoreCase("0")) {
                            session.setEntry("1");
                            session.setCurrentEntry("1");
                        }else {
                           int value=Integer.parseInt(session.getEntry());
                            session.setEntry(value+1+"");
                            session.setCurrentEntry(value+1+"");
                        }*/
                        session.setKEYAuth("Token " + response.getString("token"));
                        // session.setKEY_MULTILOG(session.getEntry(),"Token "+response.getString("token"));
                        // session.setKEY_AccountActive(session.getEntry(),true);
                        System.out.println("acc token : " + "Token " + response.getString("token"));

                        session.setCurrentEntry(response.getString("id"));
                        System.out.println("check value :::::" + databaseHelper.CheckIsDataAlreadyInDBorNot(response.getString("id")));
                        /*if(!databaseHelper.CheckIsDataAlreadyInDBorNot(response.getString("id"))) {
                            databaseHelper.insertUser(response.getString("id"),
                                    "", 1, response.getString("name"), 1, "Token " + response.getString("token"));
                            // Toast.makeText(LoginActivity.this, "Travel Create Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }*/
                        GetUserInfo();
                    }

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }

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
                                Toast.makeText(LoginActivity.this, errorObj.optString("message"), Toast.LENGTH_SHORT).show();
                                //displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                // displayMessage(getString(R.string.something_went_wrong));
                            }
                        } else if (response.statusCode == 401) {
                            Toast.makeText(LoginActivity.this, errorObj.optString("message"), Toast.LENGTH_SHORT).show();
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
                // headers.put("Authorization", session.getKEYAuth());
                return headers;
            }
        };

        Application.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private JSONObject jsonMake() {
        JSONObject json = new JSONObject();

        try {
            // {"username":"allformani@gmail.com","password":"test"}
         /*   json.put("username", "sathya@zeyaly.com");
            json.put("password", "1234");*/
            //binding.usernameEdt.getText().toString().trim();
            json.put("username", binding.usernameEdt.getText().toString().trim());
            json.put("password", binding.passwordEdt.getText().toString().trim());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void GetUserInfo() {
        AndroidNetworking.get(URLHelper.getuserinfo)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("json---userinfo-------->" + response.toString());
                        try {


                            if (!databaseHelper.CheckIsDataAlreadyInDBorNot(response.getString("id"))) {
                                databaseHelper.insertUser(Id,
                                        response.getString("profile_image_url"), 1, Name, 1, session.getKEYAuth());
                                // Toast.makeText(LoginActivity.this, "Travel Create Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                            }


                            //  if (from.equalsIgnoreCase("main")) {


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
                            databaseHelper.deleteAllUserDetaill();


                        }
                    }
                });
    }


}
