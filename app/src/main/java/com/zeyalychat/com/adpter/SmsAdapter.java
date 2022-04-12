package com.zeyalychat.com.adpter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zeyalychat.com.R;
import com.zeyalychat.com.bean.SmsBean;
import com.zeyalychat.com.databinding.AudioRowBinding;

import java.util.ArrayList;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {
    public ArrayList<SmsBean> mDataset;
    private Activity mContext;

    AudioRowBinding binding = null;


    public SmsAdapter(Activity mContext, ArrayList<SmsBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.audio_row, parent, false);

        return new ViewHolder(binding);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        SmsBean smsBean = mDataset.get(position);
        binding.TitleTxt.setText(smsBean.getContent());
        binding.Descriptionname.setText(smsBean.getReason());



    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(AudioRowBinding binding) {
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