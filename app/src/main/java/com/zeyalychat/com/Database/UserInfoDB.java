package com.zeyalychat.com.Database;

public class UserInfoDB {

    public static final String TABLE_NAME = "UserInfoDB";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_UserID = "UserID";
    public static final String COLUMN_Image = "Image";
    public static final String COLUMN_name = "name";
    public static final String COLUMN_status = "status";
    public static final String COLUMN_entry = "entry";
    public static final String COLUMN_token = "token";

    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_entry + " INTEGER,"
                    + COLUMN_token + " TEXT,"
                    + COLUMN_Image + " TEXT,"
                    + COLUMN_name + " TEXT,"
                    + COLUMN_UserID + " TEXT,"
                    + COLUMN_status + " INTEGER,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";


    public UserInfoDB() {
    }

    int id, status, entry;
    String Token, image, name,UserId;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEntry() {
        return entry;
    }

    public void setEntry(int entry) {
        this.entry = entry;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }



    public UserInfoDB(int id, String UserId, String image, String name,
                      int status, int entry, String Token) {
        this.id = id;
        this.UserId = UserId;
        this.image = image;
        this.name = name;
        this.status = status;
        this.entry = entry;
        this.Token = Token;


    }

}
