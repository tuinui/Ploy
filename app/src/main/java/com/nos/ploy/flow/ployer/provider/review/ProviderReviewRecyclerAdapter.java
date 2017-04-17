package com.nos.ploy.flow.ployer.provider.review;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nos.ploy.R;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.ployer.model.ProviderUserListGson;
import com.nos.ploy.api.ployer.model.ReviewGson;
import com.nos.ploy.flow.ployer.provider.ProviderProfileActivity;
import com.nos.ploy.utils.DateParseUtils;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 13/1/2560.
 */

public class ProviderReviewRecyclerAdapter extends RecyclerView.Adapter<ProviderReviewRecyclerAdapter.ViewHolder> {


    private final PloyerServicesGson.Data mParentData;
    private List<ReviewGson.Data.ReviewData> mDatas = new ArrayList<>();

    public ProviderReviewRecyclerAdapter(PloyerServicesGson.Data parentData) {
        this.mParentData = parentData;
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
            final ReviewGson.Data.ReviewData data = mDatas.get(position);
            if (null != data.getUserProfileImage()) {
                Glide.with(holder.imgProfile.getContext()).load(data.getUserProfileImage().getImagePath()).error(R.drawable.ic_circle_profile_120dp).into(holder.imgProfile);
            }
            if (null != data.getReview()) {
                holder.tvMessage.setText(data.getReview().getReviewText());
                if (data.getUserReview() != null) {
                    holder.tvName.setText(data.getUserReview().getFullName());
                }

                if (null != data.getReview().getCreatedDate()) {
                    holder.tvDate.setText(DateParseUtils.parseDateString(data.getReview().getCreatedDate(), "MMM dd, yyyy"));
                }
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != data.getUserReview()) {
                        Bundle bundle = new Bundle();
                        ProviderUserListGson.Data.UserService mockUserService = new ProviderUserListGson.Data.UserService(data.getUserReview().getUserId(), data.getUserReview(), null);
                        bundle.putParcelable(ProviderProfileActivity.KEY_PLOYEE_USER_SERVICE_DATA, mockUserService);
                        bundle.putParcelable(ProviderProfileActivity.KEY_PARENT_DATA, mParentData);
                        IntentUtils.startActivity(v.getContext(), ProviderProfileActivity.class, bundle);
                    }
                }
            });

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
            ButterKnife.bind(this, itemView);
        }
    }
}
