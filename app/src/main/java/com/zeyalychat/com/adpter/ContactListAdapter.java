package com.zeyalychat.com.adpter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zeyalychat.com.R;
import com.zeyalychat.com.bean.ContactBean;
import com.zeyalychat.com.databinding.ContactListAdapterBinding;


import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    public ArrayList<ContactBean> mDataset;
    private Activity mContext;
    Dialog dialog;

    ContactListAdapterBinding binding = null;

    public ContactListAdapter(Activity mContext, ArrayList<ContactBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.contact_list_adapter, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ContactBean contactBean=mDataset.get(position);
        Picasso.with(mContext).load(contactBean.getPicture_url()).into(binding.profile);
        binding.headingTxt.setText(contactBean.getName());


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {




        public ViewHolder(ContactListAdapterBinding binding) {
            super(binding.getRoot());


        }
    }


}


