package com.nos.ploy.flow.ployer.ployeelist;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.flow.ployer.ployeelist.view.PloyeeListRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 10/11/2559.
 */

public class PloyeeListActivity extends BaseActivity {
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview_ployee_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.imageview_main_footer_more)
    ImageView mImageViewMore;
    @BindView(R.id.viewstub_main_appbar_searchview)
    ViewStub mStubSearchView;
    private SearchView mSearchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ployee_list);
        ButterKnife.bind(this);
        mSearchView = (SearchView) mStubSearchView.inflate().findViewById(R.id.searchview_main);
        initToolbar(mToolbar);
        initRecyclerView(mRecyclerView);
    }

    private void initRecyclerView(RecyclerView recyclerview) {
        recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerview.setAdapter(new PloyeeListRecyclerAdapter() {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });
    }


    private void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("Aide au Senior");
        toolbar.setSubtitle("999 Ployees");
        enableBackButton(toolbar);
        toolbar.inflateMenu(R.menu.menu_ployer_home);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_ployee_list_filter) {

                    return true;
                } else if (id == R.id.menu_ployee_list_maps) {

                    return true;
                }
                return false;
            }
        });
    }
}
