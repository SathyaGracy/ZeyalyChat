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
import com.zeyalychat.com.bean.HomeWorkBean;
import com.zeyalychat.com.databinding.HomeWorkRowBinding;

import java.util.ArrayList;

public class HomeWorkAdapter extends RecyclerView.Adapter<HomeWorkAdapter.ViewHolder> {
    public ArrayList<HomeWorkBean> mDataset;
    private Activity mContext;

    HomeWorkRowBinding binding = null;


    public HomeWorkAdapter(Activity mContext, ArrayList<HomeWorkBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.home_work_row, parent, false);

        return new ViewHolder(binding);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        HomeWorkBean homeWorkBean = mDataset.get(position);
        binding.SubjectTxt.setText(homeWorkBean.getSubject_name());
        binding.TitleTxt.setText(homeWorkBean.getTitle());
        binding.ContentTxt.setText(homeWorkBean.getContent());
        binding.homeworkTypeTxt.setText(homeWorkBean.getType_name());
        binding.CompletionDateTxt.setText(homeWorkBean.getComplete_by());

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


        public ViewHolder(HomeWorkRowBinding binding) {
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