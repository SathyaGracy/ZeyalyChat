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
import com.zeyalychat.com.bean.TutorialBean;
import com.zeyalychat.com.databinding.VideoRowListBinding;

import java.util.ArrayList;

public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.ViewHolder> {
    public ArrayList<TutorialBean> mDataset;
    private Activity mContext;
    int row_index = 0;
    VideoRowListBinding binding = null;
    String datevalue = "";

    public TutorialAdapter(Activity mContext, ArrayList<TutorialBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.video_row_list, parent, false);

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
        TutorialBean feeTypeBean = mDataset.get(position);

   /*     int[] androidColors = mContext.getResources().getIntArray(R.array.random);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        binding.cardView.setCardBackgroundColor(randomAndroidColor);*/
        binding.title.setText(feeTypeBean.getTitle());


        //    binding.Syllabusname.setText(syllabusBean.getChapter());
        //  binding.Descriptionname.setText(syllabusBean.getDescription());
        //  binding.Syllabussubtitle.setText(syllabusBean.getStaff_name());
     /*   if (position % 6 == 0) {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_purple_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.purple));
            binding.title.setTextColor(mContext.getResources().getColor(R.color.purple));
            binding.subtitle.setTextColor(mContext.getResources().getColor(R.color.purple));
            binding.lastdate.setTextColor(mContext.getResources().getColor(R.color.purple));

            binding.title.setText(feeTypeBean.getName() + "-" + feeTypeBean.getAmount());
            binding.lastdate.setText(datevalue);
        } else if (position % 6 == 1) {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_shade_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.shade));
            binding.title.setTextColor(mContext.getResources().getColor(R.color.shade));
            binding.subtitle.setTextColor(mContext.getResources().getColor(R.color.shade));
            binding.lastdate.setTextColor(mContext.getResources().getColor(R.color.shade));


            binding.title.setText(feeTypeBean.getName() + "-" + feeTypeBean.getAmount());
            binding.lastdate.setText(datevalue);
        } else if (position % 6 == 2) {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_greeen_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.greeeen));
            binding.title.setTextColor(mContext.getResources().getColor(R.color.greeeen));
            binding.subtitle.setTextColor(mContext.getResources().getColor(R.color.greeeen));
            binding.lastdate.setTextColor(mContext.getResources().getColor(R.color.greeeen));

            binding.title.setText(feeTypeBean.getName() + "-" + feeTypeBean.getAmount());
            binding.lastdate.setText(datevalue);
        } else if (position % 6 == 3) {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_red_color_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.red_color));
            binding.title.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.subtitle.setTextColor(mContext.getResources().getColor(R.color.red_color));
            binding.lastdate.setTextColor(mContext.getResources().getColor(R.color.red_color));

            binding.title.setText(feeTypeBean.getName() + "-" + feeTypeBean.getAmount());
            binding.lastdate.setText(datevalue);
        } else if (position % 6 == 4) {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_litegreen_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.litegreen));

            binding.title.setTextColor(mContext.getResources().getColor(R.color.litegreen));
            binding.subtitle.setTextColor(mContext.getResources().getColor(R.color.litegreen));
            binding.lastdate.setTextColor(mContext.getResources().getColor(R.color.litegreen));

            binding.title.setText(feeTypeBean.getName() + "-" + feeTypeBean.getAmount());
            binding.lastdate.setText(datevalue);
        } else {
            binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_completed_lite));
            binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.completed));

            binding.title.setTextColor(mContext.getResources().getColor(R.color.completed));
            binding.subtitle.setTextColor(mContext.getResources().getColor(R.color.completed));
            binding.lastdate.setTextColor(mContext.getResources().getColor(R.color.completed));


            binding.title.setText(feeTypeBean.getName() + "-" + feeTypeBean.getAmount());
            binding.lastdate.setText(datevalue);
        }*/


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(VideoRowListBinding binding) {
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