package com.zeyalychat.com.adpter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zeyalychat.com.R;
import com.zeyalychat.com.bean.NavigationBean;
import com.zeyalychat.com.databinding.NavHeaderViewBinding;
import com.zeyalychat.com.utils.RecyclerAdapterListener;

import java.util.ArrayList;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {
    public ArrayList<NavigationBean> mDataset;
    private Activity mContext;
    Dialog dialog;
    NavHeaderViewBinding binding;
//    int row_index = 0;
    private RecyclerAdapterListener adapterListener;


    public NavigationAdapter(Activity mContext, ArrayList<NavigationBean> myDataset, RecyclerAdapterListener<NavigationBean> adapterListener) {
        this.mDataset = myDataset;
        this.mContext = mContext;
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.adapterListener = adapterListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.nav_header_view, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (position == 0) {
            binding.img.setImageDrawable(mContext.getDrawable(R.mipmap.calander));
        } else if (position == 1) {
            binding.img.setImageDrawable(mContext.getDrawable(R.mipmap.book));
        } else if (position == 2){
            binding.img.setImageDrawable(mContext.getDrawable(R.mipmap.test));
        }else if (position == 3){
            binding.img.setImageDrawable(mContext.getDrawable(R.mipmap.holiday));
        }else if (position == 4){
            binding.img.setImageDrawable(mContext.getDrawable(R.mipmap.fee));
        }else if (position == 5){
            binding.img.setImageDrawable(mContext.getDrawable(R.mipmap.video_ic));
        }



        NavigationBean navigationBean = mDataset.get(position);


      // binding.txtName.setTextColor(mContext.getResources().getColor(R.color.medium_text_color));
       /* if (navigationBean.getIsSelf()) {
            binding.txtName.setTextColor(mContext.getResources().getColor(R.color.green));
            ImageViewCompat.setImageTintList(binding.img, ColorStateList.valueOf(mContext.getResources().getColor(R.color.green)));

        }else {
            binding.txtName.setTextColor(mContext.getResources().getColor(R.color.medium_text_color));
            ImageViewCompat.setImageTintList(binding.img, ColorStateList.valueOf(mContext.getResources().getColor(R.color.medium_text_color)));
        }*/

        binding.txtName.setText(navigationBean.getTitle());

        /*binding.LTRMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
            }
        });*/

      /*  if (navigationBean.isSelected()) {
            binding.main.setBackgroundColor(mContext.getResources().getColor(R.color.background_color));
           // ImageViewCompat.setImageTintList(binding.img, ColorStateList.valueOf(mContext.getResources().getColor(R.color.green)));
            //binding.txtName.setTextColor(mContext.getResources().getColor(R.color.green));
            // binding.img.setColorFilter(ContextCompat.getColor(mContext, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);

        }else {
           // binding.txtName.setTextColor(mContext.getResources().getColor(R.color.full_textt_color));
            //ImageViewCompat.setImageTintList(binding.img, ColorStateList.valueOf(mContext.getResources().getColor(R.color.medium_text_color)));
        }
*/
        // binding.img.setColorFilter(ContextCompat.getColor(mContext, R.color.medium_text_color), android.graphics.PorterDuff.Mode.MULTIPLY);

        binding.LTRMain.setOnClickListener(v -> {
            adapterListener.onItemClicked(navigationBean, position);
        });


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(NavHeaderViewBinding binding) {
            super(binding.getRoot());

        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}

