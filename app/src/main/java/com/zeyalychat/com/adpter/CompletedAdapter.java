package com.zeyalychat.com.adpter;


import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zeyalychat.com.R;
import com.zeyalychat.com.bean.CompletedBean;
import com.zeyalychat.com.databinding.FinishedRowBinding;

import java.util.ArrayList;

public class CompletedAdapter extends RecyclerView.Adapter<CompletedAdapter.ViewHolder> {
    public ArrayList<CompletedBean> mDataset;
    private Activity mContext;

    FinishedRowBinding binding = null;


    public CompletedAdapter(Activity mContext, ArrayList<CompletedBean> mDataset) {
        this.mDataset = mDataset;
        this.mContext = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.finished_row, parent, false);

        return new ViewHolder(binding);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        CompletedBean completedBean=mDataset.get(position);
        Picasso.with(mContext).load(completedBean.getStudent_profile()).into(binding.profile);
        binding.nameTxt.setText(completedBean.getStudent_name());
        binding.fileurl.setText(completedBean.getFile_name());






    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(FinishedRowBinding binding) {
            super(binding.getRoot());


        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
  /* public class ViewHolder extends RecyclerView.ViewHolder {
       CatagoryLayoutBinding binding;

       public ViewHolder(CatagoryLayoutBinding binding) {
           super(binding.getRoot());
           this.binding = binding;
       }


   }*/


}