package com.nos.ploy.flow.ployee.home.content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployee.home.view.PloyeeHomeRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 19/11/2559.
 */

public class PloyeeHomeListFragment extends BaseFragment {


    @BindView(R.id.recyclerview_ployee_home_main)
    RecyclerView mRecyclerView;


    public static PloyeeHomeListFragment newInstance() {
        Bundle args = new Bundle();
        PloyeeHomeListFragment fragment = new PloyeeHomeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private PloyeeHomeRecyclerAdapter mAdapter = new PloyeeHomeRecyclerAdapter() {
        @Override
        public void onBindViewHolder(PloyeeHomeRecyclerAdapter.ViewHolder holder, int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showFragment(PloyeeServiceFragment.newInstance());
                }
            });
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_home_main, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerview(mRecyclerView);
    }

    private void initRecyclerview(RecyclerView recyclerView) {
        if (null == recyclerView) {
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }
}
