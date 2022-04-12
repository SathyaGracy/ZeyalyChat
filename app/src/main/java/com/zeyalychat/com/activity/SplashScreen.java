package com.zeyalychat.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.zeyalychat.com.Database.DatabaseHelper;
import com.zeyalychat.com.Database.UserInfoDB;
import com.zeyalychat.com.R;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


public class SplashScreen extends AppCompatActivity {

    Session session;
    DatabaseHelper databaseHelper;
    UserInfoDB userInfoDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
       /* String languageToLoad  = "ta"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());*/
        session = new Session(this);
        setLocale(this, session.getKEYlanguage());
        setContentView(R.layout.splash_layout);
        databaseHelper=new DatabaseHelper(this);
        initView();
        checkFormat();

    }

    private void initView() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ( databaseHelper.getAllUserList().size()>0) {
                    GetUserInfo();
                   /* Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                   // GetUserInfo();*/

                } else {
                    Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();

                }
            }
        }, 3000);
    }
    private void GetUserInfo() {


        userInfoDB=  databaseHelper.getUser(session.getCurrentEntry());
        AndroidNetworking.get(URLHelper.getuserinfo)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", userInfoDB.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }


                    @Override
                    public void onError(ANError error) {
                        // handle error
                        System.out.println(error.getErrorBody());
                        System.out.println(error.getErrorCode());
                        if (error.getErrorCode() == 403) {
                            session.onDestroy();
                            databaseHelper.deleteAllUserDetaill();


                        }
                    }
                });
    }


    private JSONObject checkFormat(){
        JSONObject jobjectmain = new JSONObject();
        JSONObject jobjectsub = new JSONObject();
        JSONObject jobjectsection = new JSONObject();
        JSONObject jobjectsubject = new JSONObject();
        JSONArray jarray=new JSONArray();


        try {
            // {"username":"allformani@gmail.com","password":"test"}
         /*   json.put("username", "sathya@zeyaly.com");
            json.put("password", "1234");*/
            //binding.usernameEdt.getText().toString().trim();
            jobjectsection.put("id","1");
            jobjectsection.put("name","2345");

            jobjectsubject.put("description","fdkjfghsk");
            jobjectsubject.put("id","1");
            jobjectsubject.put("name","sathya");
            jobjectsubject.put("type","sd");

            jobjectsub.put("day","monday");
            jobjectsub.put("from_time","9");
            jobjectsub.put("id","1");
            jobjectsub.put("to_time","10");
            jobjectsub.put("section",jobjectsection);
            jobjectsub.put("subject",jobjectsubject);

            jarray.put(jobjectsub);
            jobjectmain.put("data",jarray);


            System.out.println("object----->"+jobjectmain);


          /*  json.put("username", binding.usernameEdt.getText().toString().trim());
            json.put("password", binding.passwordEdt.getText().toString().trim());
*/

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("try exc"+e);

        }
        return jobjectmain;
    }
    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}
