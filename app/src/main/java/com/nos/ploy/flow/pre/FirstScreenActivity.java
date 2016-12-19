package com.nos.ploy.flow.pre;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.flow.ployee.home.PloyeeHomeActivity;
import com.nos.ploy.flow.ployer.home.PloyerHomeActivity;
import com.nos.ploy.flow.pre.register.SignInSignupActivity;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.KeyHashUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 10/11/2559.
 */

public class FirstScreenActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.button_first_screen_search_services)
    Button mButtonSearchServices;
    @BindView(R.id.button_first_screen_search_jobs)
    Button mButtonSearchJobs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        ButterKnife.bind(this);
        mButtonSearchJobs.setOnClickListener(this);
        mButtonSearchServices.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mButtonSearchJobs.getId()) {
            if (UserTokenManager.isLogin(this)) {
                IntentUtils.startActivity(this, PloyeeHomeActivity.class);
            } else {
                IntentUtils.startActivity(this, SignInSignupActivity.class);
            }

        } else if (id == mButtonSearchServices.getId()) {
            IntentUtils.startActivity(this, PloyerHomeActivity.class);
        }
    }
}
