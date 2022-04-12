package com.zeyalychat.com.activity;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.zeyalychat.com.R;
import com.zeyalychat.com.adpter.SectionAdapter;
import com.zeyalychat.com.bean.SectionBean;
import com.zeyalychat.com.databinding.AudioActivityBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class AudioActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    AudioActivityBinding binding;

    private static final String LOG_TAG = "AudioRecord";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;


    private MediaRecorder recorder = null;
    boolean mStartRecording = true;
    boolean mStartPlaying = true;
    private MediaPlayer player = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    ArrayList<SectionBean> SectionBeanArrayList;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    int SectionId;
    Session session;
    String AudioDate;
    String Time;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.audio_activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecord.3gp";
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        binding.recordButton.setText("Start recording");
        binding.playButton.setText("Start playing");
        binding.recordButtonRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onRecord(mStartRecording);
                if (mStartRecording) {
                    binding.recordButton.setText("Stop recording");
                    binding.recordImg.setImageDrawable(getDrawable(R.mipmap.stop));
                    binding.recordImg.setColorFilter(ContextCompat.getColor(AudioActivity.this, R.color.red_color), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    binding.recordButton.setText("Start recording");
                    binding.recordImg.setImageDrawable(getDrawable(R.mipmap.play));
                    binding.recordImg.setColorFilter(ContextCompat.getColor(AudioActivity.this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                mStartRecording = !mStartRecording;
            }
        });

        binding.playButtonRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    binding.playButton.setText("Stop playing");
                    binding.playImg.setImageDrawable(getDrawable(R.mipmap.stop));
                    binding.playImg.setColorFilter(ContextCompat.getColor(AudioActivity.this, R.color.red_color), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    binding.playButton.setText("Start playing");
                    binding.playImg.setImageDrawable(getDrawable(R.mipmap.play));
                    binding.playImg.setColorFilter(ContextCompat.getColor(AudioActivity.this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);

                }
                mStartPlaying = !mStartPlaying;
            }
        });
        session = new Session(AudioActivity.this);
        onClickListener();
    }

    private void onClickListener() {
        binding.TypeLayout1.setOnClickListener(this);
        binding.backArraow.setOnClickListener(this);
        binding.submit.setOnClickListener(this);
        binding.DatewRLayout.setOnClickListener(this);
        binding.timeRl.setOnClickListener(this);

        Section();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TypeLayout1:
                Dialog();
                break;
            case R.id.backArraow:
                finish();
                break;
            case R.id.DatewRLayout:
                setNormalPicker();
                break;
            case R.id.timeRl:
                setTimerPicker();
                break;
            case R.id.submit:
                // openWhatsApp();
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setAction(Intent.ACTION_VIEW);
                sendIntent.setPackage("com.whatsapp");
                String url = "https://api.whatsapp.com/send?phone=" + "+91 (741) 860-1463" + "&text=" + "Hi";
                sendIntent.setData(Uri.parse(url));
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendIntent);
                }
                break;
        }
    }

    private void Dialog() {
        final Dialog dialog = new Dialog(AudioActivity.this);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_list);

        TextView Heading = dialog.findViewById(R.id.Heading);
        Heading.setText("Class");

        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout searchRL = dialog.findViewById(R.id.searchRL);
        searchRL.setVisibility(View.GONE);
        RecyclerView mainList = dialog.findViewById(R.id.mainList);
        mainList.setLayoutManager(new LinearLayoutManager(AudioActivity.this, LinearLayoutManager.VERTICAL, false));


        SectionAdapter sectionAdapter = new SectionAdapter(AudioActivity.this, SectionBeanArrayList);
        mainList.setAdapter(sectionAdapter);


        mainList.addOnItemTouchListener(new RecyclerTouchListener(AudioActivity.this, mainList,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        binding.SelectTypeTxt.setText(SectionBeanArrayList.get(position).getGrade_name() + "" + SectionBeanArrayList.get(position).getSection_name());
                        SectionId = SectionBeanArrayList.get(position).getGrade_id();


                        dialog.dismiss();

                    }


                    @Override
                    public void onLongClick(View view, int position) {
                        //dialogAlert(position);

                    }
                }));


        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();

    }

    private void Section() {
        SectionBeanArrayList = new ArrayList<>();


        AndroidNetworking.get(URLHelper.section)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                SectionBean sectionBean = new SectionBean();
                                sectionBean.setGrade_id(jsonObject.getInt("grade_id"));
                                sectionBean.setGrade_name(jsonObject.getString("grade_name"));
                                sectionBean.setSection_id(jsonObject.getInt("section_id"));
                                sectionBean.setSection_name(jsonObject.getString("section_name"));
                                sectionBean.setSection_visibility(jsonObject.getBoolean("section_visibility"));
                                SectionBeanArrayList.add(sectionBean);


                            }


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

    private void openWhatsApp() {
        String smsNumber = "7418601463"; // E164 format without '+' sign
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
        sendIntent.setPackage("com.whatsapp");
       /* if (sendIntent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
            return;
        }*/
        startActivity(sendIntent);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        AudioDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "";
        binding.DateTxt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + "");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String am_pm = "";
        Calendar datetime = Calendar.getInstance();

        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";

        //  ((Button)getActivity().findViewById(R.id.btnEventStartTime)).setText( strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm );

            Time = hourOfDay + ":" + minute;


            binding.timeTxt.setText(strHrsToShow + ":" + datetime.get(Calendar.MINUTE) + " " + am_pm);

    }

    private void setNormalPicker() {
        Calendar now = getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(YEAR), // Initial year selection
                now.get(MONTH), // Initial month selection
                now.get(DAY_OF_MONTH) // Inital day selection
        );
// If you're calling this from a support Fragment
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
        int color = ContextCompat.getColor(AudioActivity.this, R.color.green);
        Calendar cal1 = Calendar.getInstance();
        dpd.setAccentColor(color);
        dpd.setMinDate(cal1);
    }

    private void setTimerPicker() {
        Calendar now = Calendar.getInstance();

        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        int color = ContextCompat.getColor(AudioActivity.this, R.color.green);
        tpd.setAccentColor(color);
        tpd.enableSeconds(true);
        tpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }
}
