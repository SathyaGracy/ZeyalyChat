package com.zeyalychat.com.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.zeyalychat.com.R;
import com.zeyalychat.com.databinding.VideoViewBinding;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;

public class VideoViewActivity extends AppCompatActivity implements View.OnClickListener {
    VideoViewBinding binding;
    String url="";

    Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.video_view);
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

        intView();

    }

    @SuppressLint("RestrictedApi")
    private void intView() {
        session = new Session(VideoViewActivity.this);
       String[] token= session.getKEYAuth().split(" ");

        final MediaController mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(binding.videoView);


        binding.videoView.setMediaController(mediacontroller);
        binding.videoView.setVideoURI(Uri.parse(url+"&token="+token[1]));
        binding.videoView.requestFocus();
        binding.videoView.start();

        binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        binding.videoView.setMediaController(mediacontroller);
                        mediacontroller.setAnchorView(binding.videoView);

                    }
                });
            }
        });

        binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getApplicationContext(), "Video over", Toast.LENGTH_SHORT).show();



            }
        });

        binding.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("API123", "What " + what + " extra " + extra);
                return false;
            }
        });
        binding.backArraow.setOnClickListener(this::onClick);
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

