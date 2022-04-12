package com.zeyalychat.com.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.zeyalychat.com.R;
import com.zeyalychat.com.databinding.MeetingDetailBinding;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class MeetingDetailActivity extends AppCompatActivity implements View.OnClickListener {
    MeetingDetailBinding binding;
    Session session;
    String MettingId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.meeting_detail);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }
        Intent intent = getIntent();
        MettingId = intent.getStringExtra("MeetingId");

        intView();

    }

    private void intView() {
        session = new Session(MeetingDetailActivity.this);
        binding.backArraow.setOnClickListener(this);
        Meeting();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArraow:
                finish();
                break;
        }
    }

    private void Meeting() {
        String url = "";
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            url = URLHelper.meeting + "?section_id=" + MainActivity.userInfoArrayList.get(0).getSection_id()  + "&id=" + MettingId;
        } else {
            url = URLHelper.meeting + "?staff_id=" + MainActivity.userInfoArrayList.get(0).getId() + "&section_id=" + MainActivity.userInfoArrayList.get(0).getSection_id() + "&id=" + MettingId;
            ;
        }
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        try {

                            binding.MeetingIDtxt.setText(jsonObject.getString("meeting_id"));
                            binding.endtimeTxt.setText(jsonObject.getString("end_time"));
                            binding.starttimeTxt.setText(jsonObject.getString("start_time"));
                            binding.MeetingUrlTxt.setText(jsonObject.getString("meeting_url"));
                            binding.passcodeTxt.setText(jsonObject.getString("passcode"));
                            binding.Title.setText(jsonObject.getString("title"));



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
