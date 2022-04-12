package com.zeyalychat.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.hbb20.CountryCodePicker;
import com.zeyalychat.com.R;
import com.zeyalychat.com.databinding.MobilenumberTypeLayoutBinding;
import com.zeyalychat.com.utils.TransistionAnimation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;



public class MobileVerficationActivity extends AppCompatActivity implements View.OnClickListener {

    private int MobileCheck = 144;

    String Countrycode = "+91";
    MobilenumberTypeLayoutBinding binding;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.mobilenumber_type_layout);
     //   setContentView(R.layout.mobilenumber_type_layout);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
        getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        initView();
    }

    private void initView() {
        onClickLisener();
        onTextChange();
        Countrycode = "+"+binding.ccp.getDefaultCountryCode();
        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Countrycode = "+"+binding.ccp.getSelectedCountryCode();
                System.out.println("Updated " + binding.ccp.getSelectedCountryCode());
            }
        });
    }

    private void onClickLisener() {
        binding.closeLayout.setOnClickListener(this);
        binding.send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_layout:
                finish();
                break;
            case R.id.send:
                validationCheck();
                break;
        }


    }

    private void onTextChange() {
        binding.mobileEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
               /* if (error_txt_mobile.isShown()) {
                    error_txt_mobile.setVisibility(View.GONE);
                }
                v4.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.ViewColor));
                if (s.length() == 10) {
                    Intent intent = new Intent(SignUpActivity.this, VerifyPhoneActivity.class);
                    intent.putExtra("mobile", mobile_edt.getText().toString());
                    startActivityForResult(intent, MobileCheck);
                }*/

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

    private void validationCheck() {
        if (binding.mobileEdt.getText().toString().equalsIgnoreCase("")) {
            System.out.println("is true mobile ety------------------>");
            binding.errorTxtMobile.setVisibility(View.VISIBLE);
            binding.errorTxtMobile.setText(getResources().getString(R.string.enter_mobileNo));
            binding.v4.setBackgroundColor(ContextCompat.getColor(MobileVerficationActivity.this, R.color.dark_red));
        } else {
            if (binding.errorTxtMobile.isShown()) {
                binding.errorTxtMobile.setVisibility(View.GONE);
            }
            binding.v4.setBackgroundColor(ContextCompat.getColor(MobileVerficationActivity.this, R.color.ViewColor));
            Intent intent = new Intent(MobileVerficationActivity.this, VerifyPhoneActivity.class);
            intent.putExtra("mobile", binding.mobileEdt.getText().toString());
            intent.putExtra("countrycode", Countrycode);
            startActivityForResult(intent, MobileCheck);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == MobileCheck && resultCode == RESULT_OK) {
                String mobile = data.getStringExtra("result");
                System.out.println("mobile_check--------->"+mobile);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", mobile);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
