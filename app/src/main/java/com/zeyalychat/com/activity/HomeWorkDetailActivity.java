package com.zeyalychat.com.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.zeyalychat.com.R;
import com.zeyalychat.com.databinding.HomeWorkDetailBinding;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HomeWorkDetailActivity extends AppCompatActivity implements View.OnClickListener {
    HomeWorkDetailBinding binding;
    Session session;
    String HomeWorkId;
    private static final int RESULT_LOAD_IMAGE = 104;
    Bitmap bitmap = null;
    File file;
    InputStream inputStream;
    String Url_web="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.home_work_detail);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }
        Intent intent = getIntent();
        HomeWorkId = intent.getStringExtra("HomeWorkId");
        binding.calculatorImg.setImageDrawable(ContextCompat.getDrawable(HomeWorkDetailActivity.this, R.mipmap.calculator));
        //  binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.purple));
        binding.cardView.setBackground(getResources().getDrawable(R.drawable.background_purple_lite));
        binding.calculatorImg.setColorFilter(ContextCompat.getColor(HomeWorkDetailActivity.this, R.color.purple), PorterDuff.Mode.SRC_IN);
        intView();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void intView() {
        session = new Session(HomeWorkDetailActivity.this);
        binding.backArraow.setOnClickListener(this);
        binding.finishedRl.setOnClickListener(this);
        binding.unfinishedRl.setOnClickListener(this);
        binding.fileUpload.setOnClickListener(this);
        binding.fileUploadTheme.setOnClickListener(this);
        binding.webview.setOnClickListener(this);
        System.out.println("getRole_name "+MainActivity.userInfoArrayList.get(0).getRole_name());
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.fileData.setVisibility(VISIBLE);
            binding.bottomLayout.setVisibility(VISIBLE);

        } else {
            binding.fileData.setVisibility(GONE);
            binding.bottomLayout.setVisibility(GONE);
        }
        binding.webview.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_MOVE){

                    return false;
                }

                if (event.getAction()==MotionEvent.ACTION_UP){
                   // startActivity(new Intent(this,Example.class));
                    Intent viewfile = new Intent(HomeWorkDetailActivity.this, ViewFileActivity.class);
                    viewfile.putExtra("url", Url_web);
                    viewfile.putExtra("name", "");
                    startActivity(viewfile);
                }

                return false;
            }
        });
        HomeWork();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArraow:
                finish();
                break;
            case R.id.finishedRl:
                Intent intent = new Intent(HomeWorkDetailActivity.this, CompletedActivity.class);
                intent.putExtra("id", HomeWorkId);
                intent.putExtra("status", "homework");
                System.out.println("Exam id" + HomeWorkId);
                startActivity(intent);

                break;
            case R.id.unfinishedRl:
                Intent intent1 = new Intent(HomeWorkDetailActivity.this, PendingActivity.class);
                intent1.putExtra("id", HomeWorkId);
                intent1.putExtra("status", "homework");
                startActivity(intent1);
                break;
            case R.id.fileUploadTheme:
                try {
                    if (ActivityCompat.checkSelfPermission(HomeWorkDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(HomeWorkDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT_LOAD_IMAGE);
                    } else {
                      /*  Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);*/

                        // Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                      //  @SuppressLint("IntentReset") Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("*/*");
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.fileUpload:
                if (file != null) {
                    uplodefile();
                }

                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            try {

                if (data != null) {
                    try {
                        Uri selectedImage = data.getData();
                        inputStream = getContentResolver().openInputStream(data.getData());

                        /*Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        file = new File(picturePath);
                        binding.fileUploadTheme.setVisibility(GONE);
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        binding.fileImage.setImageBitmap(bitmap);*/
                        String pathToFile = data.getDataString();
                        System.out.println("GetDATA   " + data.getData());
                        //getPath(data.getData());
                        //  String temp = data.getStringExtra("path");
                        Log.d("Files Fragment: ", pathToFile);
                        file = new File(getFilePathForN(selectedImage,this));
                        System.out.println("file------------->" + file);
                        // binding.fileUploadTheme.setVisibility(GONE);
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        if (bitmap != null) {
                            binding.fileImage.setImageBitmap(bitmap);
                            binding.chsfile.setText("File Uploaded");

                        } else {
                            binding.chsfile.setText(getText(R.string.chooseFile));
                            binding.fileUploadTheme.setVisibility(VISIBLE);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                binding.fileUploadTheme.setVisibility(VISIBLE);
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();

            }
        }

    }
    private static String getFilePathForN(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getFilesDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }
    private void HomeWork() {
        String url = "";
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            url = URLHelper.homwwork + "?section_id=" + MainActivity.userInfoArrayList.get(0).getSection_id() + "&id=" + HomeWorkId;
        } else {
            url = URLHelper.homwwork + "?staff_id=" + MainActivity.userInfoArrayList.get(0).getId() + "&section_id=" + MainActivity.userInfoArrayList.get(0).getSection_id() + "&id=" + HomeWorkId;
            ;
        }
        String finalUrl = url;
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        System.out.println("Homework detail" + jsonObject);

                        try {
                            jsonObject.getString("assigned_by");
                            binding.CompletionDateTxt.setText(jsonObject.getString("complete_by"));
                            jsonObject.getString("content");
                            jsonObject.getString("id");
                            binding.TitleTxt.setText(jsonObject.getString("title"));
                            jsonObject.getString("to");
                            jsonObject.getString("to_type");
                            JSONObject section = jsonObject.getJSONObject("section");
                            section.getString("id");
                            section.getString("name");
                            JSONObject subject = jsonObject.getJSONObject("subject");
                            binding.ContentTxt.setText(jsonObject.getString("content"));
                            subject.getString("id");
                            binding.SubjectTxt.setText(subject.getString("name"));
                            subject.getString("type");
                            JSONObject type = jsonObject.getJSONObject("type");
                            type.getString("id");
                            binding.homeworkTypeTxt.setText(type.getString("name"));
                            JSONArray attachments = jsonObject.getJSONArray("attachments");
                            if (attachments.length() > 0) {
                                binding.browser.setVisibility(VISIBLE);
                                JSONObject url = attachments.getJSONObject(0);
                                binding.webview.getSettings().setLoadsImagesAutomatically(true);
                                binding.webview.getSettings().setJavaScriptEnabled(true);
                                binding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                                binding.webview.loadUrl(url.getString("url"));
                                Url_web= url.getString("url");

                            } else {
                                binding.browser.setVisibility(GONE);
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

    private void uplodefile() {

        AndroidNetworking.upload(URLHelper.homeworkupload + HomeWorkId)
                .addMultipartFile("homework_attachments", file)
                .addHeaders("Content-Type", "multipart/form-data")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                        System.out.println("json-----exam------>" + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(HomeWorkDetailActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        System.out.println("error value" + error);
                        System.out.println("error value" + error.getErrorCode());
                    }
                });
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
