package com.zeyalychat.com.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.zeyalychat.com.R;
import com.zeyalychat.com.adpter.DialogAdapter;
import com.zeyalychat.com.adpter.SectionAdapter;
import com.zeyalychat.com.bean.SectionBean;
import com.zeyalychat.com.bean.StringBean;
import com.zeyalychat.com.databinding.CreateHomeBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.TransistionAnimation;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class HomeWorkCreate extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    CreateHomeBinding binding;
    ArrayList<StringBean> HomeWorkTypeArrayList;
    ArrayList<SectionBean> SectionBeanArrayList;
    ArrayList<StringBean> SubjectArraylist;
    int SelectTypeId, SectionId, SubjectID;

    String CompletionDate = "";
    Bitmap bitmap = null;
    File file;
    private static final int SELECT_FILE = 12;
    Session session;
    private static final String LOG_TAG = "Text API";
    private static final int RESULT_LOAD_IMAGE = 104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.create_home);
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

    @SuppressLint("RestrictedApi")
    private void intView() {
        session = new Session(HomeWorkCreate.this);
        onItemClickListener();
        HomeWorkType();
        Section();
        Subject();
    }

    private void onItemClickListener() {
        binding.backArraow.setOnClickListener(this);
        binding.TypeLayout.setOnClickListener(this);
        binding.ToLayout.setOnClickListener(this);
        binding.SubjectLayout.setOnClickListener(this);
        binding.CompletionDateLayout.setOnClickListener(this);
        binding.submit.setOnClickListener(this);
        binding.fileUpload.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArraow:
                finish();
                break;
            case R.id.TypeLayout:
                Dialog("HomeWork Type", 0);
                break;
            case R.id.ToLayout:
                Dialog("Section", 1);
                break;
            case R.id.SubjectLayout:
                Dialog("Subject", 2);
                break;
            case R.id.CompletionDateLayout:
                setNormalPicker();
                break;
            case R.id.submit:
                if (file != null)
                    CreateHomework();
                break;
            case R.id.fileUpload:
                try {
                    if (ActivityCompat.checkSelfPermission(HomeWorkCreate.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(HomeWorkCreate.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT_LOAD_IMAGE);
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
                        file = new File(getFilePathForN(selectedImage,this));
                        System.out.println("file------------->"+file);
                        binding.fileUploadTheme.setVisibility(GONE);
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        if(bitmap!=null) {
                            binding.fileImage.setImageBitmap(bitmap);
                            binding.chsfile.setText(getText(R.string.chooseFile));
                        }else {
                            binding.fileUploadTheme.setVisibility(VISIBLE);
                            binding.chsfile.setText("File Uploaded");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                binding.fileUploadTheme.setVisibility(VISIBLE);
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
                Log.e(LOG_TAG, e.toString());
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
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
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
        int color = ContextCompat.getColor(HomeWorkCreate.this, R.color.green);
        Calendar cal1 = Calendar.getInstance();
        dpd.setAccentColor(color);
        dpd.setMinDate(cal1);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        CompletionDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "";
        binding.CompletionDateTxt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + "");
    }

    private void Dialog(String heading, int val) {
        final Dialog dialog = new Dialog(HomeWorkCreate.this);
        DialogAdapter dialogAdapter;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_list);

        TextView Heading = dialog.findViewById(R.id.Heading);
        Heading.setText(heading);

        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout searchRL = dialog.findViewById(R.id.searchRL);
        searchRL.setVisibility(View.GONE);
        RecyclerView mainList = dialog.findViewById(R.id.mainList);
        mainList.setLayoutManager(new LinearLayoutManager(HomeWorkCreate.this, LinearLayoutManager.VERTICAL, false));


        if (val == 0) {
            dialogAdapter = new DialogAdapter(HomeWorkCreate.this, HomeWorkTypeArrayList);
            mainList.setAdapter(dialogAdapter);
        } else if (val == 1) {
            SectionAdapter sectionAdapter = new SectionAdapter(HomeWorkCreate.this, SectionBeanArrayList);
            mainList.setAdapter(sectionAdapter);
        } else if (val == 2) {
            DialogAdapter subjectAdapter = new DialogAdapter(HomeWorkCreate.this, SubjectArraylist);
            mainList.setAdapter(subjectAdapter);
        }


        mainList.addOnItemTouchListener(new RecyclerTouchListener(HomeWorkCreate.this, mainList,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if (val == 0) {
                            binding.SelectTypeTxt.setText(HomeWorkTypeArrayList.get(position).getText());
                            SelectTypeId = HomeWorkTypeArrayList.get(position).getId();
                        } else if (val == 1) {
                            binding.SelectSectionTxt.setText(SectionBeanArrayList.get(position).getGrade_name() + " " + SectionBeanArrayList.get(position).getSection_name());
                            SectionId = SectionBeanArrayList.get(position).getSection_id();
                        } else if (val == 2) {
                            binding.SelectSubjectTxt.setText(SubjectArraylist.get(position).getText());
                            SubjectID = SubjectArraylist.get(position).getId();
                        }
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

    private void HomeWorkType() {
        HomeWorkTypeArrayList = new ArrayList<>();

        AndroidNetworking.get(URLHelper.homeworktype)
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

                                StringBean stringBean = new StringBean();
                                stringBean.setId(jsonObject.getInt("id"));
                                stringBean.setText(jsonObject.getString("name"));


                                HomeWorkTypeArrayList.add(stringBean);
                            }
                           /* DialogAdapter dialogAdapter = new HomeWorkAdapter(HomeWorkCreate.this, SessionArrayList);
                            binding.recyclerview.setAdapter(homeWorkAdapter);*/


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

    private void Subject() {
        SubjectArraylist = new ArrayList<>();

        AndroidNetworking.get(URLHelper.subject)
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

                                StringBean stringBean = new StringBean();
                                stringBean.setId(jsonObject.getInt("id"));
                                stringBean.setText(jsonObject.getString("name"));


                                SubjectArraylist.add(stringBean);
                            }
                           /* DialogAdapter dialogAdapter = new HomeWorkAdapter(HomeWorkCreate.this, SessionArrayList);
                            binding.recyclerview.setAdapter(homeWorkAdapter);*/


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

    private void CreateHomework() {
        AndroidNetworking.upload(URLHelper.homwwork)
                .addMultipartParameter("data", jsonMake().toString())
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
                        Toast.makeText(HomeWorkCreate.this, "Error!", Toast.LENGTH_SHORT).show();
                        System.out.println("error value" + error.getErrorBody());
                        System.out.println("error value" + error.getErrorCode());
                    }
                });
    }


    private JSONObject jsonMake() {

        JSONObject form = new JSONObject();


        try {

            form.put("type", SelectTypeId);
            form.put("title", binding.TitleTxt.getText().toString());
            form.put("content", binding.DescreptionTxt.getText().toString());

            form.put("section_id", SectionId);
            form.put("complete_by", CompletionDate);
            form.put("subject_id", SubjectID);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("form json" + form.toString());
        return form;
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}

