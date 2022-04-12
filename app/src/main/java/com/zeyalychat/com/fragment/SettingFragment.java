package com.zeyalychat.com.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zeyalychat.com.R;
import com.zeyalychat.com.activity.MainActivity;
import com.zeyalychat.com.activity.ProfileActivity;
import com.zeyalychat.com.adpter.DialogAdapter;
import com.zeyalychat.com.bean.StringBean;
import com.zeyalychat.com.databinding.SettingBinding;
import com.zeyalychat.com.onItemClickListner.RecyclerTouchListener;
import com.zeyalychat.com.session.Session;

import java.util.ArrayList;
import java.util.Locale;

public class SettingFragment extends Fragment implements View.OnClickListener {
    SettingBinding binding;
    ArrayList<StringBean> languageArrayList;
    Session session;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //   View view = inflater.inflate(R.layout.fragment_bronze, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.setting, container, false);


        initView();
        // getActivity().registerReceiver(broadcastReceiver, new IntentFilter("INTERNET_LOST"));

        return binding.getRoot();
    }

    private void initView() {
        session = new Session(getActivity());
        languageArrayList = new ArrayList<>();
        binding.contactLayout.setOnClickListener(this);
        binding.ltRLan.setOnClickListener(this);
        binding.logLayout.setOnClickListener(this);
        binding.nameTxt.setText(MainActivity.userInfoArrayList.get(0).getName());
        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.numberTxt.setText(MainActivity.userInfoArrayList.get(0).getGrade_name() + " " + MainActivity.userInfoArrayList.get(0).getSection_name());
        } else {
            binding.numberTxt.setText(MainActivity.userInfoArrayList.get(0).getRole_name());
        }

        Picasso.with(getActivity()).load(MainActivity.userInfoArrayList.get(0).getProfile_image_url()).into(binding.profile);
        setLocale(getActivity(), session.getKEYlanguage());
        if (session.getKEYlanguage().equalsIgnoreCase("ta"))
            binding.languageTxt.setText(getResources().getString(R.string.Language) + " (" + getResources().getString(R.string.tamil) + ") ");
        else
            binding.languageTxt.setText(getResources().getString(R.string.Language) + " (" + getResources().getString(R.string.english) + ") ");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_layout:
                Intent mainIntent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.lt_R_lan:
                Dialog();
                break;
            case R.id.log_layout:

                break;
        }

    }

    private void Dialog() {
        languageArrayList = new ArrayList<>();
        StringBean stringBean = new StringBean();
        stringBean.setText(getString(R.string.tamil));
        stringBean.setId(1);
        stringBean.setCode("ta");
        languageArrayList.add(stringBean);
        StringBean stringBean1 = new StringBean();
        stringBean1.setText(getString(R.string.english));
        stringBean1.setId(2);
        stringBean1.setCode("en");
        languageArrayList.add(stringBean1);
        final Dialog dialog = new Dialog(getActivity());
        DialogAdapter dialogAdapter;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_list);

        TextView Heading = dialog.findViewById(R.id.Heading);
        Heading.setText(getString(R.string.Language));

        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        RelativeLayout searchRL = dialog.findViewById(R.id.searchRL);
        searchRL.setVisibility(View.GONE);
        RecyclerView mainList = dialog.findViewById(R.id.mainList);
        mainList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        dialogAdapter = new DialogAdapter(getActivity(), languageArrayList);
        mainList.setAdapter(dialogAdapter);


        mainList.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mainList,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        dialogChange(position);

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

    /* public static void setLocale(Activity activity, String languageCode) {
     *//*Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());*//*
        Resources res = activity.getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale("th");
        res.updateConfiguration(conf, dm);
    }*/
    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

    }

    public void dialogChange(int pos) {
        final Dialog dialog = new Dialog(getActivity());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_alert);
        TextView content = dialog.findViewById(R.id.content);
        content.setText(getResources().getString(R.string.alert_change));

        TextView no = dialog.findViewById(R.id.close);
        TextView yes = dialog.findViewById(R.id.save);

        yes.setText(getResources().getString(R.string.Change));
        no.setText(getResources().getString(R.string.Cancel));

        dialog.show();

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.languageTxt.setText(getResources().getString(R.string.Language) + " (" + languageArrayList.get(pos).getText() + ") ");

                setLocale(getActivity(), languageArrayList.get(pos).getCode());
                session.setKEYlanguage(languageArrayList.get(pos).getCode());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

                dialog.dismiss();
            }
        });

    }

}
