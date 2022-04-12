package com.zeyalychat.com.adpter;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zeyalychat.com.R;
import com.zeyalychat.com.bean.SyllabusBean;
import com.zeyalychat.com.databinding.SyllabusRowListBinding;

import java.util.ArrayList;

public class SyllabusAdapter extends RecyclerView.Adapter<SyllabusAdapter.ViewHolder> {
    public ArrayList<SyllabusBean> mDataset;
    private Activity mContext;
    int row_index = 0;
    SyllabusRowListBinding binding = null;


    public SyllabusAdapter(Activity mContext, ArrayList<SyllabusBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.syllabus_row_list, parent, false);

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
      /*  int[] androidColors = mContext.getResources().getIntArray(R.array.sylrandom);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        binding.cardView.setCardBackgroundColor(randomAndroidColor);*/
        SyllabusBean syllabusBean = mDataset.get(position);
        binding.Syllabusname.setText(syllabusBean.getName());
      //  binding.Syllabussubtitle.setText(syllabusBean.getStaff_name());

        if (position % 6 == 0) {
            binding.calculatorImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.calculator));
            binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.purple));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_purple_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.purple), PorterDuff.Mode.SRC_IN);

        } else if (position % 6 == 1) {
            binding.calculatorImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.biotech));
            binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.shade));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_shade_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.shade), PorterDuff.Mode.SRC_IN);
        } else if (position % 6 == 2) {
            binding.calculatorImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.physics));

            binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.greeeen));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_greeen_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.greeeen), PorterDuff.Mode.SRC_IN);
        } else if (position % 6 == 3) {
            binding.calculatorImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.science));
            binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_red_color_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.red_color), PorterDuff.Mode.SRC_IN);
        } else if (position % 6 == 4) {
            binding.calculatorImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.physics));

            binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.litegreen));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_litegreen_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.litegreen), PorterDuff.Mode.SRC_IN);
        } else {
            binding.calculatorImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.calculator));
            binding.Syllabusname.setTextColor(mContext.getResources().getColor(R.color.completed));
            binding.cardView.setBackground(mContext.getResources().getDrawable(R.drawable.background_completed_lite));
            binding.calculatorImg.setColorFilter(ContextCompat.getColor(mContext, R.color.completed), PorterDuff.Mode.SRC_IN);

        }



    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(SyllabusRowListBinding binding) {
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