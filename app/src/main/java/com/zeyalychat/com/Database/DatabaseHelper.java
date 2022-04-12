package com.zeyalychat.com.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "classmate_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(UserInfoDB.CREATE_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + UserInfoDB.TABLE_NAME);


        // Create tables again
        onCreate(db);
    }

    public long insertUser(String UserID, String image, int entry, String name, int status, String token) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(UserInfoDB.COLUMN_UserID, UserID);
        values.put(UserInfoDB.COLUMN_Image, image);
        values.put(UserInfoDB.COLUMN_entry, entry);
        values.put(UserInfoDB.COLUMN_name, name);
        values.put(UserInfoDB.COLUMN_status, status);
        values.put(UserInfoDB.COLUMN_token, token);
        // insert row
        long id = db.insert(UserInfoDB.TABLE_NAME, null, values);
        System.out.println("db inset id------------" + id);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }







    public ArrayList<UserInfoDB> getAllUserList() {
        ArrayList<UserInfoDB> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + UserInfoDB.TABLE_NAME + " ORDER BY " +
                UserInfoDB.COLUMN_ID + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserInfoDB userinfo = new UserInfoDB();
                userinfo.setId(cursor.getInt(cursor.getColumnIndex(UserInfoDB.COLUMN_ID)));
                userinfo.setUserId(cursor.getString(cursor.getColumnIndex(UserInfoDB.COLUMN_UserID)));
                userinfo.setImage(cursor.getString(cursor.getColumnIndex(UserInfoDB.COLUMN_Image)));
                userinfo.setToken(cursor.getString(cursor.getColumnIndex(UserInfoDB.COLUMN_token)));
                userinfo.setName(cursor.getString(cursor.getColumnIndex(UserInfoDB.COLUMN_name)));
                userinfo.setStatus(cursor.getInt(cursor.getColumnIndex(UserInfoDB.COLUMN_status)));
                userinfo.setEntry(cursor.getInt(cursor.getColumnIndex(UserInfoDB.COLUMN_entry)));


                notes.add(userinfo);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }




    public int updateUserEntry(UserInfoDB userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(UserInfoDB.COLUMN_entry, userInfo.getEntry());

        values.put(UserInfoDB.COLUMN_status, userInfo.getUserId());


        // updating row
        return db.update(UserInfoDB.TABLE_NAME, values, UserInfoDB.COLUMN_UserID + " = ?",
                new String[]{String.valueOf(userInfo.getUserId())});
    }
    public int updateUserStatus(UserInfoDB userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(UserInfoDB.COLUMN_status, userInfo.getStatus());

        values.put(UserInfoDB.COLUMN_status, userInfo.getUserId());


        // updating row
        return db.update(UserInfoDB.TABLE_NAME, values, UserInfoDB.COLUMN_UserID + " = ?",
                new String[]{String.valueOf(userInfo.getUserId())});
    }

    public void deleteAllUserDetaill() {
        SQLiteDatabase db = this.getWritableDatabase();
        // db.delete(TABLE_NAME,null,null);
        db.execSQL("delete from " + UserInfoDB.TABLE_NAME);
        //  db.execSQL("TRUNCATE table" + TranscationHistoryInfo.TABLE_NAME);
        db.close();
    }
    public void delete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
       // db.execSQL("delete from "+UserInfoDB.TABLE_NAME+" where Google='"+id+"'");
       // db.execSQL("delete from "+UserInfoDB.TABLE_NAME+" where UserInfoDB.COLUMN_UserID='"+id+"'");
        db.delete(UserInfoDB.TABLE_NAME, UserInfoDB.COLUMN_UserID +"=?", new String[] {id});
        db.close();
    }

    public UserInfoDB getUser(String userID) {

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + UserInfoDB.TABLE_NAME
                + " WHERE " + UserInfoDB.COLUMN_UserID + " = " + userID;
        Cursor cursor = db.rawQuery(sql, null);
        UserInfoDB userinfo = new UserInfoDB();

        // Read data, I simplify cursor in one line
        if (cursor.moveToFirst()) {

            // Get imageData in byte[]. Easy, right?
            userinfo.setId(cursor.getInt(cursor.getColumnIndex(UserInfoDB.COLUMN_ID)));
            userinfo.setUserId(cursor.getString(cursor.getColumnIndex(UserInfoDB.COLUMN_UserID)));
            userinfo.setImage(cursor.getString(cursor.getColumnIndex(UserInfoDB.COLUMN_Image)));
            userinfo.setToken(cursor.getString(cursor.getColumnIndex(UserInfoDB.COLUMN_token)));
            userinfo.setName(cursor.getString(cursor.getColumnIndex(UserInfoDB.COLUMN_name)));
            userinfo.setStatus(cursor.getInt(cursor.getColumnIndex(UserInfoDB.COLUMN_status)));
            userinfo.setEntry(cursor.getInt(cursor.getColumnIndex(UserInfoDB.COLUMN_entry)));
        }
        cursor.close();
        db.close();
        return userinfo;
    }

    public boolean CheckIsDataAlreadyInDBorNot(String id) {

        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + UserInfoDB.TABLE_NAME + " where " + UserInfoDB.COLUMN_UserID + " = " + id;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

}
