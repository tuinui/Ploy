package com.nos.ploy.flow.ployee.home.content.availability.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;
import com.nos.ploy.flow.ployee.home.content.availability.contract.AvailabilityViewModel;
import com.nos.ploy.flow.ployee.home.content.availability.contract.NormalItemAvailabilityVM;
import com.nos.ploy.flow.ployee.home.content.availability.contract.WeekAvailabilityVM;
import com.nos.ploy.utils.RecyclerAdapterUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 21/12/2559.
 */

public class AvailabilityRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AvailabilityViewModel> mDatas = new ArrayList<>();

    public AvailabilityRecyclerAdapter() {
    }

    public void replaceData(List<AvailabilityViewModel> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public List<PloyeeAvailiabilityGson.Data.AvailabilityItem> gatheredData(){
        List<PloyeeAvailiabilityGson.Data.AvailabilityItem> results = new ArrayList<>();
        if(RecyclerAdapterUtils.getSize(mDatas) > 0){
            for(AvailabilityViewModel vm : mDatas){
                if(vm instanceof NormalItemAvailabilityVM){
                    results.add(((NormalItemAvailabilityVM) vm).getData());
                }
            }
        }

        return results;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, @AvailabilityViewModel.ViewType int viewType) {
        switch (viewType) {
            case AvailabilityViewModel.NORMAL:
                return new NormalVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_availability_item, parent, false));
            case AvailabilityViewModel.WEEK:
                return new WeekVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_availability_days, parent, false));
            case AvailabilityViewModel.NONE:
            default:
                return new RecyclerView.ViewHolder(new Space(parent.getContext())) {
                };
        }
    }

    @Override
    public
    @AvailabilityViewModel.ViewType
    int getItemViewType(int position) {
        if (RecyclerAdapterUtils.isAvailableData(mDatas, position)) {
            return mDatas.get(position).getViewType();
        }
        return AvailabilityViewModel.NONE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!RecyclerAdapterUtils.isAvailableData(mDatas, position)) {
            return;
        }
        AvailabilityViewModel vm = mDatas.get(position);
        @AvailabilityViewModel.ViewType int viewType = getItemViewType(position);
        switch (viewType) {
            case AvailabilityViewModel.NORMAL:
                if (holder instanceof NormalVH && vm instanceof NormalItemAvailabilityVM) {
                    bindNormal((NormalVH) holder, (NormalItemAvailabilityVM) vm);
                }
                break;
            case AvailabilityViewModel.WEEK:
//                if (holder instanceof WeekVH && vm instanceof WeekAvailabilityVM) {
//
//                }
                break;
            case AvailabilityViewModel.NONE:
            default:
                break;
        }
    }

    private void bindNormal(NormalVH holder, NormalItemAvailabilityVM vm) {
        PloyeeAvailiabilityGson.Data.AvailabilityItem data = vm.getData();
        if (null != data) {
            holder.tvMon.setActivated(data.isMon());
            holder.tvTues.setActivated(data.isTues());
            holder.tvWednes.setActivated(data.isWed());
            holder.tvThurs.setActivated(data.isThurs());
            holder.tvFri.setActivated(data.isFri());
            holder.tvSatur.setActivated(data.isSat());
            holder.tvSun.setActivated(data.isSun());
            holder.tvDuration.setText(data.getDurationValue());
        }
    }


    @Override
    public int getItemCount() {
        return RecyclerAdapterUtils.getSize(mDatas);
    }

    public static class NormalVH extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_availability_item_monday)
        public TextView tvMon;
        @BindView(R.id.textview_availability_item_tuesday)
        public TextView tvTues;
        @BindView(R.id.textview_availability_item_wednesday)
        public TextView tvWednes;
        @BindView(R.id.textview_availability_item_thursday)
        public TextView tvThurs;
        @BindView(R.id.textview_availability_item_friday)
        public TextView tvFri;
        @BindView(R.id.textview_availability_item_saturday)
        public TextView tvSatur;
        @BindView(R.id.textview_availability_item_sunday)
        public TextView tvSun;
        @BindView(R.id.textview_availability_item_duration)
        public TextView tvDuration;

        public NormalVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class WeekVH extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_availability_day_monday)
        public TextView tvMon;
        @BindView(R.id.textview_availability_day_tuesday)
        public TextView tvTues;
        @BindView(R.id.textview_availability_day_wednesday)
        public TextView tvWednes;
        @BindView(R.id.textview_availability_day_thursday)
        public TextView tvThurs;
        @BindView(R.id.textview_availability_day_friday)
        public TextView tvFri;
        @BindView(R.id.textview_availability_day_saturday)
        public TextView tvSatur;
        @BindView(R.id.textview_availability_day_sunday)
        public TextView tvSun;

        public WeekVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
