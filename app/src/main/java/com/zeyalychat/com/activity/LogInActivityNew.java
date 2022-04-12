package com.zeyalychat.com.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.zeyalychat.com.R;
import com.zeyalychat.com.databinding.LoginBinding;
import com.zeyalychat.com.utils.IsNetworkConnected;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.Validation;

import java.util.regex.Pattern;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;


public class LogInActivityNew extends AppCompatActivity implements View.OnClickListener {

    Boolean check_password_show = false;
   // Session session;
    IsNetworkConnected networkConnected;

    Dialog dialog_loading;
    LoginBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login_layout);
        binding = DataBindingUtil.setContentView(this, R.layout.login);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
        getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        initView();
        dialog_loading = new Dialog(LogInActivityNew.this);
        dialog_loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLoad();
    }

    private void initView() {
        networkConnected = new IsNetworkConnected(this);
        onClickListner();
        onTextChange();

    }

    private void onTextChange() {
        binding.usernameEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                if (s.length() > 1) {
                    char ch = s.charAt(0);
                    binding.errorTxtUsername.setVisibility(View.VISIBLE);
                    if (Character.isDigit(ch)) {
                        if (!Pattern.matches("[a-zA-Z]+", s.toString())) {
                            if (s.toString().length() < 6 || s.toString().length() > 13) {
                                binding.errorTxtUsername.setVisibility(View.GONE);
                            } else {
                                binding.errorTxtUsername.setVisibility(View.VISIBLE);
                                binding.errorTxtUsername.setText("Enter Mobile Number");
                            }
                        }

                    } else {
                        binding.errorTxtUsername.setText("Enter Valid Email");
                    }
                    Validation validation = new Validation();
                    if (validation.emailValidation(binding.usernameEdt)) {
                        binding.errorTxtUsername.setVisibility(View.GONE);
                    }

                } else {
                    binding.errorTxtUsername.setVisibility(View.GONE);
                    binding.errorTxtUsername.setText("");
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub


            }

        });
        binding.passwordEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                binding.errorTxtUsername.setVisibility(View.GONE);
                binding.errorTxtPassword.setVisibility(View.GONE);


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub


            }

        });
    }

    private void onClickListner() {

        binding.signinLayout.setOnClickListener(this);
        binding.forgotLtR.setOnClickListener(this);
        binding.hideShowLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.signin_layout:
                if (networkConnected.isNetworkConnected()) {
                    validationCheck();
                } else {
                    Toast.makeText(LogInActivityNew.this, getResources().getString(R.string.Please_Connect_Network), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.hide_show_layout:
                if (check_password_show) {
                    binding.passwordEdt.setTransformationMethod(new PasswordTransformationMethod());
                    binding.hideShowImg.setImageDrawable(getResources().getDrawable(R.mipmap.hide_password));
                    check_password_show = false;

                } else {
                    binding.passwordEdt.setTransformationMethod(null);
                    binding.hideShowImg.setImageDrawable(getResources().getDrawable(R.mipmap.hide_image));
                    check_password_show = true;
                }

                break;

            case R.id.forgot_lt_R:
                /*Intent forgot = new Intent(LogInActivityNew.this, MobileVerficationActivity.class);
                startActivity(forgot);*/
                break;
        }
    }

    private void validationCheck() {
        boolean check_empty_user = false;
        boolean check_empty_password = false;
        Validation validation = new Validation();

        if (!validation.emptyCheck(binding.usernameEdt)) {
            binding.errorTxtUsername.setVisibility(View.VISIBLE);
            binding.errorTxtUsername.setText(getResources().getString(R.string.enter_email));
            binding.v1.setBackgroundColor(ContextCompat.getColor(LogInActivityNew.this, R.color.dark_red));
        } else {
            check_empty_user = true;
            if (binding.errorTxtUsername.isShown()) {
                binding.errorTxtUsername.setVisibility(View.GONE);
            }binding.v1.setBackgroundColor(ContextCompat.getColor(LogInActivityNew.this, R.color.ViewColor));
        }

        if (!validation.emptyCheck(binding.passwordEdt)) {
            binding.errorTxtPassword.setVisibility(View.VISIBLE);
            binding.errorTxtPassword.setText(getResources().getString(R.string.enter_password));
            binding.v3.setBackgroundColor(ContextCompat.getColor(LogInActivityNew.this, R.color.dark_red));
        } else {
            check_empty_password = true;
            if (binding.errorTxtPassword.isShown()) {
                binding.errorTxtPassword.setVisibility(View.GONE);
            }
            binding.v3.setBackgroundColor(ContextCompat.getColor(LogInActivityNew.this, R.color.ViewColor));
        }

        if (check_empty_user && check_empty_password) {
            if (networkConnected.isNetworkConnected()) {
                //logIN();
           /*     Intent intent = new Intent(LogInActivityNew.this, MobileVerficationActivity.class);
                startActivity(intent);
                finish();*/
            } else {
                Toast.makeText(LogInActivityNew.this, getResources().getString(R.string.Please_Connect_Network), Toast.LENGTH_SHORT).show();
            }
        }

    }

    /*public void logIN() {
        System.out.println("set login---------------->");
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("username", username_edt.getText().toString());
            paramObject.put("password", password_edt.getText().toString());
            paramObject.put("deviceId", session.getKEY_DEVICE_ID());
            paramObject.put("deviceToken", session.getDeviceToken());
            System.out.println("deviceToken------------------------->"+session.getDeviceToken());
            paramObject.put("deviceType", "Android");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.LOGIN,
                paramObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equalsIgnoreCase("200")) {


                        JSONObject jsonObject = response.getJSONObject("result");
                        //  session.setKEY_UserName(username_edt.getText().toString());
                        session.setKEY_Email(username_edt.getText().toString());
                        session.setKEY_AccessToken("Bearer " + jsonObject.getString("token"));
                        session.setKEY_UserID(jsonObject.getString("username"));
                        session.setKEY_FINGERPRINT_ENABLE(jsonObject.getString("fingerStatus"));
                        session.setKEY_SETPIN(jsonObject.getString("pin"));
                        session.setKEY_PINENABLE(jsonObject.getString("pinstatus"));
                        session.setKEY_INFIRST("1");
                        Intent intent = new Intent(LogInActivity.this, GetDataActivity.class);
                        startActivity(intent);
                        finish();


                    }
                    if (status.equalsIgnoreCase("500")) {
                        System.out.println("login 500");
                        error_txt_password.setVisibility(View.VISIBLE);
                        error_txt_password.setText(response.getString("message"));
                        //   v3.setBackgroundColor(ContextCompat.getColor(LogInActivity.this, R.color.button_color));
                        // Toast.makeText(LogInActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    //  Toast.makeText(LogInActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();

                }
                String responses = response.toString();
                //  processResponse(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                String Message;


                NetworkResponse response = error.networkResponse;
                System.out.println("response errererere----------------->" + response);
                if (response != null && response.data != null) {

                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));
                        System.out.println("errorObj----------------->" + errorObj);
                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                            try {
                                //displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                // displayMessage(getString(R.string.something_went_wrong));
                            }
                        } else if (response.statusCode == 401) {
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
                return headers;
            }
        };

        Maa.getInstance().addToRequestQueue(jsonObjectRequest);
    }*/

    private void dialogLoad() {
       /* dialog_loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_loading.setContentView(R.layout.dialog_loading);
        dialog_loading.setCanceledOnTouchOutside(false);*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog_loading != null) {
            dialog_loading.dismiss();
            dialog_loading = null;
        }
    }
}
