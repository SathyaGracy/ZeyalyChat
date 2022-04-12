package com.zeyalychat.com.adpter;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zeyalychat.com.R;
import com.zeyalychat.com.bean.ExamBean;
import com.zeyalychat.com.databinding.ExamRowBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {
    public ArrayList<ExamBean> mDataset;
    private Activity mContext;
    int row_index = 0;
    ExamRowBinding binding = null;
    String day, month, year;


    public ExamAdapter(Activity mContext, ArrayList<ExamBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.exam_row, parent, false);

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
        //   binding.cardLayout.getBackground().setTint(Color.parseColor(randomStr));
      /*  int[] rainbow = mContext.getResources().getIntArray(R.array.random);
        binding.cardLayout.setBackgroundTintList(rainbow);
        for (int i = 0; i < tileColumns; i++) {
            paint.setColor(rainbow[i]);
            // Do something with the paint.
        }*/
        ExamBean examBean = mDataset.get(position);


        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat2.parse(examBean.getExam_date());
            day = (String) DateFormat.format("dd", date); // 20
            month = (String) DateFormat.format("MMM", date); // Jun
            year = (String) DateFormat.format("yyyy", date); // Jun
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_purple_lite));
        // binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.purple));
        // binding.Descriptionname.setTextColor(mContext.getResources().getColor(R.color.purple));
        //  binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.purple));
        //  binding.minMax.setTextColor(mContext.getResources().getColor(R.color.purple));
        //  binding.examday.setTextColor(mContext.getResources().getColor(R.color.purple));
        //  binding.exammonth.setTextColor(mContext.getResources().getColor(R.color.purple));
//        binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_blue));
        binding.month.setText(month);
        binding.day.setText(day);
        //   binding.examday.setText(day);
        binding.Syllabusname.setText(examBean.getName());
        binding.date.setText(examBean.getExam_date());
        binding.time.setText(examBean.getExam_date());
        binding.mark.setText(examBean.getMin_mark() + "/" + examBean.getMax_mark());
        binding.SubTxt.setText(examBean.getSubject_name());
        binding.examtype.setText(examBean.getType_name());

        //    binding.Syllabusname.setText(examBean.getChapter());
        //  binding.Descriptionname.setText(examBean.getDescription());
        //  binding.Syllabussubtitle.setText(examBean.getStaff_name());
       /* if (position % 6 == 0) {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_purple_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.purple));
            binding.Descriptionname.setTextColor(mContext.getResources().getColor(R.color.purple));
            binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.purple));
            binding.minMax.setTextColor(mContext.getResources().getColor(R.color.purple));
            binding.examday.setTextColor(mContext.getResources().getColor(R.color.purple));
            binding.exammonth.setTextColor(mContext.getResources().getColor(R.color.purple));
            binding.exammonth.setText(month);
            binding.examday.setText(day);
            binding.Syllabusname.setText(examBean.getType_name());
            binding.Descriptionname.setText(examBean.getName());
            binding.minMax.setText(examBean.getMin_mark() + "/" + examBean.getMax_mark());
        } else if (position % 6 == 1) {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_shade_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.shade));
            binding.Descriptionname.setTextColor(mContext.getResources().getColor(R.color.shade));
            binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.shade));
            binding.minMax.setTextColor(mContext.getResources().getColor(R.color.shade));
            binding.examday.setTextColor(mContext.getResources().getColor(R.color.shade));
            binding.exammonth.setTextColor(mContext.getResources().getColor(R.color.shade));
            binding.Syllabusname.setText(examBean.getType_name());
            binding.exammonth.setText(month);
            binding.examday.setText(day);
            binding.Descriptionname.setText(examBean.getName());
            binding.minMax.setText(examBean.getMin_mark() + "/" + examBean.getMax_mark());
        } else if (position % 6 == 2) {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_greeen_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.greeeen));
            binding.Descriptionname.setTextColor(mContext.getResources().getColor(R.color.greeeen));
            binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.greeeen));
            binding.minMax.setTextColor(mContext.getResources().getColor(R.color.greeeen));
            binding.examday.setTextColor(mContext.getResources().getColor(R.color.greeeen));
            binding.exammonth.setTextColor(mContext.getResources().getColor(R.color.greeeen));
            binding.exammonth.setText(month);
            binding.examday.setText(day);
            binding.Syllabusname.setText(examBean.getType_name());
            binding.Descriptionname.setText(examBean.getName());

            binding.minMax.setText(examBean.getMin_mark() + "/" + examBean.getMax_mark());
        } else if (position % 6 == 3) {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_red_color_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.red_color));
            binding.Descriptionname.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.minMax.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.exammonth.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.examday.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.exammonth.setText(month);
            binding.examday.setText(day);
            binding.Syllabusname.setText(examBean.getType_name());
            binding.Descriptionname.setText(examBean.getName());
            binding.minMax.setText(examBean.getMin_mark() + "/" + examBean.getMax_mark());
        } else if (position % 6 == 4) {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_litegreen_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.litegreen));
            binding.Descriptionname.setTextColor(mContext.getResources().getColor(R.color.litegreen));
            binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.litegreen));
            binding.minMax.setTextColor(mContext.getResources().getColor(R.color.litegreen));
            binding.examday.setTextColor(mContext.getResources().getColor(R.color.litegreen));
            binding.exammonth.setTextColor(mContext.getResources().getColor(R.color.litegreen));
            binding.Syllabusname.setText(examBean.getType_name());
            binding.Descriptionname.setText(examBean.getName());
            binding.exammonth.setText(month);
            binding.examday.setText(day);
            binding.minMax.setText(examBean.getMin_mark() + "/" + examBean.getMax_mark());
        } else {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_completed_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.completed));
            binding.Descriptionname.setTextColor(mContext.getResources().getColor(R.color.completed));
            binding.examday.setTextColor(mContext.getResources().getColor(R.color.completed));
            binding.exammonth.setTextColor(mContext.getResources().getColor(R.color.completed));
            binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.completed));
            binding.minMax.setTextColor(mContext.getResources().getColor(R.color.completed));
            binding.Syllabusname.setText(examBean.getType_name());
            binding.Descriptionname.setText(examBean.getName());
            binding.exammonth.setText(month);
            binding.examday.setText(day);
            binding.minMax.setText(examBean.getMin_mark() + "/" + examBean.getMax_mark());
        }
*/
        if (position % 6 == 0) {
            binding.calculatorImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.calculator));
          //  binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.purple));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_purple_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.purple), PorterDuff.Mode.SRC_IN);

        } else if (position % 6 == 1) {
            binding.calculatorImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.biotech));
          //  binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.shade));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_shade_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.shade), PorterDuff.Mode.SRC_IN);
        } else if (position % 6 == 2) {
            binding.calculatorImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.physics));

          //  binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.greeeen));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_greeen_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.greeeen), PorterDuff.Mode.SRC_IN);
        } else if (position % 6 == 3) {
            binding.calculatorImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.science));
          //  binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_red_color_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.red_color), PorterDuff.Mode.SRC_IN);
        } else if (position % 6 == 4) {
            binding.calculatorImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.physics));

         //   binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.litegreen));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_litegreen_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.litegreen), PorterDuff.Mode.SRC_IN);
        } else {
            binding.calculatorImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.calculator));
         //   binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.completed));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_completed_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.completed), PorterDuff.Mode.SRC_IN);

        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(ExamRowBinding binding) {
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