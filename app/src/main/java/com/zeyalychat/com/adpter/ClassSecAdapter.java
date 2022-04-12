package com.zeyalychat.com.adpter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zeyalychat.com.R;
import com.zeyalychat.com.bean.SectionBean;
import com.zeyalychat.com.databinding.DialogListRowBinding;

import java.util.ArrayList;


public class ClassSecAdapter extends RecyclerView.Adapter<ClassSecAdapter.ViewHolder> {
    public ArrayList<SectionBean> mDataset;
    private Activity mContext;
    int row_index = 0;
    DialogListRowBinding binding;


    public ClassSecAdapter(Activity mContext, ArrayList<SectionBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.dialog_list_row, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //if (mDataset.get(position).getSection_visibility()) {
            binding.textDate.setText(mDataset.get(position).getGrade_name());
       /* }else {
            binding.textDate.setText(mDataset.get(position).getGrade_name());
        }*/


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(DialogListRowBinding binding) {
            super(binding.getRoot());


        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}

