package com.nos.ploy.flow.ployer.service.view;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nos.ploy.R;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.flow.generic.PloyerServicesCategoryFilter;
import com.nos.ploy.utils.RecyclerUtils;
import com.nos.ploy.utils.URLHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by User on 10/11/2559.
 */

public class PloyerCategoryRecyclerAdapter extends RecyclerView.Adapter<PloyerCategoryRecyclerAdapter.ViewHolder> {


    private final Action1<PloyerServicesGson.Data> onItemClickListener;
    private LanguageAppLabelGson.Data language;
    private PloyerServicesCategoryFilter filter;
    private List<PloyerServicesGson.Data> mOriginalDatas = new ArrayList<>();
    private List<PloyerServicesGson.Data> mFilteredDatas = new ArrayList<>();

    public PloyerCategoryRecyclerAdapter(Action1<PloyerServicesGson.Data> onItemClick) {
        this.onItemClickListener = onItemClick;
        filter = new PloyerServicesCategoryFilter(mOriginalDatas,this);

    }

//    @Override
//    protected SortedListAdapter.ViewHolder<? extends PloyerServicesGson.Data> onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
//        return new PloyerCategoryRecyclerAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ployer_service_item, parent, false));
//    }

//    @Override
//    protected boolean areItemsTheSame(PloyerServicesGson.Data item1, PloyerServicesGson.Data item2) {
//        return item1.getId() == item2.getId();
//    }
//
//    @Override
//    protected boolean areItemContentsTheSame(PloyerServicesGson.Data oldItem, PloyerServicesGson.Data newItem) {
//        return oldItem.equals(newItem);
//    }

    public void setLanguage(LanguageAppLabelGson.Data language) {
        this.language = language;
    }

    public void replaceData(ArrayList<PloyerServicesGson.Data> data) {
        mOriginalDatas.clear();
        mFilteredDatas.clear();
        notifyDataSetChanged();
        mOriginalDatas.addAll(data);
        mFilteredDatas.addAll(mOriginalDatas);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PloyerCategoryRecyclerAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ployer_service_item, parent, false));
    }

    public void filterList(String text) {
        filter.filter(text);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (RecyclerUtils.isAvailableData(mFilteredDatas, position)) {
            final PloyerServicesGson.Data data = mFilteredDatas.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (RecyclerUtils.isAvailableData(mFilteredDatas, holder.getAdapterPosition())) {
                        PloyerServicesGson.Data data = mFilteredDatas.get(holder.getAdapterPosition());
                        if (null != onItemClickListener) {
                            onItemClickListener.call(data);
                        }
                    }
                }
            });


            Glide.with(holder.imgServiceImage.getContext()).load(URLHelper.changURLEndpoint(data.getImgUrl())).placeholder(holder.mDrawableGenizPlaceHolder).error(holder.mDrawableGenizPlaceHolder).fallback(holder.mDrawableGenizPlaceHolder).into(holder.imgServiceImage);
            holder.tvTitle.setText(data.getServiceName());
            holder.tvSubtitle.setText(data.getPloyeeCount() + " " + language.providersLabel);
        }

    }

    @Override
    public int getItemCount() {
        return RecyclerUtils.getSize(mFilteredDatas);
    }

    public void setFilteredData(List<PloyerServicesGson.Data> filteredData) {
        this.mFilteredDatas.clear();
        this.mFilteredDatas.addAll(filteredData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

//        @Override
//        protected void performBind(final PloyerServicesGson.Data data) {
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (null != onItemClickListener) {
//                        onItemClickListener.call(data);
//                    }
//                }
//            });
//            Glide.with(imgServiceImage.getContext()).load(data.getImgUrl()).placeholder(mDrawableGenizPlaceHolder).error(mDrawableGenizPlaceHolder).fallback(mDrawableGenizPlaceHolder).into(imgServiceImage);
//            tvTitle.setText(data.getServiceName());
//            tvSubtitle.setText(data.getPloyeeCount() + " " +language.providersLabel);
//        }
    }
}
