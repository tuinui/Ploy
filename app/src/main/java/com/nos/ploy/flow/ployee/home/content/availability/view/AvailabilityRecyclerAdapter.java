package com.nos.ploy.flow.ployee.home.content.availability.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;
import com.nos.ploy.flow.ployee.home.content.availability.contract.AvailabilityViewModel;
import com.nos.ploy.flow.ployee.home.content.availability.contract.NormalItemAvailabilityVM;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Saran on 21/12/2559.
 */

public class AvailabilityRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Action1<Void> mOnContentChangeListener;
    private List<AvailabilityViewModel> mDatas = new ArrayList<>();
    private LanguageAppLabelGson.Data language;


    public AvailabilityRecyclerAdapter(Action1<Void> onContentChangeListener) {
        this.mOnContentChangeListener = onContentChangeListener;
    }

    public void replaceData(List<AvailabilityViewModel> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public ArrayList<PloyeeAvailiabilityGson.Data.AvailabilityItem> gatheredData() {
        ArrayList<PloyeeAvailiabilityGson.Data.AvailabilityItem> results = new ArrayList<>();
        if (RecyclerUtils.getSize(mDatas) > 0) {
            for (AvailabilityViewModel vm : mDatas) {
                if (vm instanceof NormalItemAvailabilityVM) {
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
        if (RecyclerUtils.isAvailableData(mDatas, position)) {
            return mDatas.get(position).getViewType();
        }
        return AvailabilityViewModel.NONE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!RecyclerUtils.isAvailableData(mDatas, position)) {
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
                bindWeek((WeekVH) holder);
                break;
            case AvailabilityViewModel.NONE:
            default:
                break;
        }
    }

    private void bindWeek(final WeekVH holder) {
        if (null != holder && null != language) {
            holder.tvMon.setText(language.avaliabilityScreenMonday);
            holder.tvTues.setText(language.avaliabilityScreenTuesday);
            holder.tvWednes.setText(language.avaliabilityScreenWednesday);
            holder.tvThurs.setText(language.avaliabilityScreenThursday);
            holder.tvFri.setText(language.avaliabilityScreenFriday);
            holder.tvSatur.setText(language.avaliabilityScreenSaturday);
            holder.tvSun.setText(language.avaliabilityScreenSunday);
        }
    }

    private void onContentChange(){
        if(mOnContentChangeListener != null){
            mOnContentChangeListener.call(null);
        }
    }

    private void bindNormal(final NormalVH holder, NormalItemAvailabilityVM vm) {
        final PloyeeAvailiabilityGson.Data.AvailabilityItem data = vm.getData();
        if (null != data) {
            holder.tvMon.setActivated(data.isMon());
            holder.tvTues.setActivated(data.isTues());
            holder.tvWednes.setActivated(data.isWed());
            holder.tvThurs.setActivated(data.isThurs());
            holder.tvFri.setActivated(data.isFri());
            holder.tvSatur.setActivated(data.isSat());
            holder.tvSun.setActivated(data.isSun());
            holder.tvDuration.setText(data.getDurationValue());

            holder.tvMon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.setMon(!data.isMon());
                    holder.tvMon.setActivated(data.isMon());
                    onContentChange();
                }
            });

            holder.tvTues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.setTues(!data.isTues());
                    holder.tvTues.setActivated(data.isTues());
                    onContentChange();
                }
            });

            holder.tvWednes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.setWed(!data.isWed());
                    holder.tvWednes.setActivated(data.isWed());
                    onContentChange();
                }
            });

            holder.tvThurs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.setThurs(!data.isThurs());
                    holder.tvThurs.setActivated(data.isThurs());
                    onContentChange();
                }
            });

            holder.tvFri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.setFri(!data.isFri());
                    holder.tvFri.setActivated(data.isFri());
                    onContentChange();
                }
            });

            holder.tvSatur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.setSat(!data.isSat());
                    holder.tvSatur.setActivated(data.isSat());
                    onContentChange();
                }
            });

            holder.tvSun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.setSun(!data.isSun());
                    holder.tvSun.setActivated(data.isSun());
                    onContentChange();
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return RecyclerUtils.getSize(mDatas);
    }

    public void setLanguage(LanguageAppLabelGson.Data language) {
        this.language = language;
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

    public class WeekVH extends RecyclerView.ViewHolder {
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
