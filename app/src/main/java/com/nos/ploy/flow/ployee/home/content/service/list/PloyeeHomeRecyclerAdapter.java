package com.nos.ploy.flow.ployee.home.content.service.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.nos.ploy.R;
import com.nos.ploy.flow.ployee.home.content.service.list.viewmodel.PloyeeServiceItemViewModel;

import java.util.Comparator;

import rx.functions.Action1;

/**
 * Created by User on 11/11/2559.
 */

public class PloyeeHomeRecyclerAdapter extends SortedListAdapter<PloyeeServiceItemViewModel> {
    private final Action1<PloyeeServiceItemViewModel> mActionOnClick;

    public PloyeeHomeRecyclerAdapter(Context context, Comparator<PloyeeServiceItemViewModel> comparator, Action1<PloyeeServiceItemViewModel> actionOnClick) {
        super(context, PloyeeServiceItemViewModel.class, comparator);
        this.mActionOnClick = actionOnClick;
    }

    @Override
    protected SortedListAdapter.ViewHolder<? extends PloyeeServiceItemViewModel> onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_textview_list_item, parent, false));
    }

    @Override
    protected boolean areItemsTheSame(PloyeeServiceItemViewModel item1, PloyeeServiceItemViewModel item2) {
        return item1.getId() == item2.getId();
    }

    @Override
    protected boolean areItemContentsTheSame(PloyeeServiceItemViewModel oldItem, PloyeeServiceItemViewModel newItem) {
        return oldItem.equals(newItem);
    }

    public class ViewHolder extends SortedListAdapter.ViewHolder<PloyeeServiceItemViewModel> {
        public TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.textview_list_item);
        }

        @Override
        protected void performBind(final PloyeeServiceItemViewModel vm) {
            tvTitle.setText(vm.getText());
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mActionOnClick) {
                        mActionOnClick.call(vm);
                    }
                }
            });
        }
    }
}
