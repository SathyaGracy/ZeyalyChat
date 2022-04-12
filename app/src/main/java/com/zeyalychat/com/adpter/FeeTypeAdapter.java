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
import com.zeyalychat.com.bean.FeesBean;
import com.zeyalychat.com.databinding.FeeRowListBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FeeTypeAdapter extends RecyclerView.Adapter<FeeTypeAdapter.ViewHolder> {
    public ArrayList<FeesBean> mDataset;
    private Activity mContext;
    int row_index = 0;
    FeeRowListBinding binding = null;
    String datevalue = "";

    public FeeTypeAdapter(Activity mContext, ArrayList<FeesBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.fee_row_list, parent, false);

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
        FeesBean feeTypeBean = mDataset.get(position);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = simpleDateFormat2.parse(feeTypeBean.getLast_date());
            datevalue = simpleDateFormat1.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
      /*  int[] androidColors = mContext.getResources().getIntArray(R.array.random);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        binding.cardView.setCardBackgroundColor(randomAndroidColor);*/
        binding.titleFee.setText(feeTypeBean.getDisplay_description());
        binding.subtitletitleFee.setText(feeTypeBean.getAmount());
        binding.dateFee.setText("Last Date: "+datevalue);

        //    binding.Syllabusname.setText(syllabusBean.getChapter());
        //  binding.Descriptionname.setText(syllabusBean.getDescription());
        //  binding.Syllabussubtitle.setText(syllabusBean.getStaff_name());
        if (position % 6 == 0) {
        //    binding.cardLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_purple_lite));
         //   binding.v1.setBackgroundColor(mContext.getResources().getColor(R.color.purple));
          //  binding.title.setTextColor(mContext.getResources().getColor(R.color.purple));
           // binding.subtitle.setTextColor(mContext.getResources().getColor(R.color.purple));
           // binding.lastdate.setTextColor(mContext.getResources().getColor(R.color.purple));

          //  binding.title.setText(feeTypeBean.getName() + "-" + feeTypeBean.getAmount());
           // binding.lastdate.setText(datevalue);
            binding.statusBlock.setBackground(mContext.getResources().getDrawable(R.drawable.pending_box));
            binding.status.setTextColor(mContext.getResources().getColor(R.color.pending));
            binding.status.setText("Pending");
        } else if (position % 6 == 1) {
            binding.statusBlock.setBackground(mContext.getResources().getDrawable(R.drawable.paid_box));
            binding.status.setTextColor(mContext.getResources().getColor(R.color.paid));
            binding.status.setText("Paid");
        } else if (position % 6 == 2) {
            binding.statusBlock.setBackground(mContext.getResources().getDrawable(R.drawable.overdue_box));
            binding.status.setTextColor(mContext.getResources().getColor(R.color.overdue));
            binding.status.setText("OverDue");
        } else if (position % 6 == 3) {
            binding.statusBlock.setBackground(mContext.getResources().getDrawable(R.drawable.pending_box));
            binding.status.setTextColor(mContext.getResources().getColor(R.color.pending));
            binding.status.setText("Pending");

        } else if (position % 6 == 4) {
            binding.statusBlock.setBackground(mContext.getResources().getDrawable(R.drawable.paid_box));
            binding.status.setTextColor(mContext.getResources().getColor(R.color.paid));
            binding.status.setText("Paid");
        } else {
            binding.statusBlock.setBackground(mContext.getResources().getDrawable(R.drawable.overdue_box));
            binding.status.setTextColor(mContext.getResources().getColor(R.color.overdue));
            binding.status.setText("OverDue");
        }


    }/**/

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(FeeRowListBinding binding) {
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