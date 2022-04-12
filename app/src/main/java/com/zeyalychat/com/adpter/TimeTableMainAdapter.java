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
import com.zeyalychat.com.activity.MainActivity;
import com.zeyalychat.com.bean.TimeTableBean;
import com.zeyalychat.com.databinding.TimetableMainRowBinding;

import java.util.ArrayList;

public class TimeTableMainAdapter extends RecyclerView.Adapter<TimeTableMainAdapter.ViewHolder> {
    public ArrayList<TimeTableBean> mDataset;
    private Activity mContext;
    int row_index = 0;
    TimetableMainRowBinding binding = null;


    public TimeTableMainAdapter(Activity mContext, ArrayList<TimeTableBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.timetable_main_row, parent, false);

        return new ViewHolder(binding);

     /*   View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row_layout, parent, false);
        //   itemView.getLayoutParams().width = (int) (getScreenWidth() / 3);*/
        // return new ViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

    TimeTableBean timeTableBean = mDataset.get(position);


        binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_blue));
       /* binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
        binding.title.setTextColor(mContext.getResources().getColor(R.color.blue));
        binding.tutorname.setTextColor(mContext.getResources().getColor(R.color.blue));
        binding.timefrom.setTextColor(mContext.getResources().getColor(R.color.blue));*/
       // binding.tutorname.setText(timeTableBean.getStaff_name());
        binding.title.setText(timeTableBean.getSubject_name());
        binding.timefrom.setText(timeTableBean.getFrom_time());
        binding.timeto.setText(timeTableBean.getTo_time());



        if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.tutorname.setText(timeTableBean.getStaff_name());
        } else {
            binding.tutorname.setText(timeTableBean.getSection_name());
        }






     /*

        if (position % 6 == 0) {
             binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_purple_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.purple));
            binding.title.setTextColor(mContext.getResources().getColor(R.color.purple));
            binding.tutorname.setTextColor(mContext.getResources().getColor(R.color.purple));
            binding.timeon.setTextColor(mContext.getResources().getColor(R.color.purple));
            binding.tutorname.setText(timeTableBean.getStaff_name());
            binding.title.setText(timeTableBean.getSubject_name());
            binding.timeon.setText(timeTableBean.getFrom_time()+"-"+timeTableBean.getTo_time());
        } else if (position % 6 == 1) {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_shade_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.shade));
            binding.title.setTextColor(mContext.getResources().getColor(R.color.shade));
            binding.tutorname.setTextColor(mContext.getResources().getColor(R.color.shade));
            binding.timeon.setTextColor(mContext.getResources().getColor(R.color.shade));
            binding.tutorname.setText(timeTableBean.getStaff_name());
            binding.title.setText(timeTableBean.getSubject_name());
            binding.timeon.setText(timeTableBean.getFrom_time()+"-"+timeTableBean.getTo_time());
        } else if (position % 6 == 2) {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_greeen_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.greeeen));


            binding.title.setTextColor(mContext.getResources().getColor(R.color.greeeen));
            binding.tutorname.setTextColor(mContext.getResources().getColor(R.color.greeeen));
            binding.timeon.setTextColor(mContext.getResources().getColor(R.color.greeeen));


            binding.tutorname.setText(timeTableBean.getStaff_name());
            binding.title.setText(timeTableBean.getSubject_name());
            binding.timeon.setText(timeTableBean.getFrom_time()+"-"+timeTableBean.getTo_time());
        } else if (position % 6 == 3) {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_red_color_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.red_color));
            binding.title.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.tutorname.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.timeon.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.tutorname.setText(timeTableBean.getStaff_name());
            binding.title.setText(timeTableBean.getSubject_name());
            binding.timeon.setText(timeTableBean.getFrom_time()+"-"+timeTableBean.getTo_time());
        }else if (position % 6 == 4) {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_litegreen_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.litegreen));
            binding.title.setTextColor(mContext.getResources().getColor(R.color.litegreen));
            binding.tutorname.setTextColor(mContext.getResources().getColor(R.color.litegreen));
            binding.timeon.setTextColor(mContext.getResources().getColor(R.color.litegreen));
            binding.tutorname.setText(timeTableBean.getStaff_name());
            binding.title.setText(timeTableBean.getSubject_name());
            binding.timeon.setText(timeTableBean.getFrom_time()+"-"+timeTableBean.getTo_time());
        }  else {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_completed_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.completed));
            binding.title.setTextColor(mContext.getResources().getColor(R.color.completed));
            binding.tutorname.setTextColor(mContext.getResources().getColor(R.color.completed));
            binding.timeon.setTextColor(mContext.getResources().getColor(R.color.completed));
            binding.tutorname.setText(timeTableBean.getStaff_name());
            binding.title.setText(timeTableBean.getSubject_name());
            binding.timeon.setText(timeTableBean.getFrom_time()+"-"+timeTableBean.getTo_time());
        }*/


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(TimetableMainRowBinding binding) {
            super(binding.getRoot());


        }
    }
  /* public class ViewHolder extends RecyclerView.ViewHolder {
       CatagoryLayoutBinding binding;

       public ViewHolder(CatagoryLayoutBinding binding) {
           super(binding.getRoot());
           this.binding = binding;
       }


   }*/

}