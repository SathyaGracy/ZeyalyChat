package com.zeyalychat.com.adpter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zeyalychat.com.R;
import com.zeyalychat.com.bean.HomeWorkBean;
import com.zeyalychat.com.databinding.AssingnmentRowBinding;

import java.util.ArrayList;

public class CardRowAdapter extends RecyclerView.Adapter<CardRowAdapter.ViewHolder> {

    private ArrayList<HomeWorkBean> mdataset;
    Context mContext;

    AssingnmentRowBinding binding = null;

    public CardRowAdapter(Context mContext, ArrayList<HomeWorkBean> data) {
        this.mdataset = data;
        this.mContext = mContext;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.assingnment_row, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeWorkBean homeWorkBean = mdataset.get(position);
        binding.txtTitle.setText(homeWorkBean.getContent());
        binding.txtSubtitlte.setText(homeWorkBean.getTitle());
        binding.date.setText(homeWorkBean.getComplete_by());
      /*  if (position % 3 == 0) {
            binding.cardRl.setBackground(mContext.getDrawable(R.drawable.button_design_card_green));
        } else if (position % 3 == 1) {
            binding.cardRl.setBackground(mContext.getDrawable(R.drawable.button_design_card_red));
        } else {
            binding.cardRl.setBackground(mContext.getDrawable(R.drawable.button_design_card_blue));
        }
        binding.cardRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog(position);
            }
        });*/


    }


    @Override
    public int getItemCount() {
        return mdataset.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(AssingnmentRowBinding binding) {
            super(binding.getRoot());

        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void Dialog(int pos) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_more);
        RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
        TextView textView= dialog.findViewById(R.id.name_txt);
        textView.setText(mdataset.get(pos).getContent());
        dialog.show();
        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


}
