package com.nos.ploy.flow.ployee.home.content.availability.view;

import android.support.design.internal.ForegroundLinearLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.flow.ployee.home.content.availability.contract.AvailabilityViewModel;
import com.nos.ploy.flow.ployee.home.content.availability.contract.HeaderDayAvailabilityVM;
import com.nos.ploy.flow.ployee.home.content.availability.contract.HeaderTimeAvailabilityVM;
import com.nos.ploy.flow.ployee.home.content.availability.contract.NormalItemAvailabilityVM;
import com.nos.ploy.utils.RecyclerAdapterUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 22/11/2559.
 */

public class PloyeeAvailabilityRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<AvailabilityViewModel> mDatas = new ArrayList<>();

    public PloyeeAvailabilityRecyclerAdapter(List<AvailabilityViewModel> models) {
        mDatas.clear();
        mDatas.addAll(models);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @AvailabilityViewModel.ViewType int type = viewType;
        switch (type) {
            case AvailabilityViewModel.HEADER_DAY:
                return new HeaderVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ployee_availability_time_table_label_item, parent, false));
            case AvailabilityViewModel.HEADER_TIME:
                return new HeaderVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ployee_availability_time_table_label_item, parent, false));
            case AvailabilityViewModel.ITEM:
                return new NormalVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ployee_availability_time_table_item, parent, false));
            case AvailabilityViewModel.NONE:
            default:
                break;
        }
        return new RecyclerView.ViewHolder(new View(parent.getContext())) {
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (RecyclerAdapterUtils.isAvailableData(mDatas, position)) {
            return mDatas.get(position).getAvailibilityViewType();
        }

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        @AvailabilityViewModel.ViewType int viewType = getItemViewType(position);
        if (RecyclerAdapterUtils.isAvailableData(mDatas, position)) {
            AvailabilityViewModel data = mDatas.get(position);
            switch (viewType) {

                case AvailabilityViewModel.HEADER_DAY:
                    bindHeaderDay(holder, data);
                    break;
                case AvailabilityViewModel.HEADER_TIME:
                    bindHeaderTime(holder, data);
                    break;
                case AvailabilityViewModel.ITEM:
                    bindNormalItem(holder, data);
                    break;
                case AvailabilityViewModel.NONE:
                default:
                    break;
            }
        }
    }

    private void bindHeaderDay(RecyclerView.ViewHolder uncastHolder, AvailabilityViewModel uncastData) {
        if (null != uncastHolder && null != uncastData && uncastHolder instanceof HeaderVH && uncastData instanceof HeaderDayAvailabilityVM) {
            HeaderDayAvailabilityVM data = (HeaderDayAvailabilityVM) uncastData;
            HeaderVH holder = (HeaderVH) uncastHolder;
            holder.tvLabel.setText(data.getDay());
            holder.tvLabel.setBackgroundResource(android.R.color.holo_blue_dark);
        }
    }

    private void bindHeaderTime(RecyclerView.ViewHolder uncastHolder, AvailabilityViewModel uncastData) {
        if (null != uncastData && null != uncastHolder && uncastHolder instanceof HeaderVH && uncastData instanceof HeaderTimeAvailabilityVM) {
            HeaderTimeAvailabilityVM data = (HeaderTimeAvailabilityVM) uncastData;
            HeaderVH holder = (HeaderVH) uncastHolder;
            holder.tvLabel.setText(data.getTimeRange());
            holder.tvLabel.setBackgroundResource(android.R.color.white);
        }
    }

    private void bindNormalItem(RecyclerView.ViewHolder uncastHolder, AvailabilityViewModel uncastData) {
        if (null != uncastData && null != uncastHolder && uncastHolder instanceof NormalVH && uncastData instanceof NormalItemAvailabilityVM) {
            NormalItemAvailabilityVM data = (NormalItemAvailabilityVM) uncastData;
            NormalVH holder = (NormalVH) uncastHolder;
            if (data.isChecked()) {
                holder.buttonAvailability.setBackgroundResource(R.drawable.com_facebook_button_icon_blue);
            } else {
                holder.buttonAvailability.setBackgroundDrawable(null);
            }

        }
    }

    public class NormalVH extends RecyclerView.ViewHolder {
        @BindView(R.id.linearlayout_ployee_availability_time_table_item)
        ForegroundLinearLayout buttonAvailability;

        public NormalVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class HeaderVH extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_ployee_availability_time_table_label_item)
        TextView tvLabel;

        public HeaderVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
