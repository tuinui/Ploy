package com.nos.ploy.flow.ployee.home.content.availability;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployee.home.content.availability.contract.AvailabilityDummyData;
import com.nos.ploy.flow.ployee.home.content.availability.view.PloyeeAvailabilityRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 22/11/2559.
 */

public class PloyeeAvailabilityFragment extends BaseFragment {
    @BindView(R.id.recyclerview_ployee_availability_time_table)
    RecyclerView mRecyclerViewTimeTable;

    public static PloyeeAvailabilityFragment newInstance() {
        Bundle args = new Bundle();
        PloyeeAvailabilityFragment fragment = new PloyeeAvailabilityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_availability, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerViewTimeTable.setLayoutManager(new GridLayoutManager(mRecyclerViewTimeTable.getContext(), 8));
        mRecyclerViewTimeTable.setAdapter(new PloyeeAvailabilityRecyclerAdapter(AvailabilityDummyData.getDummyData()));
    }


}
