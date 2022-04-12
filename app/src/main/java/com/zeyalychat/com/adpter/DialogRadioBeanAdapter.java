package com.zeyalychat.com.adpter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zeyalychat.com.R;
import com.zeyalychat.com.bean.SectionBean;
import com.zeyalychat.com.databinding.DialogRadioBinding;

import java.util.ArrayList;


public class DialogRadioBeanAdapter extends RecyclerView.Adapter<DialogRadioBeanAdapter.ViewHolder> {
    public ArrayList<SectionBean> mDataset;
    private Activity mContext;
    int row_index = 0;
    int last_index = 0;
    DialogRadioBinding binding;


    public DialogRadioBeanAdapter(Activity mContext, ArrayList<SectionBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.dialog_radio, parent, false);

        return new ViewHolder(binding);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if(!mDataset.get(position).getGrade_name().equalsIgnoreCase("")) {
            binding.title.setText(mDataset.get(position).getGrade_name()
                    + " " + mDataset.get(position).getSection_name());
        }else {
            binding.title.setText( mDataset.get(position).getSection_name());
        }
        if(mDataset.get(position).getSelect()){
            binding.unselect.setVisibility(View.GONE);
            binding.select.setVisibility(View.VISIBLE);
        }else {
            binding.unselect.setVisibility(View.VISIBLE);
            binding.select.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(DialogRadioBinding binding) {
            super(binding.getRoot());


        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}

