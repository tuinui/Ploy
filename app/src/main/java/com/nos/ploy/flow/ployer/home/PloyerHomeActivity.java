package com.nos.ploy.flow.ployer.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.flow.ployer.home.view.PloyeeCategoryRecyclerAdapter;
import com.nos.ploy.flow.ployer.ployeelist.PloyeeListActivity;
import com.nos.ploy.utils.IntentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PloyerHomeActivity extends BaseActivity {
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview_ployer_home)
    RecyclerView mRecyclerView;
    @BindView(R.id.imageview_main_footer_more)
    ImageView mImageViewMore;
    @BindView(R.id.viewstub_main_appbar_searchview)
    ViewStub mStubSearchView;
    private SearchView mSearchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ployer_home);
        ButterKnife.bind(this);
        mSearchView = (SearchView) mStubSearchView.inflate().findViewById(R.id.searchview_main);
        initToolbar(mToolbar);
        initRecyclerView(mRecyclerView);
    }

    private void initRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(new PloyeeCategoryRecyclerAdapter() {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentUtils.startActivity(PloyerHomeActivity.this, PloyeeListActivity.class);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });

    }

    private void initToolbar(Toolbar toolbar) {
        toolbar.setTitle(R.string.Ployer);
        enableBackButton(toolbar);

    }
}
