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
import com.zeyalychat.com.bean.AttendanceBean;
import com.zeyalychat.com.databinding.AttendanceRowBinding;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    public ArrayList<AttendanceBean> mDataset;
    private Activity mContext;

    AttendanceRowBinding binding = null;


    public AttendanceAdapter(Activity mContext, ArrayList<AttendanceBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.attendance_row, parent, false);

        return new ViewHolder(binding);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        AttendanceBean attendanceBean = mDataset.get(position);
        if(attendanceBean.getMorning_session().equalsIgnoreCase("1")){
            binding.morningsessiontxt.setText("Present");
            binding.morningsessiontxt.setTextColor(mContext.getResources().getColor(R.color.green));

        }else {
            binding.morningsessiontxt.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.morningsessiontxt.setText("Not Taken");
        }

        if(attendanceBean.getAfternoon_session().equalsIgnoreCase("1")){
            binding.afternoonsessionTxt.setTextColor(mContext.getResources().getColor(R.color.green));
            binding.afternoonsessionTxt.setText("Present");

        }else {
            binding.afternoonsessionTxt.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.afternoonsessionTxt.setText("Not Taken");
        }

        binding.nameTxt.setText(attendanceBean.getName());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(AttendanceRowBinding binding) {
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