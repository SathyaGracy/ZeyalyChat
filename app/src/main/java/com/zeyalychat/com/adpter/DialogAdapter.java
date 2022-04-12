package com.zeyalychat.com.adpter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zeyalychat.com.R;
import com.zeyalychat.com.bean.StringBean;
import com.zeyalychat.com.databinding.DialogListRowBinding;

import java.util.ArrayList;


public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolder> {
    public ArrayList<StringBean> mDataset;
    private Activity mContext;
    int row_index = 0;
    DialogListRowBinding binding;


    public DialogAdapter(Activity mContext, ArrayList<StringBean> myDataset) {
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
       binding.textDate.setText(mDataset.get(position).getText());



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

