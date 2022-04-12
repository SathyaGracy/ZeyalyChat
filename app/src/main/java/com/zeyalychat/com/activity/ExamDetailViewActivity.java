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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.zeyalychat.com.R;
import com.zeyalychat.com.databinding.DetailExamBinding;
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

public class ExamDetailViewActivity extends AppCompatActivity implements View.OnClickListener {
    DetailExamBinding binding;
    Session session;
    String ExamId, SectionId;
    private static final int RESULT_LOAD_IMAGE = 104;
    Bitmap bitmap = null;
    File file;
    String Url_web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.detail_exam);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }
        Intent intent = getIntent();
        ExamId = intent.getStringExtra("id");
      SectionId = intent.getStringExtra("SectionId");

        intView();

    }

    @SuppressLint("RestrictedApi")
    private void intView() {
        session = new Session(ExamDetailViewActivity.this);
        // binding.recyclerview.setLayoutManager(new LinearLayoutManager(ExamCreate.this, LinearLayoutManager.VERTICAL, false));
        onItemClickListener();
        ExamActivity(SectionId, ExamId);
        binding.calculatorImg.setImageDrawable(ContextCompat.getDrawable(ExamDetailViewActivity.this, R.mipmap.calculator));
        //  binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.purple));
        binding.cardView.setBackground(getResources().getDrawable(R.drawable.background_purple_lite));
        binding.calculatorImg.setColorFilter(ContextCompat.getColor(ExamDetailViewActivity.this, R.color.purple), PorterDuff.Mode.SRC_IN);


    }

    private void onItemClickListener() {
        binding.backArraow.setOnClickListener(this);
        binding.finishedRl.setOnClickListener(this);
        binding.unfinishedRl.setOnClickListener(this);
        binding.fileUpload.setOnClickListener(this);
        binding.fileUploadTheme.setOnClickListener(this);
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
                    Intent viewfile = new Intent(ExamDetailViewActivity.this, ViewFileActivity.class);
                    viewfile.putExtra("url", Url_web);
                    viewfile.putExtra("name", "");
                    startActivity(viewfile);
                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArraow:
                finish();
                break;
            case R.id.finishedRl:
                Intent intent = new Intent(ExamDetailViewActivity.this, CompletedActivity.class);
                intent.putExtra("id", ExamId);
                intent.putExtra("status", "exam");
                System.out.println("Exam id"+ExamId);

                startActivity(intent);

                break;
            case R.id.unfinishedRl:
                Intent intent1 = new Intent(ExamDetailViewActivity.this, PendingActivity.class);
                intent1.putExtra("id", ExamId);
                intent1.putExtra("status", "exam");
                startActivity(intent1);
                break;
            case R.id.fileUploadTheme:
                try {
                    if (ActivityCompat.checkSelfPermission(ExamDetailViewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ExamDetailViewActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT_LOAD_IMAGE);
                    } else {
                      /*  Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);*/

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        galleryIntent.setType("*/*");
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.fileUpload:
                if(file!=null){
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
                        String   pathToFile = data.getDataString();
                        //  String temp = data.getStringExtra("path");
                        Log.d("Files Fragment: ", pathToFile);
                      //  file = new File(pathToFile);
                        file = new File(getFilePathForN(selectedImage,this));
                        System.out.println("file------------->"+file);
                         binding.fileUploadTheme.setVisibility(GONE);
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        if(bitmap!=null) {
                             binding.fileImage.setImageBitmap(bitmap);
                            binding.chsfile.setText("File Uploaded");

                        }else {
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
    private void ExamActivity(String sectionId, String ExamID) {

        AndroidNetworking.get(URLHelper.exam + "?section_id=" + sectionId + "&id=" + ExamID)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        System.out.println("json---exam-------->" + jsonObject.toString());
                        try {
                          jsonObject.getString("id");
                            jsonObject.getString("section");


                            JSONObject type = jsonObject.getJSONObject("type");
                            JSONObject subject = jsonObject.getJSONObject("subject");
                            type.getString("id");
                            type.getString("is_sys");
                            type.getString("name");

                            subject.getString("description");

                            subject.getString("type");

                            binding.Syllabusname.setText(jsonObject.getString("name"));
                            binding.date.setText(jsonObject.getString("exam_date"));
                            binding.time.setText(jsonObject.getString("exam_date"));
                            binding.mark.setText(jsonObject.getString("min_mark") + "/" + jsonObject.getString("max_mark"));
                            binding.SubTxt.setText(subject.getString("name"));
                            binding.examtype.setText(type.getString("name"));
                            JSONArray attachments=jsonObject.getJSONArray("attachments");
                            if(attachments.length()>0) {
                                binding.browser.setVisibility(VISIBLE);
                                JSONObject url=attachments.getJSONObject(0);
                                binding.webview.getSettings().setLoadsImagesAutomatically(true);
                                binding.webview.getSettings().setJavaScriptEnabled(true);
                                binding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                                binding.webview.loadUrl(url.getString("url"));
                                Url_web=url.getString("url");
                            }else {
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

      /*  String url,value = "";
        if(status.equalsIgnoreCase("exam")) {
            binding.statustitle.setText("Finished");
            url=URLHelper.examstatuscompleted+ Id;
            value="question_attachments";

        }else {
            value="homework_attachments";ur
            binding.statustitle.setText("Completed");
            url=URLHelper.homeworkstatuscompleted+Id;
        }*/
       //file = new File("");
        AndroidNetworking.upload(URLHelper.examupload+ExamId)
                .addMultipartFile("question_attachments", file)
                .addHeaders("Content-Type", "multipart/form-data")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        System.out.println("Erroe");
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                        System.out.println("json-----exam------>" + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(ExamDetailViewActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        System.out.println("error value" + error);
                        System.out.println("error value" + error.getErrorCode());
                    }
                });
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

