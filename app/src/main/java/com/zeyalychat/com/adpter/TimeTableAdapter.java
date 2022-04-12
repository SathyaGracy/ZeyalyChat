package com.zeyalychat.com.adpter;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zeyalychat.com.R;
import com.zeyalychat.com.bean.TimeTableBean;
import com.zeyalychat.com.databinding.TimeRowBinding;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.ViewHolder> {
    public ArrayList<TimeTableBean> mDataset;
    private Activity mContext;
    int row_index = 0;
    TimeRowBinding binding = null;


    public TimeTableAdapter(Activity mContext, ArrayList<TimeTableBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.time_row, parent, false);

        return new ViewHolder(binding);

     /*   View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row_layout, parent, false);
        //   itemView.getLayoutParams().width = (int) (getScreenWidth() / 3);*/
        // return new ViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // String[] array = mContext.getResources().getStringArray(R.array.random);
        // String randomStr = array[new Random().nextInt(array.length)];
        //   binding.cardView.getBackground().setTint(Color.parseColor(randomStr));
      /*  int[] rainbow = mContext.getResources().getIntArray(R.array.random);
        binding.cardView.setBackgroundTintList(rainbow);
        for (int i = 0; i < tileColumns; i++) {
            paint.setColor(rainbow[i]);
            // Do something with the paint.
        }*/
        int[] androidColors = mContext.getResources().getIntArray(R.array.random);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
      //  binding.cardView.setCardBackgroundColor(randomAndroidColor);
      //  binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, randomAndroidColor), PorterDuff.Mode.MULTIPLY);

        TimeTableBean timeTableBean = mDataset.get(position);
        binding.subjectname.setText(timeTableBean.getSubject_name());

        /*if (MainActivity.userInfoArrayList.get(0).getRole_name().equalsIgnoreCase("Students")) {
            binding.staffname.setText(timeTableBean.getStaff_name());
        } else {
            binding.staffname.setText(timeTableBean.getSection_name());
        }*/
        binding.timeperiod.setText(timeTableBean.getFrom_time() + "-" + timeTableBean.getTo_time());


        if (position % 6 == 0) {
            binding.subjectname.setTextColor(mContext.getResources().getColor(R.color.purple));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_purple_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.purple), PorterDuff.Mode.SRC_IN);

        } else if (position % 6 == 1) {
            binding.subjectname.setTextColor(mContext.getResources().getColor(R.color.shade));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_shade_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.shade), PorterDuff.Mode.SRC_IN);
        } else if (position % 6 == 2) {
            binding.subjectname.setTextColor(mContext.getResources().getColor(R.color.greeeen));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_greeen_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.greeeen), PorterDuff.Mode.SRC_IN);
        } else if (position % 6 == 3) {
            binding.subjectname.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_red_color_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.red_color), PorterDuff.Mode.SRC_IN);
        } else if (position % 6 == 4) {
            binding.subjectname.setTextColor(mContext.getResources().getColor(R.color.litegreen));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_litegreen_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.litegreen), PorterDuff.Mode.SRC_IN);
        } else {
            binding.subjectname.setTextColor(mContext.getResources().getColor(R.color.completed));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_completed_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.completed), PorterDuff.Mode.SRC_IN);

        }











      /*   TimeTableBean referalBean = mDataset.get(position);
       binding.email.setText(referalBean.getAddress());
        binding.userName.setText(referalBean.getAmount());
        binding.userType.setText("Crypto Amount :" + referalBean.getCrypto_amount());
        binding.active.setText("crypto Symbol :" + referalBean.getStatus());
        binding.profit.setText("Fees :" + referalBean.getFees() + " EUR");*/
   /*     if (referalBean.getStatus().equalsIgnoreCase("true")) {
            binding.active.setText("Active");
            binding.active.setTextColor(mContext.getResources().getColor(R.color.button_color));
        } else {
            binding.active.setText("InActive");
            binding.active.setTextColor(mContext.getResources().getColor(R.color.red_color));
        }*/

/*

            try {
                String[] value = mDataset.get(position).getUpdatedAt().split("T");
                System.out.println("value " + value[0]);
                System.out.println("value " + value[1]);
                SimpleDateFormat ft = new SimpleDateFormat("MMM dd, yyyy");
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
                String check = null;
                try {
                    Date date = format1.parse(value[0]);
                    Date dt = sdf.parse(value[1]);
                    String time = sdfs.format(dt);
                    check = ft.format(date);
                    System.out.println("check   " + check);
                    System.out.println("time   " + time);

                    binding.date.setText(check + " " + time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(TimeRowBinding binding) {
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