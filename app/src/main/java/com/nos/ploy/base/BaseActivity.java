package com.nos.ploy.base;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.nos.ploy.R;

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
        if(isReady()){
            ActivityCompat.finishAfterTransition(this);
        }
    }

    protected void goToActivity(Class<? extends Activity> activityClass){

    }

    protected void enableBackButton(Toolbar toolbar){
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishThisActivity();
            }
        });
    }
}
