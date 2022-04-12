package com.zeyalychat.com.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.zeyalychat.com.Database.DatabaseHelper;
import com.zeyalychat.com.Database.UserInfoDB;
import com.zeyalychat.com.R;
import com.zeyalychat.com.adpter.MultiloginAdapter;
import com.zeyalychat.com.bean.UserInfo;
import com.zeyalychat.com.databinding.MainLayoutBinding;
import com.zeyalychat.com.fragment.MessageListFragment;
import com.zeyalychat.com.fragment.TimeLineListFragment;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;
import com.zeyalychat.com.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.Gravity.LEFT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MainLayoutBinding binding;


    Session session;
    public int resultcodevalue = 103;


    public static ArrayList<UserInfo> userInfoArrayList;
    View tabviewhome, tabviewchat, tabviewMap, tabviewprofile;
    TextView tabHomeTxt, tabchatTxt, tabMapTxt, tabprofileTxt;
    ImageView tabHomeimg, tabchatimg, tabMapimg, tabprofileimg;

    ViewPagerAdapter adapter;
    int NAV_DRAWER = 0;
    Dialog dialog;
    RecyclerView dialog_recy_multilog;

    DatabaseHelper databaseHelper;

    ArrayList<UserInfoDB> userInfoArrayListDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_layout);
        initView();
    }

    private void initView() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        session = new Session(this);
        databaseHelper = new DatabaseHelper(this);

        dialog = new Dialog(this);



        binding.navRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        binding.navLayout.setOnClickListener(this);
        binding.headerlogin.setOnClickListener(this);
        binding.filterLayout.setOnClickListener(this);
        binding.filterLayout.setVisibility(View.GONE);

        tabviewhome = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_title_layout, null);
        tabviewchat = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_title_layout, null);
        tabviewMap = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_title_layout, null);
        tabviewprofile = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_title_layout, null);
        tabHomeimg = tabviewhome.findViewById(R.id.icon);
        tabchatimg = tabviewchat.findViewById(R.id.icon);
        tabMapimg = tabviewMap.findViewById(R.id.icon);
        tabprofileimg = tabviewprofile.findViewById(R.id.icon);
        tabHomeTxt = tabviewhome.findViewById(R.id.text);
        tabchatTxt = tabviewchat.findViewById(R.id.text);
        tabMapTxt = tabviewMap.findViewById(R.id.text);
        tabprofileTxt = tabviewprofile.findViewById(R.id.text);
        binding.tab.setupWithViewPager(binding.viewpager);


        userInfoArrayListDB = new ArrayList<>();
        userInfoArrayListDB = databaseHelper.getAllUserList();
        UserInfoDB userInfoDB = databaseHelper.getUser(session.getCurrentEntry());
        binding.nameTxt.setText(userInfoDB.getName());
        binding.title.setText(getResources().getText(R.string.hi) + " " + userInfoDB.getName());


        System.out.println("count ;" + userInfoArrayListDB.size());
        userInfoArrayList = new ArrayList<>();

        if (userInfoArrayListDB.size() > 0) {
            for (int i = 0; i < userInfoArrayListDB.size(); i++) {
                if (userInfoArrayListDB.get(i).getUserId().equalsIgnoreCase(session.getCurrentEntry())) {
                    session.setKEYAuth(userInfoArrayListDB.get(i).getToken());
                    System.out.println(userInfoArrayListDB.get(i).getName()+" : "+userInfoArrayListDB.get(i).getToken());
                    GetUserInfo();
                    break;
                }
            }
        } else {
            this.finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        setupViewPager(binding.viewpager);
        binding.viewpager.setPagingEnabled(false);
        setupTabIcons();

        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int postion = binding.tab.getSelectedTabPosition();
                switch (postion) {
                    case 0:
                        binding.ltRActionbarlayout.setVisibility(View.VISIBLE);

                        binding.filterLayout.setVisibility(View.GONE);
                        tabUnSelected();
                        tabHomeimg.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.mipmap.home));
                        // tabHomeimg.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                        tabHomeimg.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                        // icon_selectHome.setVisibility(VISIBLE);
                        tabHomeTxt.setText(getResources().getText(R.string.home));
                        tabHomeTxt.setTextColor(getResources().getColor(R.color.green));
                        Objects.requireNonNull(binding.tab.getTabAt(0)).setCustomView(tabviewhome);

                        break;

                    case 1:
                        tabUnSelected();
                        binding.ltRActionbarlayout.setVisibility(View.GONE);
                        binding.filterLayout.setVisibility(View.GONE);
                        tabchatimg.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.mipmap.chat));
                        //  tabPassbookimg.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                        //icon_selectPassbook.setVisibility(VISIBLE);
                        tabchatTxt.setText(getResources().getText(R.string.Chat));
                        tabchatTxt.setTextColor(getResources().getColor(R.color.green));
                        tabchatimg.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                        Objects.requireNonNull(binding.tab.getTabAt(1)).setCustomView(tabviewchat);
                        break;

                }
                //do stuff here

            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
             /*   int tabIconColor = ContextCompat.getColor(BuySellActivity.this, R.color.full_textt_color);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);*/
            }
        });

        binding.ltRMenulayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (NAV_DRAWER == 0) {
                        if (binding.drawer != null)
                            binding.drawer.openDrawer(Gravity.LEFT);
                    } else {
                        NAV_DRAWER = 0;
                        if (binding.drawer != null)
                            binding.drawer.closeDrawers();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        drawerCloseLister();

    }

    public void itemClick(int position) {
        dialog.dismiss();

        session.setCurrentEntry(userInfoArrayListDB.get(position).getUserId());
        session.setKEYAuth(userInfoArrayListDB.get(position).getToken());
        System.out.println(userInfoArrayListDB.get(position).getName()+":::"+userInfoArrayListDB.get(position).getToken());
        GetUserInfo();
    }


    public void openNavigation() {
        try {
            if (NAV_DRAWER == 0) {
                if (binding.drawer != null)


                    binding.drawer.openDrawer(LEFT);
            } else {
                NAV_DRAWER = 0;
                if (binding.drawer != null)
                    binding.drawer.closeDrawers();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawerCloseLister() {
        binding.drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {

            }

            @Override
            public void onDrawerOpened(View view) {

            }

            @Override
            public void onDrawerClosed(View view) {
                // your refresh code can be called from here


            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
    }


    private void dialogLogin() {
        System.out.println("Entry:::::" + session.getEntry());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_multilogin);
        dialog_recy_multilog = dialog.findViewById(R.id.dialog_recy_multilog);
        dialog_recy_multilog.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout add_account_layout = dialog.findViewById(R.id.add_account_layout);
        MultiloginAdapter multiloginAdapter = new MultiloginAdapter(MainActivity.this, userInfoArrayListDB);
        dialog_recy_multilog.setAdapter(multiloginAdapter);
        //userInfoArrayList = new ArrayList<>();

    /*    for (int i = 0; i < userInfoArrayListDB.size(); i++) {
            if (userInfoArrayListDB.get(i).getStatus() == 1) {
                session.setKEYAuth(userInfoArrayListDB.get(i).getToken());
                GetUserInfo("dialog");

            }
        }*/
        dialog.show();
        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        add_account_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();

                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent1);
            }
        });

        dialog_recy_multilog.addOnItemTouchListener(new RecyclerTouchListener(this, dialog_recy_multilog,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {


                    }


                    @Override
                    public void onLongClick(View view, int position) {
                        //dialogAlert(position);

                    }
                }));


    }






    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_layout:
                openNavigation();

                break;
            case R.id.headerlogin:
                dialogLogin();
                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultcodevalue && resultCode == RESULT_OK) {
            userInfoArrayListDB.clear();
            userInfoArrayListDB = databaseHelper.getAllUserList();


            if (userInfoArrayListDB.size() > 0) {
                session.setKEYAuth(userInfoArrayListDB.get(0).getToken());
                GetUserInfo();
            } else {
                this.finish();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }


        }
    }

    private void GetUserInfo() {
        userInfoArrayList=new ArrayList<>();
        AndroidNetworking.get(URLHelper.getuserinfo)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getKEYAuth())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("json---userinfo-------->" + response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("role");
                            JSONArray profile = response.getJSONArray("profile");

                            UserInfo userInfo = new UserInfo();
                            userInfo.setId(response.getString("id"));
                            userInfo.setName(response.getString("name"));

                            userInfo.setAuth(session.getKEYAuth());
                            userInfo.setProfile_image_url(response.getString("profile_image_url"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                userInfo.setRole_id(object.getString("id"));
                                userInfo.setRole_name(object.getString("name"));
                            }
                            for (int i = 0; i < profile.length(); i++) {
                                JSONObject object = profile.getJSONObject(i);
                                userInfo.setRole_id(object.getString("id"));
                                userInfo.setRole_name(object.getString("name"));
                            }


                            if (response.has("section")) {

                                JSONObject jsonObject = response.getJSONObject("section");
                                userInfo.setSection_id(jsonObject.getString("section_id"));
                                userInfo.setSection_name(jsonObject.getString("section_name"));
                                userInfo.setGrade_id(jsonObject.getString("grade_id"));
                                userInfo.setGrade_name(jsonObject.getString("grade_name"));
                                userInfo.setSection_visibility(jsonObject.getString("section_visibility"));
                            }
                            userInfoArrayList.add(userInfo);
                            System.out.println("role name:"+userInfo.getRole_name());
                            System.out.println("name:"+response.getString("name"));


                            //  if (from.equalsIgnoreCase("main")) {
                            binding.nameTxt.setText(response.getString("name"));
                            binding.title.setText(getResources().getText(R.string.hi) + " " + response.getString("name"));


                            Picasso.with(MainActivity.this).load(response.getString("profile_image_url")).into(binding.profile);
                            Picasso.with(MainActivity.this).load(response.getString("profile_image_url")).into(binding.prImg);
                            // Picasso.with(MainActivity.this).load(URLHelper.profile+response.getString("id")+"&"+session.getKEYAuth()).into(binding.profile);





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
                            databaseHelper.deleteAllUserDetaill();


                        }
                    }
                });
    }

    @SuppressLint("NewApi")
    private void setupTabIcons() {

        tabHomeimg.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.mipmap.home));
        tabHomeimg.setColorFilter(ContextCompat.getColor(this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
        tabHomeTxt.setText(getResources().getText(R.string.home));
        tabHomeTxt.setTextColor(getResources().getColor(R.color.green));
        Objects.requireNonNull(binding.tab.getTabAt(0)).setCustomView(tabviewhome);

        tabchatimg.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.mipmap.chat_uncheck));
        //  tabPassbookimg.setColorFilter(ContextCompat.getColor(this, R.color.txt_medium_color), android.graphics.PorterDuff.Mode.MULTIPLY);
        tabchatimg.setColorFilter(ContextCompat.getColor(this, R.color.txt_medium_color), android.graphics.PorterDuff.Mode.SRC_IN);
        //  icon_selectPassbook.setVisibility(View.GONE);
        tabchatTxt.setText(getResources().getText(R.string.Chat));
        tabchatTxt.setTextColor(getResources().getColor(R.color.txt_medium_color));
        Objects.requireNonNull(binding.tab.getTabAt(1)).setCustomView(tabviewchat);




    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void tabUnSelected() {

        tabHomeimg.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.mipmap.home_uncheck));
        tabHomeimg.setColorFilter(ContextCompat.getColor(this, R.color.txt_medium_color), android.graphics.PorterDuff.Mode.SRC_IN);
        tabHomeTxt.setText(getResources().getText(R.string.home));
        tabHomeTxt.setTextColor(getResources().getColor(R.color.txt_medium_color));
        Objects.requireNonNull(binding.tab.getTabAt(0)).setCustomView(tabviewhome);

        tabchatimg.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.mipmap.chat_uncheck));
        //  tabPassbookimg.setColorFilter(ContextCompat.getColor(this, R.color.txt_medium_color), android.graphics.PorterDuff.Mode.MULTIPLY);
        tabchatimg.setColorFilter(ContextCompat.getColor(this, R.color.txt_medium_color), android.graphics.PorterDuff.Mode.SRC_IN);
        //  icon_selectPassbook.setVisibility(View.GONE);
        tabchatTxt.setText(getResources().getText(R.string.Chat));
        tabchatTxt.setTextColor(getResources().getColor(R.color.txt_medium_color));
        Objects.requireNonNull(binding.tab.getTabAt(1)).setCustomView(tabviewchat);


    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TimeLineListFragment(), "");
        adapter.addFrag(new MessageListFragment(), "");
        viewPager.setAdapter(adapter);


    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //   getTabView(position);
            return mFragmentTitleList.get(position);

        }


    }

    public void callRemoveAccount(String id) {
        dialog.dismiss();
        Intent intent = new Intent(this, RemoveAccountActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, resultcodevalue);
    }
}
