package com.nos.ploy.flow.ployer.service.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.nos.ploy.R;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;

import java.util.Comparator;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by User on 10/11/2559.
 */

public class PloyerCategoryRecyclerAdapter extends SortedListAdapter<PloyerServicesGson.Data> {


    private final Action1<PloyerServicesGson.Data> onItemClickListener;

    public PloyerCategoryRecyclerAdapter(Context context, Comparator<PloyerServicesGson.Data> comparator, Action1<PloyerServicesGson.Data> onItemClick) {
        super(context, PloyerServicesGson.Data.class, comparator);
        this.onItemClickListener = onItemClick;
    }

    @Override
    protected SortedListAdapter.ViewHolder<? extends PloyerServicesGson.Data> onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new PloyerCategoryRecyclerAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ployer_service_item, parent, false));
    }

    @Override
    protected boolean areItemsTheSame(PloyerServicesGson.Data item1, PloyerServicesGson.Data item2) {
        return item1.getId() == item2.getId();
    }

    @Override
    protected boolean areItemContentsTheSame(PloyerServicesGson.Data oldItem, PloyerServicesGson.Data newItem) {
        return oldItem.equals(newItem);
    }

    public class ViewHolder extends SortedListAdapter.ViewHolder<PloyerServicesGson.Data> {
        @BindView(R.id.imageview_ployer_service_item_image)
        public ImageView imgServiceImage;
        @BindView(R.id.textview_ployer_service_item_title)
        public TextView tvTitle;
        @BindView(R.id.textview_ployer_service_item_subtitle)
        public TextView tvSubtitle;
        @BindDrawable(R.drawable.ic_ployer_item_placeholder)
        Drawable mDrawableGenizPlaceHolder;
        @BindString(R.string.PLOYEE)
        String LPLOYEE;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void performBind(final PloyerServicesGson.Data data) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onItemClickListener) {
                        onItemClickListener.call(data);
                    }
                }
            });
            Glide.with(imgServiceImage.getContext()).load(data.getImgUrl()).placeholder(mDrawableGenizPlaceHolder).error(mDrawableGenizPlaceHolder).fallback(mDrawableGenizPlaceHolder).into(imgServiceImage);
            tvTitle.setText(data.getServiceName());
            tvSubtitle.setText(data.getPloyeeCount() + " " + LPLOYEE);
        }
    }
}
