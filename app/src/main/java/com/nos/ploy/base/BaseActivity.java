package com.nos.ploy.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.nos.ploy.DrawerController;
import com.nos.ploy.R;
import com.nos.ploy.api.base.RetrofitManager;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.cache.LanguageAppLabelManager;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.utils.FragmentTransactionUtils;
import com.nos.ploy.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import rx.functions.Action1;

/**
 * Created by User on 1/10/2559.
 */

public class BaseActivity extends LocalizationActivity {

    protected Long mUserId;
    private boolean isActivityDestroyed = false;
    private ProgressDialog mProgressDialog;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean isRefreshing;
    protected LanguageAppLabelGson.Data mLanguageData;

    protected void bindLanguage(LanguageAppLabelGson.Data data) {

    }

    @Override
    protected void onDestroy() {
        isActivityDestroyed = true;
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LanguageAppLabelManager.getLanguageLabel(BaseActivity.this, new Action1<LanguageAppLabelGson.Data>() {
                    @Override
                    public void call(LanguageAppLabelGson.Data data) {
                        if (null != data) {
                            mLanguageData = data;
                            bindLanguage(data);
                            changeRightMenuLabel(data, DrawerController.PLOYEE_MENUS);
                            changeRightMenuLabel(data, DrawerController.PLOYER_MENUS);
                        }
                    }
                });
            }
        });
    }

    protected List<EditText> getAllEditTexts(View v) {
        if (null == v || !(v instanceof ViewGroup)) {
            return null;
        }

        ViewGroup vg = (ViewGroup) v;
        List<EditText> editTextList = new ArrayList<>();
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);

            if (null == child) {
                continue;
            }

            if (child instanceof EditText) {
                editTextList.add((EditText) child);
            } else if (child instanceof ViewGroup) {
                editTextList.addAll(getAllEditTexts(child));
            }
        }
        return editTextList;
    }

    private void changeRightMenuLabel(LanguageAppLabelGson.Data data, List<DrawerController.DrawerMenuItem> items) {
        if (null != items) {
            for (DrawerController.DrawerMenuItem item : items) {
                @DrawerController.Menu int menu = item.getMenu();
                switch (menu) {

                    case DrawerController.ACCOUNT:
                        item.setMenuTitle(data.sidemenuAccount);
                        break;
                    case DrawerController.INTRODUCTION:
                        item.setMenuTitle(data.sidemenuIntro);
                        break;
                    case DrawerController.NONE:
                        break;
                    case DrawerController.SETTINGS:
                        item.setMenuTitle(data.sidemenuSetting);
                        break;
                    case DrawerController.WHAT_IS_PLOYEE:
                        item.setMenuTitle("What is ployee");
                        break;
                    case DrawerController.WHAT_IS_PLOYER:
                        item.setMenuTitle("What is ployer");
                        break;
                }
            }
        }
    }


    protected boolean isRefreshing(){
        return isRefreshing;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserTokenManager.isLogin(this)) {
            mUserId = UserTokenManager.getToken(this).getUserId();
        } else {
            mUserId = -404L;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        NetworkUtils.isDeviceOnline(this);
        isActivityDestroyed = false;
    }

    public boolean isActivityDestroyedCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return isDestroyed();
        } else {
            return isActivityDestroyed;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        enableBackButton(toolbar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });
    }

    protected void enableBackButton(Toolbar toolbar, View.OnClickListener onNavigationClick) {
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_40dp);


        toolbar.setNavigationOnClickListener(onNavigationClick);

    }

//    private
//    @DrawerController.Menu
//    int toMenu(String menu) {
//        Map<Integer, String> map = DrawerController.PLOYEE_MENUS;
//        for (Integer index : map.keySet()) {
//            String menuString = map.get(index);
//            if (TextUtils.equals(menu, menuString)) {
//                return index;
//            }
//        }
//        return DrawerController.NONE;
//    }


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

    protected void showToastLong(String message) {
        if (isReady()) {
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    protected void showToastShort(String message) {
        if (isReady()) {
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            toast.show();
        }
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

    protected void showRefreshing() {
        if (mRefreshLayout == null) {
//            showLoading();
            return;
        }
        if (!isRefreshing) {
            isRefreshing = true;
            mRefreshLayout.setRefreshing(true);
        }
    }

    protected void setRefreshLayout(SwipeRefreshLayout swipeRefreshLayout, final SwipeRefreshLayout.OnRefreshListener listener) {
        if (null == swipeRefreshLayout) {
            return;
        }
        this.mRefreshLayout = swipeRefreshLayout;
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                listener.onRefresh();
            }
        });
    }

    protected void dismissRefreshing() {
        if (mRefreshLayout == null) {
            dismissLoading();
            return;
        }
        if (isRefreshing) {
            isRefreshing = false;
            mRefreshLayout.setRefreshing(false);
        }
    }

    protected String extractString(EditText editText) {
        if (null != editText && null != editText.getText() && !TextUtils.isEmpty(editText.getText())) {
            return editText.getText().toString();
        } else {
            return "";
        }
    }

    protected long extractLong(EditText editText) {
        if (null != editText && null != editText.getText() && !TextUtils.isEmpty(editText.getText())) {
            String text = editText.getText().toString();
            try {
                return Long.parseLong(text);
            } catch (NumberFormatException e) {
                return 0;
            }
        } else {
            return 0;
        }
    }


    protected double extractDouble(EditText editText) {
        if (null != editText && null != editText.getText() && !TextUtils.isEmpty(editText.getText())) {
            String text = editText.getText().toString();
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException e) {
                if (!TextUtils.isEmpty(text) && text.contains(",")) {
                    text = String.valueOf(TextUtils.replace(text, new String[]{","}, new String[]{""}));
                    return extractDouble(text);
                } else {
                    return -404;
                }
            }
        } else {
            return -404;
        }
    }

    protected void runOnUiThread(final Action1<Context> action){
        if(isReady()){
            super.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(null != action){
                        action.call(BaseActivity.this);
                    }
                }
            });
        }
    }

    protected double extractDouble(String s) {
        if (!TextUtils.isEmpty(s)) {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
