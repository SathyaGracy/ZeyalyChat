package com.zeyalychat.com.activity;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.zeyalychat.com.R;
import com.zeyalychat.com.databinding.PasswordSetupBinding;
import com.zeyalychat.com.utils.IsNetworkConnected;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.Validation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    TransistionAnimation transistionAnimation;
    PasswordSetupBinding binding;

    IsNetworkConnected networkConnected;
    Dialog dialog_loading;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setContentView(R.layout.forgotpassword_layout);
        binding = DataBindingUtil.setContentView(this, R.layout.password_setup);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        transistionAnimation = new TransistionAnimation();
        getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
        getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        initView();
        onClickListener();
    }

    private void initView() {

        networkConnected = new IsNetworkConnected(this);

        dialog_loading = new Dialog(ResetPasswordActivity.this);
        dialog_loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLoad();
    }

    private void onClickListener() {
        binding.closeLayout.setOnClickListener(this);
        binding.send.setOnClickListener(this);
    }

    private void onTextChange() {
        binding.confirmPasswordEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                binding.confirmErrorTxtPassword.setVisibility(View.GONE);
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
        binding.passwordEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                binding.confirmErrorTxtPassword.setVisibility(View.GONE);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_layout:
                finish();
                break;
            case R.id.send:
                if (networkConnected.isNetworkConnected()) {
                    validCheck();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.Please_Connect_Network), Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

/*    private void stringRequest() {
        dialog_loading.show();
        final StringRequest stringReq = new StringRequest(Request.Method.POST, URLHelper.FORGOTPASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog_loading.dismiss();
                    JSONObject person = new JSONObject(response);
                    String status = person.getString("status");
                    if (status.equalsIgnoreCase("200")) {

                        Toast.makeText(ForgotPasswordActivity.this, person.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                    if (status.equalsIgnoreCase("500")) {
                        error_txt_password.setVisibility(View.VISIBLE);
                        error_txt_password.setText(person.getString("message"));
                        v3.setBackgroundColor(ContextCompat.getColor(ForgotPasswordActivity.this, R.color.button_color));
                        Toast.makeText(ForgotPasswordActivity.this, person.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Toast.makeText(ForgotPasswordActivity.this, "Email Send Successfully !.", Toast.LENGTH_LONG).show();
                // session.setKEY_FINGERPRINT_ENABLE(statuscheck + "");
                //do other things with the received JSONObject
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog_loading.dismiss();
                // Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> pars = new HashMap<String, String>();
                pars.put("Content-Type", "application/x-www-form-urlencoded");
                pars.put("Authorization", session.getKEY_AccessToken());
                return pars;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> setValue = new HashMap<>();
                setValue.put("email", password_edt.getText().toString());
                return setValue;
            }
        };
        //add to the request queue
        Maa.getInstance().addToRequestQueue(stringReq);
    }*/

    private void validCheck() {

        boolean check_validate = false;
        Validation validation = new Validation();
        if (!validation.emptyCheck(binding.passwordEdt)) {
            check_validate=false;
            binding.errorTxtPassword.setVisibility(View.VISIBLE);
            binding.errorTxtPassword.setText(getResources().getString(R.string.enter_password));
            binding.v1.setBackgroundColor(ContextCompat.getColor(ResetPasswordActivity.this, R.color.dark_red));
        } else {
            check_validate = true;
            if ( binding.errorTxtPassword.isShown()) {
                binding.errorTxtPassword.setVisibility(View.GONE);
            }
            binding.v3.setBackgroundColor(ContextCompat.getColor(ResetPasswordActivity.this, R.color.ViewColor));
        }
        if (!binding.passwordEdt.getText().toString().equals(binding.confirmErrorTxtPassword.getText().toString())) {
            check_validate=false;
            binding.errorTxtPassword.setVisibility(View.VISIBLE);
            binding.errorTxtPassword.setText(getResources().getString(R.string.mismatch));
            binding.v1.setBackgroundColor(ContextCompat.getColor(ResetPasswordActivity.this, R.color.dark_red));
            binding.v3.setBackgroundColor(ContextCompat.getColor(ResetPasswordActivity.this, R.color.dark_red));
        } else {
            check_validate = true;
            if ( binding.errorTxtPassword.isShown()) {
                binding.errorTxtPassword.setVisibility(View.GONE);
            }
            binding.v3.setBackgroundColor(ContextCompat.getColor(ResetPasswordActivity.this, R.color.ViewColor));
            binding.v1.setBackgroundColor(ContextCompat.getColor(ResetPasswordActivity.this, R.color.ViewColor));
        }

        if (check_validate) {
           // stringRequest();

        }

    }
    private void dialogLoad() {
        /*dialog_loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
