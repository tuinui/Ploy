package com.nos.ploy.flow.ployee.home.content.service.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.flow.generic.PloyeeServiceItemViewModelFilter;
import com.nos.ploy.flow.ployee.home.content.service.list.viewmodel.PloyeeServiceItemViewModel;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by User on 11/11/2559.
 */

public class PloyeeHomeRecyclerAdapter extends RecyclerView.Adapter<PloyeeHomeRecyclerAdapter.ViewHolder> {
    private final Action1<PloyeeServiceItemViewModel> mActionOnClick;
    private List<PloyeeServiceItemViewModel> mFilteredData = new ArrayList<>();
    private List<PloyeeServiceItemViewModel> mOriginalDatas = new ArrayList<>();
    private PloyeeServiceItemViewModelFilter filter;

    public PloyeeHomeRecyclerAdapter(Action1<PloyeeServiceItemViewModel> actionOnClick) {
        this.mActionOnClick = actionOnClick;
        filter = new PloyeeServiceItemViewModelFilter(mOriginalDatas, this);
    }


    public void setFilteredData(List<PloyeeServiceItemViewModel> mFilteredData) {
        this.mFilteredData.clear();
        this.mFilteredData.addAll(mFilteredData);
    }

    public void filterList(String text) {
        filter.filter(text);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_textview_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (RecyclerUtils.isAvailableData(mFilteredData, position)) {
            final PloyeeServiceItemViewModel vm = mFilteredData.get(position);


            if (vm.isSelected()){
                holder.viewMark.setVisibility(View.VISIBLE);
            }else{
                holder.viewMark.setVisibility(View.INVISIBLE);
            }

            holder.tvTitle.setText(vm.getText());
            holder.tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (RecyclerUtils.isAvailableData(mFilteredData, holder.getAdapterPosition())) {
                        PloyeeServiceItemViewModel vm = mFilteredData.get(holder.getAdapterPosition());
                        if (null != mActionOnClick) {
                            mActionOnClick.call(vm);
                        }
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return RecyclerUtils.getSize(mFilteredData);
    }

    public void replaceData(List<PloyeeServiceItemViewModel> datas) {
        mOriginalDatas.clear();
        mFilteredData.clear();
        notifyDataSetChanged();
        mOriginalDatas.addAll(datas);
        mFilteredData.addAll(datas);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public View viewMark;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.textview_list_item);
            viewMark =  itemView.findViewById(R.id.viewMark);

        }


    }
}
