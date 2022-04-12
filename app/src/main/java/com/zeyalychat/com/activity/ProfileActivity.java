package com.zeyalychat.com.activity;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.squareup.picasso.Picasso;
import com.zeyalychat.com.R;
import com.zeyalychat.com.databinding.ProfileLayoutBinding;
import com.zeyalychat.com.utils.TransistionAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ProfileLayoutBinding binding;

    Dialog dialog_loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.profile_layout);
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
    private void intView(){
        binding.backArraow.setOnClickListener(this);
        dialog_loading=new Dialog(this);
        dialog_loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLoad();
        Picasso.with(ProfileActivity.this).load(MainActivity.userInfoArrayList.get(0).getProfile_image_url()).into(binding.profile);
    }
    private void dialogLoad() {
       /* dialog_loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_loading.setContentView(R.layout.dialog_loading);
        dialog_loading.setCanceledOnTouchOutside(false);*/

     //   History();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backArraow:
                finish();
                break;
        }

    }
   /* private void History() {

        AndroidNetworking.get(URLHelper.totalwithdraw)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addHeaders("Authorization", session.getKEY_AccessToken())
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        System.out.println("in");

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("out");

                        try {
                            System.out.println("result--ProfitHistory  History---->" + response.toString());
                            JSONObject jsonObject = new JSONObject(response.toString());
                            //  JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("JSONException profit"+e);
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        if (error.getErrorCode() != 0) {
                            System.out.println("tradehistoryerror  " + error.getErrorBody());
                            if (error.getErrorCode() == 401) {
                                session.logout();
                            }
                            // received ANError from server
                            // error.getErrorCode() - the ANError code from server
                            // error.getErrorBody() - the ANError body from server
                            // error.getErrorDetail() - just a ANError detail

                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            System.out.println("tradehistoryerror  " + error.getErrorBody());
                        }
                    }
                });


    }*/
}
