package com.zeyalychat.com.adpter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zeyalychat.com.R;
import com.zeyalychat.com.bean.StringBean;
import com.zeyalychat.com.databinding.CreateAttendanceRowBinding;

import java.util.ArrayList;


public class CreateAttendanceAdapter extends RecyclerView.Adapter<CreateAttendanceAdapter.ViewHolder> {
    public ArrayList<StringBean> mDataset;
    private Activity mContext;
    int row_index = 0;
    CreateAttendanceRowBinding binding;


    public CreateAttendanceAdapter(Activity mContext, ArrayList<StringBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.create_attendance_row, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
       binding.TextTxt.setText(mDataset.get(position).getText());
       binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked) {

                   mDataset.get(position).setAttendance(true);
                   System.out.println("ID :"+mDataset.get(position).getId()+"-Attendancce-"+mDataset.get(position).getAttendance());
               }else {
                   mDataset.get(position).setAttendance(false);
                   System.out.println("ID :"+mDataset.get(position).getId()+"-Attendancce-"+mDataset.get(position).getAttendance());
               }

           }
       });



    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(CreateAttendanceRowBinding binding) {
            super(binding.getRoot());


        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}

