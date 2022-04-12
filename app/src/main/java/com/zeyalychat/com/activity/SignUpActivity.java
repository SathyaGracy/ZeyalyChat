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
import com.zeyalychat.com.Application;
import com.zeyalychat.com.R;
import com.zeyalychat.com.databinding.SignupLayoutBinding;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;
import com.zeyalychat.com.utils.Utility;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.zeyalychat.com.Application.trimMessage;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    SignupLayoutBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.signup_layout);
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
        binding.signinLayout.setOnClickListener(this);
        binding.signupLayout.setOnClickListener(this);
        binding.hideShowLayout.setOnClickListener(this);
        binding.confirmHideShowLayout.setOnClickListener(this);
        binding.hideShowImg.setTag(0);
        binding.passwordEdt.setTransformationMethod(new PasswordTransformationMethod());
        binding.confirmHideShowImg.setTag(0);
        binding.confirmPasswordEdt.setTransformationMethod(new PasswordTransformationMethod());
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
        binding.mobileEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.errorTxtMobile.getVisibility() == View.VISIBLE) {
                    binding.errorTxtMobile.setVisibility(View.GONE);
                }
            }
        });
        binding.confirmPasswordEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.errorTxtConfirmPassword.getVisibility() == View.VISIBLE) {
                    binding.errorTxtConfirmPassword.setVisibility(View.GONE);
                }
            }
        });
        binding.emailEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.errorTxtEmail.getVisibility() == View.VISIBLE) {
                    binding.errorTxtEmail.setVisibility(View.GONE);
                }
            }
        });

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
            case R.id.confirm_hide_show_layout:
                int eye1 = (int) binding.confirmHideShowImg.getTag();
                if (eye1 == 0) {
                    binding.confirmHideShowImg.setImageResource(R.mipmap.hide_image);
                    binding.confirmPasswordEdt.setTransformationMethod(null);
                    binding.confirmPasswordEdt.setTransformationMethod(null);
                    binding.confirmHideShowImg.setTag(1);
                } else {
                    binding.confirmHideShowImg.setImageResource(R.mipmap.hide_password);
                    binding.confirmPasswordEdt.setTransformationMethod(new PasswordTransformationMethod());
                    binding.confirmPasswordEdt.setTransformationMethod(new PasswordTransformationMethod());
                    binding.confirmHideShowImg.setTag(0);
                }
                break;
            case R.id.signup_layout:
                if(checkValidation())
                signUp();
                break;
            case R.id.signinLayout:
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    private boolean checkValidation() {
        boolean result = true;
        if (binding.emailEdt.getText().toString().trim().length() == 0) {
            binding.errorTxtEmail.setText(getString(R.string.enter_email));
            binding.errorTxtEmail.setVisibility(View.VISIBLE);
            //  v1.setBackgroundColor(ContextCompat.getColor(context, R.color.red_color));
            result = false;
        } else if (Utility.isEmailValid(binding.emailEdt.getText().toString().trim())) {
            binding.errorTxtEmail.setText(getString(R.string.enter_valid_email));
            binding.errorTxtEmail.setVisibility(View.VISIBLE);
            //     v1.setBackgroundColor(ContextCompat.getColor(context, R.color.red_color));
            result = false;

        }
        if (binding.usernameEdt.getText().toString().trim().length() == 0) {
            binding.errorTxtUsername.setText(getString(R.string.enter_user_name));
            binding.errorTxtUsername.setVisibility(View.VISIBLE);
            //  v1.setBackgroundColor(ContextCompat.getColor(context, R.color.red_color));
            result = false;
        }
        if (binding.mobileEdt.getText().toString().trim().length() == 0) {
            binding.errorTxtMobile.setText(getString(R.string.enter_mobileNo));
            binding.errorTxtMobile.setVisibility(View.VISIBLE);
            //  v1.setBackgroundColor(ContextCompat.getColor(context, R.color.red_color));
            result = false;
        }
        if (binding.passwordEdt.getText().toString().trim().length() == 0) {
            binding.errorTxtPassword.setText(getString(R.string.enter_password));
            //   v2.setBackgroundColor(ContextCompat.getColor(context, R.color.red_color));
            result = false;
            binding.errorTxtPassword.setVisibility(View.VISIBLE);
        }
        if (binding.confirmPasswordEdt.getText().toString().trim().length() == 0) {
            binding.errorTxtConfirmPassword.setText(getString(R.string.enter_password));
            //   v2.setBackgroundColor(ContextCompat.getColor(context, R.color.red_color));
            result = false;
            binding.errorTxtConfirmPassword.setVisibility(View.VISIBLE);
        }
        if (!binding.passwordEdt.getText().toString().equals(binding.confirmPasswordEdt.getText().toString())) {
            binding.errorTxtPassword.setText(getString(R.string.mismatch));
            //   v2.setBackgroundColor(ContextCompat.getColor(context, R.color.red_color));
            result = false;
            binding.errorTxtPassword.setVisibility(View.VISIBLE);
        }



        /*else if (binding.passwordEdt.getText().toString().trim().length() < 6) {
            binding.errorTxtPassword.setText(getString(R.string.password_must_six_characters));
          //  v2.setBackgroundColor(ContextCompat.getColor(context, R.color.red_color));
            result = false;
            binding.errorTxtPassword.setVisibility(View.VISIBLE);
        }*/
        return result;
    }
    public void signUp() {
        JSONObject object = jsonMake();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.createaccount,
                object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //  try {
                System.out.println("response"+response.toString());
                    /*if (response.getString("STATUS").equalsIgnoreCase("0")) {
                        Toast.makeText(TravelItinerrayActivity.this, "Travel Create Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }*/

               /* } catch (
                        JSONException e) {
                    e.printStackTrace();
                }*/

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
                                Toast.makeText(SignUpActivity.this, errorObj.optString("message"), Toast.LENGTH_SHORT).show();
                                //displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                // displayMessage(getString(R.string.something_went_wrong));
                            }
                        } else if (response.statusCode == 401) {
                            Toast.makeText(SignUpActivity.this, errorObj.optString("message"), Toast.LENGTH_SHORT).show();
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
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                // headers.put("Authorization", session.getKEYAuth());
                return headers;
            }
        };

        Application.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private JSONObject jsonMake() {
        JSONObject json = new JSONObject();

        try {

            json.put("username", "sat");
            json.put("password", "test");
            json.put("phone","9790289943");
            json.put("email", "sat@gmail.com");




        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
