package com.nos.ploy.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.nos.ploy.DrawerController;
import com.nos.ploy.R;
import com.nos.ploy.api.base.RetrofitManager;
import com.nos.ploy.api.utils.loader.AccountGsonLoader;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.utils.FragmentTransactionUtils;

import java.util.Map;

import retrofit2.Retrofit;

/**
 * Created by User on 1/10/2559.
 */

public class BaseActivity extends LocalizationActivity {

    private boolean isActivityDestroyed = false;
    private ProgressDialog mProgressDialog;
    protected Long mUserId;


    @Override
    protected void onDestroy() {
        isActivityDestroyed = true;
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(UserTokenManager.isLogin(this)){
            mUserId = UserTokenManager.getToken(this).getUserId();
        }

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

    protected void showLoading() {
        if (!isActivityReady()) {
            return;
        }
        if (null == mProgressDialog) {
            mProgressDialog = getLoadingProgressDialog(this);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    protected void dismissLoading() {
        if (!isActivityReady()) {
            return;
        }

        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private ProgressDialog getLoadingProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);

        dialog.setTitle("");
        dialog.setMessage(getStringCompat(R.string.Loading));
        dialog.setCancelable(true);
        return dialog;
    }

    protected String getStringCompat(@StringRes int id) {
        if (isActivityReady() && getResources() != null) {
            return super.getString(id);
        } else {
            return "";
        }
    }
    protected Retrofit getRetrofit() {
        return RetrofitManager.getRetrofit(this);
    }

    protected boolean isActivityReady() {
        return !isFinishing() && !isActivityDestroyedCompat();
    }
}
