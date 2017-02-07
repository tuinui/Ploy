package com.nos.ploy.flow.ployer.person.list.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.nos.ploy.R;
import com.nos.ploy.api.ployer.model.ProviderUserListGson;
import com.nos.ploy.flow.generic.UserServiceFilter;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 10/11/2559.
 */

public class PloyerPersonListRecyclerAdapter extends RecyclerView.Adapter<PloyerPersonListRecyclerAdapter.ViewHolder> {
    private OnDataBindListener mListener;
    private List<ProviderUserListGson.Data.UserService> mDatas = new ArrayList<>();
    private List<ProviderUserListGson.Data.UserService> mFilteredData = new ArrayList<>();
    private UserServiceFilter mFilter;
    public PloyerPersonListRecyclerAdapter(OnDataBindListener listener) {
        mListener = listener;
        mFilter = new UserServiceFilter(mDatas,this);
    }

    public void replaceData(List<ProviderUserListGson.Data.UserService> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyItemChanged(0, getItemCount());
    }

    public void setFilteredData(List<ProviderUserListGson.Data.UserService> mFilteredData) {
        this.mFilteredData.clear();
        this.mFilteredData.addAll(mFilteredData);
    }

    public void filterList(String text) {
        mFilter.filter(text);
    }
    //    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ployee_list_item, parent, false));
//    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PloyerPersonListRecyclerAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ployer_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null != mListener) {
            if (RecyclerUtils.isAvailableData(mDatas, position)) {
                mListener.onDataBind(holder, mDatas.get(position));
            }

        }
    }

    @Override
    public int getItemCount() {
        return RecyclerUtils.getSize(mDatas);
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
        @BindView(R.id.textview_ployer_list_item_rate)
        public TextView tvRate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void performBind(ProviderUserListGson.Data.UserService data) {
            if (null != mListener) {
                mListener.onDataBind(this, data);
            }

        }

    }


    public static interface OnDataBindListener {
        public void onDataBind(ViewHolder holder, ProviderUserListGson.Data.UserService data);
    }

}
