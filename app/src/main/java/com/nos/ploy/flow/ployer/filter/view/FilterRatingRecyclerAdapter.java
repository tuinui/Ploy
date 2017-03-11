package com.nos.ploy.flow.ployer.filter.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nos.ploy.R;
import com.nos.ploy.utils.LanguageFormatter;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Saran on 14/1/2560.
 */

public class FilterRatingRecyclerAdapter extends RecyclerView.Adapter<FilterRatingRecyclerAdapter.ViewHolder> {


    private final Action1<Long> onRatingChangeListener;
    private List<FilterRatingViewModel> mDatas = new ArrayList<>();
    private long mCurrentRating = -404;

    public FilterRatingRecyclerAdapter(Action1<Long> onRatingChange) {
        super();
        mDatas.add(new FilterRatingViewModel(1));
        mDatas.add(new FilterRatingViewModel(2));
        mDatas.add(new FilterRatingViewModel(3));
        mDatas.add(new FilterRatingViewModel(4));
        mDatas.add(new FilterRatingViewModel(5));
        this.onRatingChangeListener = onRatingChange;
    }

    public void onClickRating(Long data) {
        notifyDataSetChanged();
        if (mCurrentRating == data) {
            mCurrentRating = -404;
        } else {
            mCurrentRating = data;
        }


        onRatingChangeListener.call(mCurrentRating);


        for (FilterRatingViewModel vm : mDatas) {
            if (mCurrentRating == -404) {
                vm.setChecked(false);
            } else {
                if (vm.getRating() >= mCurrentRating) {
                    vm.setChecked(true);
                } else {
                    vm.setChecked(false);
                }
            }

        }
        notifyDataSetChanged();
    }

    public void replaceData(Long data) {
        notifyDataSetChanged();
        if (null == data) {
            mCurrentRating = -404;
        } else {
            mCurrentRating = data;
        }
        for (FilterRatingViewModel vm : mDatas) {
            if (mCurrentRating == -404) {
                vm.setChecked(false);
            } else {
                if (vm.getRating() >= mCurrentRating) {
                    vm.setChecked(true);
                } else {
                    vm.setChecked(false);
                }
            }

        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_filter_rating_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (RecyclerUtils.isAvailableData(mDatas, position)) {
            holder.bindData(mDatas.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return RecyclerUtils.getSize(mDatas);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.button_filter_rating_item)
        Button btnRating;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public void bindData(FilterRatingViewModel data) {
            if (null != data) {
                btnRating.setActivated(data.isChecked());
                btnRating.setText(LanguageFormatter.formatLong(data.getRating()));
                btnRating.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == btnRating.getId()) {
                if (RecyclerUtils.isAvailableData(mDatas, getAdapterPosition())) {
                    FilterRatingViewModel data = mDatas.get(getAdapterPosition());
                    onClickRating(data.getRating());
                }
            }
        }
    }


}
