package com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Space;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel.HeaderSubServiceVM;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel.NormalSubServiceVM;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel.PloyeeServiceDetailSubServiceItemBaseViewModel;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel.SpaceFullSpanSubServiceVM;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel.SpaceOneElementSpanSubServiceVM;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 1/12/2559.
 */

public class PloyeeServiceDetailSubServiceRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final boolean mDisableMode;
    private List<PloyeeServiceDetailSubServiceItemBaseViewModel> mDatas = new ArrayList<>();

    public PloyeeServiceDetailSubServiceRecyclerAdapter() {
        this.mDisableMode = false;
    }

    public PloyeeServiceDetailSubServiceRecyclerAdapter(boolean disableMode) {
        this.mDisableMode = disableMode;
    }

    public void replaceData(List<PloyerServiceDetailGson.Data.SubService> datas) {
        mDatas.clear();
        mDatas.addAll(toVM(datas));
        notifyDataSetChanged();
    }

    public List<Long> gatheredSubServiceIds() {
        List<Long> results = new ArrayList<>();
        if (RecyclerUtils.getSize(mDatas) > 0) {
            for (PloyeeServiceDetailSubServiceItemBaseViewModel vm : mDatas) {
                if (vm instanceof NormalSubServiceVM) {
                    NormalSubServiceVM data = ((NormalSubServiceVM) vm);
                    if (data.isChecked()) {
                        results.add(data.getData().getSubServiceLv2Id());
                    }
                }
            }
        }
        return results;
    }

    private List<PloyeeServiceDetailSubServiceItemBaseViewModel> toVM(List<PloyerServiceDetailGson.Data.SubService> datas) {
        List<PloyeeServiceDetailSubServiceItemBaseViewModel> results = new ArrayList<>();
        for (PloyerServiceDetailGson.Data.SubService data : datas) {
            PloyerServiceDetailGson.Data.SubService.SubServiceLv1 subServiceLv1 = data.getSubServiceLV1();
            if (subServiceLv1 != null) {
                results.add(new HeaderSubServiceVM(subServiceLv1));
            }
            List<PloyerServiceDetailGson.Data.SubService.SubServiceLv2> subServicesLv2 = data.getSubServiceLV2();
            for (PloyerServiceDetailGson.Data.SubService.SubServiceLv2 sub2 : subServicesLv2) {
                results.add(new NormalSubServiceVM(sub2));
            }
            if (subServicesLv2.size() % 2 != 0) {
                results.add(new SpaceOneElementSpanSubServiceVM());
            }
        }
        results.add(new SpaceFullSpanSubServiceVM());
        return results;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, @PloyeeServiceDetailSubServiceItemBaseViewModel.ViewType int viewType) {
        switch (viewType) {
            case PloyeeServiceDetailSubServiceItemBaseViewModel.HEADER: {
                return new HeaderVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_subservice_item_header, parent, false));
            }
            case PloyeeServiceDetailSubServiceItemBaseViewModel.ITEM: {
                return new NormalVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_subservice_item, parent, false), mDisableMode);
            }
            case PloyeeServiceDetailSubServiceItemBaseViewModel.SPACE_ONE_ELEMENT:
                return new SpaceVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_subservice_space_item, parent, false));
            case PloyeeServiceDetailSubServiceItemBaseViewModel.NONE:
            case PloyeeServiceDetailSubServiceItemBaseViewModel.SPACE_FULL:

                return new SpaceVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_space_item, parent, false));
            default:
                return new RecyclerView.ViewHolder(new Space(parent.getContext())) {
                };
        }
    }

    @Override
    public
    @PloyeeServiceDetailSubServiceItemBaseViewModel.ViewType
    int getItemViewType(int position) {
        if (RecyclerUtils.isAvailableData(mDatas, position)) {
            return mDatas.get(position).getViewType();
        }
        return PloyeeServiceDetailSubServiceItemBaseViewModel.NONE;
    }

    public int getNormalItemCount() {
        int count = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            if (getItemViewType(i) == PloyeeServiceDetailSubServiceItemBaseViewModel.ITEM) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (RecyclerUtils.isAvailableData(mDatas, position)) {
            @PloyeeServiceDetailSubServiceItemBaseViewModel.ViewType int viewType = getItemViewType(position);
            PloyeeServiceDetailSubServiceItemBaseViewModel data = mDatas.get(position);
            switch (viewType) {
                case PloyeeServiceDetailSubServiceItemBaseViewModel.HEADER:
                    bindHeader((HeaderSubServiceVM) data, (HeaderVH) holder);
                    break;
                case PloyeeServiceDetailSubServiceItemBaseViewModel.ITEM:
                    bindNormal((NormalSubServiceVM) data, (NormalVH) holder);
                    break;
                case PloyeeServiceDetailSubServiceItemBaseViewModel.NONE:
                    break;
                case PloyeeServiceDetailSubServiceItemBaseViewModel.SPACE_FULL:
                    break;
            }
        }
    }

    private void bindNormal(final NormalSubServiceVM data, NormalVH holder) {

        holder.radioSubService.setOnCheckedChangeListener(null);
        holder.radioSubService.setChecked(data.isChecked());
        if (!mDisableMode) {
            holder.radioSubService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    data.setChecked(isChecked);
                }
            });
        }

        holder.radioSubService.setText(data.getName());
    }

    private void bindHeader(HeaderSubServiceVM data, HeaderVH holder) {
        holder.tvHeader.setText(data.getName());
    }

    @Override
    public int getItemCount() {
        return RecyclerUtils.getSize(mDatas);
    }

    public static class HeaderVH extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_subservice_item_header)
        public TextView tvHeader;

        public HeaderVH(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public static class SpaceVH extends RecyclerView.ViewHolder {

        public SpaceVH(View itemView) {
            super(itemView);
        }
    }

    public static class NormalVH extends RecyclerView.ViewHolder {
        @BindView(R.id.radiobutton_subservice_item)
        public RadioButton radioSubService;

        public NormalVH(View v, boolean disableMode) {
            super(v);
            ButterKnife.bind(this, v);
            if (disableMode) {
                radioSubService.setButtonDrawable(null);
                radioSubService.setClickable(false);
//                radioSubService.setEnabled(false);
            }
        }
    }
}
