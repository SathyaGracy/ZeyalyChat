package com.zeyalychat.com.adpter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zeyalychat.com.R;
import com.zeyalychat.com.bean.ConversationBean;
import com.zeyalychat.com.databinding.ConversationListAdapterBinding;

import java.util.ArrayList;

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ViewHolder> {
    public ArrayList<ConversationBean> mDataset;
    private Activity mContext;
    Dialog dialog;

    ConversationListAdapterBinding binding = null;

    public ConversationListAdapter(Activity mContext, ArrayList<ConversationBean> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.conversation_list_adapter, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ConversationBean conversationBean=mDataset.get(position);
        if(conversationBean.getIs_sender().equalsIgnoreCase("true")){
            binding.senderLayout.setVisibility(View.VISIBLE);
            binding.senderMessage.setText(conversationBean.getContent());
            binding.receiverLayout.setVisibility(View.GONE);
            binding.sendertime.setText(conversationBean.getCreated_time());
        }else {
            binding.senderLayout.setVisibility(View.GONE);
            binding.receiverLayout.setVisibility(View.VISIBLE);
            binding.receiverMessage.setText(conversationBean.getContent());
            binding.time.setText(conversationBean.getCreated_time());
        }



    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {




        public ViewHolder(ConversationListAdapterBinding binding) {
            super(binding.getRoot());


        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}


