package com.nos.ploy.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nos.ploy.R;
import com.nos.ploy.api.base.RetrofitManager;
import com.nos.ploy.utils.FragmentTransactionUtils;

import retrofit2.Retrofit;

/**
 * Created by User on 1/10/2559.
 */

public abstract class BaseFragment extends AppCompatDialogFragment {

    private ProgressDialog mProgressDialog;
    protected static final String KEY_USER_ID = "USER_ID";

    protected boolean isDialog() {
        return false;
    }

    /**
     * @return true if consume event / false if do nothing
     */
    public boolean onBackPressed() {
        return false;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new AppCompatDialog(getContext(), R.style.AppTheme_Light) {
            @Override
            public void onBackPressed() {
                if (!BaseFragment.this.onBackPressed()) {
                    super.onBackPressed();
                }
            }
        };
        if (null != dialog.getWindow()) {
            dialog.getWindow()
                    .getAttributes().windowAnimations = R.style.DialogAnimation;
        }
        return dialog;
    }

    @Override
    public void dismiss() {
        if (isReadyForFragmentTransaction() && null != getFragmentManager()) {
            super.dismiss();
        }
    }

    protected Retrofit getRetrofit() {
        return RetrofitManager.getRetrofit(getContext());
    }

    protected void enableBackButton(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFragment.this.dismiss();
            }
        });
    }

    protected void runOnUiThread(Runnable action) {
        FragmentActivity activity = getActivity();
        if (null != activity && !activity.isFinishing()) {
            activity.runOnUiThread(action);
        }
    }
    protected void finishThisActivity() {
        if (isActivityReady()) {
            ActivityCompat.finishAfterTransition(getActivity());
        }
    }
    protected boolean isActivityReady() {
        return null != getActivity() && !getActivity().isFinishing() && !isActivityDestroyed();
    }

    public boolean isReadyForFragmentTransaction() {
        return isActivityReady() && isResumed() && isAdded();
    }

    public boolean isActivityDestroyed() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).isActivityDestroyedCompat();
        } else {
            return false;
        }
    }

    protected void showFragment(BaseFragment fragment) {
        FragmentTransactionUtils.showFragment(this, fragment);
    }

    protected void showToast(String message) {
        showToast(null, message);
    }

    protected void showToast(@StringRes int res) {
        showToast(null, getStringCompat(res));
    }

    protected void showToast(View v, String message) {
        if (null != getActivity() && !getActivity().isFinishing() && isAdded()) {
            Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
            if (null != v && null != v.getContext()) {
                toast.setView(v);
            }
            toast.show();
        }
    }

    protected void showLoading() {
        if (!isActivityReady()) {
            return;
        }
        if (null == mProgressDialog) {
            mProgressDialog = getLoadingProgressDialog(getContext());
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


    protected void disableEditable(TextView view) {
        view.setFocusable(false);
        view.setKeyListener(null);
    }

    protected void disableEditText(EditText view) {
        view.setFocusable(false);
        view.setEnabled(false);
        view.setKeyListener(null);
    }

    protected void disableEditableAllChild(View v) {
        if (v instanceof EditText) {
            disableEditText((EditText) v);
            return;
        }
        findAllChildViewAndDisableEditable(v);
    }

    protected void disableEditTextAllChild(View v) {
        if (v instanceof EditText) {
            disableEditText((EditText) v);
            return;
        }
        findAllChildViewAndDisableEdittext(v);
    }

    private void findAllChildViewAndDisableEdittext(View v) {
        if (null == v || !(v instanceof ViewGroup)) {
            return;
        }

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);

            if (null == child) {
                continue;
            }

            if (child instanceof EditText) {
                disableEditText((EditText) child);
            } else if (child instanceof ViewGroup) {
                findAllChildViewAndDisableEdittext(child);
            }
        }
    }


    private void findAllChildViewAndDisableEditable(View v) {
        if (null == v || !(v instanceof ViewGroup)) {
            return;
        }

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);

            if (null == child) {
                continue;
            }

            if (child instanceof EditText) {
                disableEditable((EditText) child);
            } else if (child instanceof ViewGroup) {
                findAllChildViewAndDisableEditable(child);
            }
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
