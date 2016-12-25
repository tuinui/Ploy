package com.nos.ploy.flow.generic;

import android.os.Bundle;
import android.os.Handler;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.KeyHashUtils;

/**
 * Created by Saran on 19/12/2559.
 */

public class SplashScreenActivity extends BaseActivity {

    private long SPLASH_TIME_OUT = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        KeyHashUtils.logKeyHash(this);
        new Handler().postDelayed(new Runnable() {

         /*
          * Showing splash screen with a timer. This will be useful when you
          * want to show case your app logo / company
          */

            @Override
            public void run() {
                IntentUtils.startActivity(SplashScreenActivity.this, FirstScreenActivity.class);

                // close this activity
                finishThisActivity();
            }
        }, SPLASH_TIME_OUT);
    }
}
