package com.zeyalychat.com.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zeyalychat.com.activity.LoginActivity;

public class Session {
    Context cntx;
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public final String KEY_Auth = "autho";
    public final String Entry = "entry";
    public final String account_active = "account";
    public final String CurrentEntry = "currentEntry";
    public final String RoleName = "rolename";
    public final String language = "lang";

    public final int KEYuser_ID_entry = 0;


    public final String In = "in";


    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        this.cntx = cntx;
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        editor = prefs.edit();
    }


    public void setKEYAuth(String userID) {
        editor.putString(KEY_Auth, userID);
        editor.apply();
    }

    public String getKEYAuth() {
        String userID = prefs.getString(KEY_Auth, "0");
        return userID;
    }

    public void setKEYlanguage(String userID) {
        editor.putString(language, userID);
        editor.apply();
    }

    public String getKEYlanguage() {
        String userID = prefs.getString(language, "en");
        return userID;
    }


    public void setKEY_MULTILOG(String Id, String userID) {
        editor.putString(Id + "log", userID);
        editor.apply();
    }

    public String getKEYMULTILOG(String Id) {
        String userID = prefs.getString(Id, "0");
        return userID;
    }

    public void setKEY_RoleName(String Id, String userID) {
        editor.putString(Id + "RoleName", userID);
        editor.apply();
    }

    public String getKEYRoleName(String Id) {
        String userID = prefs.getString(Id, "0");
        return userID;
    }

    public void setKEY_AccountActive(String Id, Boolean userID) {
        editor.putBoolean(Id, userID);
        editor.apply();
    }

    public Boolean getKEY_AccountActive(String Id) {
        Boolean userID = prefs.getBoolean(Id, false);
        return userID;
    }

    public void setEntry(String userID) {
        editor.putString(Entry, userID);
        editor.apply();
    }

    public String getEntry() {
        String userID = prefs.getString(Entry, "0");
        return userID;
    }

    public void setCurrentEntry(String userID) {
        editor.putString(CurrentEntry, userID);
        editor.apply();
    }

    public String getCurrentEntry() {
        String userID = prefs.getString(CurrentEntry, "0");
        return userID;
    }
   public void onDestroy(){
       editor.clear().apply();
       Intent intent = new Intent(cntx, LoginActivity.class);
       cntx.startActivity(intent);
    }

}
