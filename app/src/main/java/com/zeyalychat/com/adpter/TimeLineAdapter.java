package com.zeyalychat.com.adpter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zeyalychat.com.R;
import com.zeyalychat.com.bean.TimeLineBean;
import com.zeyalychat.com.databinding.TimeLineRowBinding;
import com.zeyalychat.com.session.Session;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {

    public ArrayList<TimeLineBean> mDataset;
    private Activity mContext;
    Dialog dialog;
    TimeLineRowBinding binding = null;
    Session session;
    String ans="";
    OnItemClickListener listener;


    public TimeLineAdapter(Activity mContext, ArrayList<TimeLineBean> myDataset,OnItemClickListener listener) {
        this.mDataset = myDataset;
        this.mContext = mContext;
        this.listener = listener;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.time_line_row, parent, false);
        //   itemView.getLayoutParams().width = (int) (getScreenWidth() / 3);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        TimeLineBean timeLineBean = mDataset.get(position);
        binding.headingTxt.setText(timeLineBean.getCreated_by_name());
        binding.lastmessage.setText(timeLineBean.getCreated_date());
        binding.postData.setText(timeLineBean.getContent());
        binding.commentsLbl.setText(timeLineBean.getComments_count() + " "+mContext.getResources().getString(R.string.Comments));
        binding.likesLbl.setText(timeLineBean.getLikes_count() + " "+mContext.getResources().getString(R.string.Likes));
        Picasso.with(mContext).load(timeLineBean.getProfile_image_url()).into(binding.profile);
        if (timeLineBean.isIslike()) {

            binding.liketxt.setTextColor(ContextCompat.getColor(mContext, R.color.green));
            ImageViewCompat.setImageTintList(binding.imgLike, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.green)));

        } else {


            ImageViewCompat.setImageTintList(binding.imgLike, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.medium_text_color)));
            binding.liketxt.setTextColor(ContextCompat.getColor(mContext, R.color.medium_text_color));


        }
        binding.likeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (timeLineBean.isIslike()) {
                       listener.unlike(timeLineBean.getId(), position);
                        binding.liketxt.setTextColor(ContextCompat.getColor(mContext, R.color.medium_text_color));
                        ImageViewCompat.setImageTintList(binding.imgLike, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.medium_text_color)));

                    } else {

                        listener.CreateLike(timeLineBean.getId(), position);
                        ImageViewCompat.setImageTintList(binding.imgLike, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.green)));
                        binding.liketxt.setTextColor(ContextCompat.getColor(mContext, R.color.green));


                    }

            }
        });
        binding.commentEdt.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                 ans = s.toString();
                // save ans to sharedpreferences or Database
            }
        });
        binding.postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if (!ans.equalsIgnoreCase("")) {
                        listener.postComment(ans, timeLineBean.getId(), position);
                       // binding.addCommentEdt.setText("");
                    }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        public ViewHolder(TimeLineRowBinding binding) {
            super(binding.getRoot());

        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
    public interface OnItemClickListener {
        void postComment(String comment, String id, int pos);
        void unlike(String id, int pos);
        void CreateLike(String id, int pos);
    }

}

