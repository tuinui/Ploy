package com.nos.ploy.base;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.nos.ploy.DrawerController;
import com.nos.ploy.R;
import com.nos.ploy.base.drawer.DrawerRecyclerAdapter;
import com.nos.ploy.utils.FragmentTransactionUtils;

/**
 * Created by User on 1/10/2559.
 */

public class BaseActivity extends LocalizationActivity {

    private boolean isActivityDestroyed = false;


    @Override
    protected void onDestroy() {
        isActivityDestroyed = true;
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        isActivityDestroyed = false;
    }

    public boolean isActivityDestroyedCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return isDestroyed();
        } else {
            return isActivityDestroyed;
        }
    }

    protected boolean isReady() {
        return !isFinishing() && !isActivityDestroyedCompat();
    }

    protected void finishThisActivity() {
        if (isReady()) {
            ActivityCompat.finishAfterTransition(this);
        }
    }

    protected void goToActivity(Class<? extends Activity> activityClass) {

    }

    protected void enableBackButton(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishThisActivity();
            }
        });
    }

    protected void initDrawer(final DrawerLayout drawerLayout, RecyclerView mRecyclerViewDrawer, Toolbar toolbar, View toggleButton) {
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        DrawerRecyclerAdapter adapter = new DrawerRecyclerAdapter() {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                String menu = DrawerController.MAP_MENU_NAMES.get(position);
                holder.tvItem.setText(menu);
            }

            @Override
            public int getItemCount() {
                return DrawerController.MAP_MENU_NAMES.size();
            }
        };
        mRecyclerViewDrawer.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewDrawer.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDrawer(drawerLayout);
            }
        });
        mRecyclerViewDrawer.setAdapter(adapter);
    }

    private void toggleDrawer(DrawerLayout dl) {
        if (null != dl) {
            if (dl.isDrawerOpen(GravityCompat.END)) {
                closeDrawer(dl);
            } else {
                openDrawer(dl);
            }
        }
    }

    private void openDrawer(DrawerLayout dl) {
        if (null != dl) {
            dl.openDrawer(GravityCompat.END);
        }
    }

    private void closeDrawer(DrawerLayout dl) {
        if (null != dl) {
            dl.closeDrawers();
        }
    }

    protected void addFragmentToActivity(BaseFragment fragment, @IdRes int containerId) {
        addFragmentToActivity(fragment, containerId, null);
    }


    protected void addFragmentToActivity(BaseFragment fragment, @IdRes int containerId, String tag) {
        if (isReady() && null != getSupportFragmentManager()) {
            FragmentTransactionUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, containerId, tag);
        }
    }

    protected void showFragment(BaseFragment baseFragment) {
        FragmentTransactionUtils.showFragment(this, baseFragment);
    }

    protected void showFragment(BaseFragment baseFragment, String tag) {
        FragmentTransactionUtils.showFragment(this, baseFragment, tag);
    }
}
