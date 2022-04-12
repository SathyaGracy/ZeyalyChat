package com.zeyalychat.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.zeyalychat.com.R;
import com.zeyalychat.com.databinding.LayoutVerificationBinding;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class VerifyPhoneActivity extends AppCompatActivity implements View.OnClickListener {
    private String mVerificationId;
   // firebase auth object
    private FirebaseAuth mAuth;

    String mobile = "";

    String countrycode;
    LayoutVerificationBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.layout_verification);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_verification);

       // mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        countrycode = intent.getStringExtra("countrycode");
        initview();
        sendVerificationCode(mobile);

        timerStart();
        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
       /* findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });*/

    }

    private void sendVerificationCode(String mobile) {
        System.out.println("mobilenooooo"+countrycode + mobile);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                countrycode + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private void timerStart() {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                Animation animation_start_slide_out = AnimationUtils.loadAnimation(VerifyPhoneActivity.this, R.anim.fade_out);
                binding.resendOtp.setAnimation(animation_start_slide_out);
                binding.resendOtp.animate();
                animation_start_slide_out.start();
                binding.resendOtp.setVisibility(View.GONE);
                animation_start_slide_out.setFillAfter(false);
                animation_start_slide_out.setFillEnabled(false);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                binding.timer.setText("");
                Animation animation_start_slide_out = AnimationUtils.loadAnimation(VerifyPhoneActivity.this, R.anim.fade_in);
                binding.resendOtp.setAnimation(animation_start_slide_out);
                binding.resendOtp.animate();
                animation_start_slide_out.start();
                binding.resendOtp.setVisibility(View.VISIBLE);
                animation_start_slide_out.setFillAfter(false);
                animation_start_slide_out.setFillEnabled(false);
            }

        }.start();
    }

    private void initview() {
        textwatchListener();
        onclikListerner();


    }

    private void onclikListerner() {
        binding.send.setOnClickListener(this);
        binding.resendOtp.setOnClickListener(this);
        binding.closeLayout.setOnClickListener(this);

    }

    private void textwatchListener() {
        binding.otp1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                if (binding.otp1.getText().toString().length() == 1)     //size as per your requirement
                {
                    binding.otp2.requestFocus();
                } else {
                    hideKeyboard(binding.otp1);
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
        binding.otp2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                if (binding.otp2.getText().toString().length() == 1)     //size as per your requirement
                {
                    binding.otp3.requestFocus();
                } else {
                    binding.otp1.requestFocus();
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
        binding.otp3.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (binding.otp3.getText().toString().length() == 1)     //size as per your requirement
                {
                    binding.otp4.requestFocus();
                } else {
                    binding.otp2.requestFocus();
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
        binding.otp4.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (binding.otp4.getText().toString().length() == 1)     //size as per your requirement
                {
                    binding.otp5.requestFocus();
                } else {
                    binding.otp3.requestFocus();
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
        binding.otp5.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (binding.otp5.getText().toString().length() == 1)     //size as per your requirement
                {
                    binding.otp6.requestFocus();
                } else {
                    binding.otp4.requestFocus();
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
        binding.otp6.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                if (binding.otp6.getText().toString().length() == 1)     //size as per your requirement
                {
                    hideKeyboard(binding.otp6);
                } else {
                    binding.otp5.requestFocus();
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
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            System.out.println("code00000000000"+code);
            if (code != null) {
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            System.out.println("dvjsdjfhsdajh"+e.getMessage());
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            System.out.println("id"+s);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {

        //creating the credential
       /* PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        //signing the user
        signInWithPhoneAuthCredential(credential);*/

        Intent intent = new Intent(VerifyPhoneActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            // Toast.makeText(VerifyPhoneActivity.this, person.getString("message"), Toast.LENGTH_SHORT).show();
                           /* Intent returnIntent = new Intent();
                            returnIntent.putExtra("result", mobile);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();*/
                            Intent intent = new Intent(VerifyPhoneActivity.this, ResetPasswordActivity.class);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Toast.makeText(VerifyPhoneActivity.this,message, Toast.LENGTH_SHORT).show();

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                String code = binding.otp1.getText().toString().trim()
                        + binding.otp2.getText().toString().trim() + binding.otp3.getText().toString().trim() + binding.otp4.getText().toString().trim()
                        + binding.otp5.getText().toString().trim() + binding.otp6.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    binding.errorTxtPassword.setError("Enter valid code");
                    binding.otp1.setText("");
                    binding.otp2.setText("");
                    binding.otp3.setText("");
                    binding.otp4.setText("");
                    binding.otp5.setText("");
                    binding.otp6.setText("");
                    binding.otp1.requestFocus();
                    return;
                }
                //verifying the code entered manually
              verifyVerificationCode(code);
                break;
            case R.id.resend_otp:
                sendVerificationCode(mobile);
                timerStart();
                break;
            case R.id.close_layout:
                finish();
                break;
        }
    }
}
