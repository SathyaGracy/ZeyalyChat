package com.zeyalychat.com.adpter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zeyalychat.com.Database.DatabaseHelper;
import com.zeyalychat.com.Database.UserInfoDB;
import com.zeyalychat.com.R;
import com.zeyalychat.com.activity.MainActivity;
import com.zeyalychat.com.databinding.MultiLogRowBinding;
import com.zeyalychat.com.session.Session;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MultiloginAdapter extends RecyclerView.Adapter<MultiloginAdapter.ViewHolder> {
    public ArrayList<UserInfoDB> mDataset;
    private Activity mContext;
    Dialog dialog;
    MultiLogRowBinding binding = null;
    Session session;
    DatabaseHelper databaseHelper;


    public MultiloginAdapter(Activity mContext, ArrayList<UserInfoDB> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;
        session = new Session(mContext);
        dialog = new Dialog(mContext);
        databaseHelper = new DatabaseHelper(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.multi_log_row, parent, false);
        //   itemView.getLayoutParams().width = (int) (getScreenWidth() / 3);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        UserInfoDB userInfo = mDataset.get(position);
        if (position == 0) {
            binding.v1.setVisibility(View.VISIBLE);
        } else {
            binding.v1.setVisibility(View.GONE);
        }
      Picasso.with(mContext).load(userInfo.getImage()).into(binding.profile);
        binding.nameTxt.setText(userInfo.getName());
        // if(position==0){
        binding.removeAccount.setVisibility(View.VISIBLE);
        // }

        binding.navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   session.setKEY_AccountActive(userInfo.getUserId(),false);

                if (mContext instanceof MainActivity) {
                    ((MainActivity) mContext).itemClick(position);
                }

            }
        });
        binding.removeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  session.setKEY_AccountActive(userInfo.getId(),false);

                if (mContext instanceof MainActivity) {
                    ((MainActivity) mContext).callRemoveAccount(userInfo.getUserId());
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;


        public ViewHolder(MultiLogRowBinding binding) {
            super(binding.getRoot());

        }
    }


}

