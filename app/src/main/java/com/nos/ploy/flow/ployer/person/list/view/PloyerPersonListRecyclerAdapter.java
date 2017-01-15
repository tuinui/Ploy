package com.nos.ploy.flow.ployer.person.list.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.nos.ploy.R;
import com.nos.ploy.api.ployer.model.ProviderUserListGson;

import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 10/11/2559.
 */

public class PloyerPersonListRecyclerAdapter extends SortedListAdapter<ProviderUserListGson.Data.UserService> {
    private OnDataBindListener mListener;

    public PloyerPersonListRecyclerAdapter(Context context, Comparator<ProviderUserListGson.Data.UserService> comparator, OnDataBindListener listener) {
        super(context, ProviderUserListGson.Data.UserService.class, comparator);
        mListener = listener;
    }

    //    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ployee_list_item, parent, false));
//    }
    @Override
    protected SortedListAdapter.ViewHolder<? extends ProviderUserListGson.Data.UserService> onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new PloyerPersonListRecyclerAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ployer_list_item, parent, false));
    }

    @Override
    protected boolean areItemContentsTheSame(ProviderUserListGson.Data.UserService data, ProviderUserListGson.Data.UserService t1) {
        return data.getUserId() == t1.getUserId();
    }

    @Override
    protected boolean areItemsTheSame(ProviderUserListGson.Data.UserService data, ProviderUserListGson.Data.UserService t1) {
        return data.equals(t1);
    }

    public class ViewHolder extends SortedListAdapter.ViewHolder<ProviderUserListGson.Data.UserService> {
        @BindView(R.id.textview_ployer_list_item_price)
        public TextView tvPrice;
        @BindView(R.id.imageview_ployer_list_item_profile_photo)
        public ImageView imgPhoto;
        @BindView(R.id.ratingbar_ployer_list_item_rate)
        public RatingBar ratingBar;
        @BindView(R.id.textview_ployer_list_item_title)
        public TextView tvTitle;
        @BindView(R.id.textview_ployer_list_distance)
        public TextView tvDistance;
        //        @BindView(R.id.textview_ployer_list_item_subtitle)
//        public TextView tvSubTitle;
        @BindView(R.id.textview_ployer_list_item_description)
        public TextView tvDescription;
        @BindView(R.id.textview_ployer_list_item_review_count)
        public TextView tvReviewCount;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void performBind(ProviderUserListGson.Data.UserService data) {
            if(null != mListener){
                mListener.onDataBind(this,data);
            }

        }

    }


    public static interface OnDataBindListener {
        public void onDataBind(ViewHolder holder,ProviderUserListGson.Data.UserService data);
    }

}
