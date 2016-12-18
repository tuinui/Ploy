package com.nos.ploy.base;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.nos.ploy.DrawerController;
import com.nos.ploy.R;
import com.nos.ploy.api.utils.loader.AccountGsonLoader;
import com.nos.ploy.utils.FragmentTransactionUtils;

import java.util.Map;

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

    public boolean isReady() {
        return null != this && !isFinishing() && !isActivityDestroyedCompat();
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

    private
    @DrawerController.Menu
    int toMenu(String menu) {
        Map<Integer, String> map = DrawerController.MAP_MENU_NAMES;
        for (Integer index : map.keySet()) {
            String menuString = map.get(index);
            if (TextUtils.equals(menu, menuString)) {
                return index;
            }
        }
        return DrawerController.NONE;
    }


    protected void addFragmentToActivity(BaseFragment fragment, @IdRes int containerId) {
        addFragmentToActivity(fragment, containerId, null);
    }


    protected void addFragmentToActivity(BaseFragment fragment, @IdRes int containerId, String tag) {
        if (isReady() && null != getSupportFragmentManager()) {
            FragmentTransactionUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, containerId, tag);
        }
    }

    public void showFragment(BaseFragment baseFragment) {
        FragmentTransactionUtils.showFragment(this, baseFragment);
    }

    public void showFragment(BaseFragment baseFragment, String tag) {
        FragmentTransactionUtils.showFragment(this, baseFragment, tag);
    }
}
