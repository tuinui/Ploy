package com.nos.ploy.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.nos.ploy.R;
import com.nos.ploy.utils.FragmentTransactionUtils;

/**
 * Created by User on 1/10/2559.
 */

public abstract class BaseFragment extends AppCompatDialogFragment {

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

    protected String getStringCompat(@StringRes int id) {
        if (isActivityReady() && getResources() != null) {
            return super.getString(id);
        } else {
            return "";
        }
    }
}
