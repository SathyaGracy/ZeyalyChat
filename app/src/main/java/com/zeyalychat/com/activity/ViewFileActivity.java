package com.zeyalychat.com.activity;

import static android.view.View.OnClickListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.zeyalychat.com.R;
import com.zeyalychat.com.databinding.ViewfileActivityBinding;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;

public class ViewFileActivity extends AppCompatActivity implements OnClickListener {
    ViewfileActivityBinding binding;

    Session session;
    String url;
    String name="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.viewfile_activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        name = intent.getStringExtra("name");
        System.out.println("Exam completed" + url);
        intView();

    }

    @SuppressLint("RestrictedApi")
    private void intView() {
        session = new Session(ViewFileActivity.this);

        binding.filename.setText(name);
        if(name.equalsIgnoreCase("")){
            binding.tname.setVisibility(View.GONE);
        }


        binding.webview.getSettings().setLoadsImagesAutomatically(true);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webview.loadUrl(url);
        onItemClickListener();

    }


    private void onItemClickListener() {
        binding.backArraow.setOnClickListener(this);



       /* if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.fablayout.setVisibility(GONE);
            binding.filter.setVisibility(GONE);
        } else {
            binding.fablayout.setVisibility(VISIBLE);
            binding.filter.setVisibility(VISIBLE);
        }
*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArraow:
                finish();
                break;

        }


    }


}

