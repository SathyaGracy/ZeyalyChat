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
import com.zeyalychat.com.bean.LeaveBean;
import com.zeyalychat.com.databinding.LeaveRowBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.ViewHolder> {
    public ArrayList<LeaveBean> mDataset;
    private Activity mContext;

    LeaveRowBinding binding = null;


    public LeaveAdapter(Activity mContext, ArrayList<LeaveBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.leave_row, parent, false);

        return new ViewHolder(binding);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        LeaveBean leaveBean = mDataset.get(position);
        binding.TypeTxt.setText(leaveBean.getType());
        binding.ReasonTxt.setText(leaveBean.getReason());

        int[] androidColors = mContext.getResources().getIntArray(R.array.random);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        binding.TypeTxt.setTextColor(randomAndroidColor);
        SimpleDateFormat format = new SimpleDateFormat("EEE,dd-MMM-yyyy");
        Date date = new Date();
        String dateTime = format.format(date);
        System.out.println("Current Date Time : " + dateTime);
        binding.DateTxt.setText(dateTime);

        /*if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.DateLayout.setVisibility(View.VISIBLE);
            binding.FromToLayout.setVisibility(View.GONE);
            binding.DateTxt.setText(dateTime);
        } else {
            binding.DateLayout.setVisibility(View.GONE);
            binding.FromToLayout.setVisibility(View.VISIBLE);
            binding.FromDateTxt.setText(dateTime);
            binding.ToDateTxt.setText(dateTime);
        }
*/

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(LeaveRowBinding binding) {
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