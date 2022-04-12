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
import com.zeyalychat.com.bean.ChatListBean;
import com.zeyalychat.com.databinding.ChatListAdapterBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    public ArrayList<ChatListBean> mDataset;
    private Activity mContext;
    Dialog dialog;

    ChatListAdapterBinding binding = null;

    public ChatListAdapter(Activity mContext, ArrayList<ChatListBean> myDataset) {
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
                R.layout.chat_list_adapter, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ChatListBean chatListBean=mDataset.get(position);
        Picasso.with(mContext).load(chatListBean.getImg_url()).into(binding.profile);
        binding.headingTxt.setText(chatListBean.getTitle());
        binding.lastmessage.setText(chatListBean.getLast_message());
        binding.date.setText(chatListBean.getUpdated_date());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {




        public ViewHolder(ChatListAdapterBinding binding) {
            super(binding.getRoot());


        }
    }


}


