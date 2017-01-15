package com.nos.ploy.flow.ployer.provider.review;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nos.ploy.R;
import com.nos.ploy.api.ployer.model.ReviewGson;
import com.nos.ploy.utils.DateParseUtils;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 13/1/2560.
 */

public class ProviderReviewRecyclerAdapter extends RecyclerView.Adapter<ProviderReviewRecyclerAdapter.ViewHolder> {


    private List<ReviewGson.Data.ReviewData> mDatas = new ArrayList<>();

    public ProviderReviewRecyclerAdapter() {
    }

    public void replaceData(List<ReviewGson.Data.ReviewData> reviewDataList) {
        mDatas.clear();
        notifyDataSetChanged();
        mDatas.addAll(reviewDataList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_provider_review_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (RecyclerUtils.isAvailableData(mDatas, position)) {
            ReviewGson.Data.ReviewData data = mDatas.get(position);
            if(null != data.getUserProfileImage()){
                Glide.with(holder.imgProfile.getContext()).load(data.getUserProfileImage().getImagePath()).error(R.drawable.ic_circle_profile_120dp).into(holder.imgProfile);
            }
            if(null != data.getReview()){
                holder.tvMessage.setText(data.getReview().getReviewText());
                holder.tvName.setText(""+data.getReview().getUserId());
                if(null != data.getReview().getCreatedDate()){
                    holder.tvDate.setText(DateParseUtils.parseDateString(data.getReview().getCreatedDate(),"MMM dd, yyyy"));
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return RecyclerUtils.getSize(mDatas);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_provider_review_item_message)
        public TextView tvMessage;
        @BindView(R.id.imageview_provider_review_item_profile_image)
        public ImageView imgProfile;
        @BindView(R.id.textview_provider_review_item_date)
        public TextView tvDate;
        @BindView(R.id.textview_provider_review_item_profile_name)
        public TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
