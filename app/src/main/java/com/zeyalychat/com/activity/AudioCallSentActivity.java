package com.zeyalychat.com.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zeyalychat.com.R;
import com.zeyalychat.com.adpter.AudioAdapter;
import com.zeyalychat.com.bean.AudioBean;
import com.zeyalychat.com.databinding.AudioListBinding;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;

import java.util.ArrayList;

public class AudioCallSentActivity extends AppCompatActivity implements View.OnClickListener {
    AudioListBinding binding;
    ArrayList<AudioBean> audioBeanArrayList;
    AudioAdapter audioAdapter;
    Session session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.audio_list);
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
        session = new Session(AudioCallSentActivity.this);
        audioBeanArrayList=new ArrayList<>();
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(AudioCallSentActivity.this, LinearLayoutManager.VERTICAL, false));

        audioAdapter = new AudioAdapter(AudioCallSentActivity.this, audioBeanArrayList);
        binding.recyclerview.setAdapter(audioAdapter);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AudioCallSentActivity.this, AudioActivity.class);
                startActivity(intent);
            }
        });
        binding.backArraow.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backArraow:
                finish();
                break;
        }
    }
}
