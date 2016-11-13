package com.nos.ploy.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nos.ploy.R;

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
}
