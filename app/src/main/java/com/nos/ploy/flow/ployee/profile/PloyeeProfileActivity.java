package com.nos.ploy.flow.ployee.profile;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.nos.ploy.R;
import com.nos.ploy.api.account.AccountApi;
import com.nos.ploy.api.account.model.ProfileGson;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.custom.view.WorkAroundMapFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Saran on 15/12/2559.
 */

public class PloyeeProfileActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {
    @BindView(R.id.button_ployee_profile_show_email)
    Button mButtonEmail;
    @BindView(R.id.edittext_ployee_profile_about_me)
    MaterialEditText mEditTextAboutMe;
    @BindView(R.id.edittext_ployee_profile_education)
    MaterialEditText mEditTextEducation;
    @BindView(R.id.edittext_ployee_profile_work)
    MaterialEditText mEditTextProfileWork;
    @BindView(R.id.edittext_ployee_profile_interest)
    MaterialEditText mEditTextInterest;
    @BindView(R.id.textview_ployee_profile_language_support)
    TextView mTExtViewLanguageSupport;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindString(R.string.Profile)
    String LProfile;
    @BindView(R.id.scrollview_ployee_profile)
    NestedScrollView mScrollView;
    @BindView(R.id.sliderlayout_ployee_profile_image)
    SliderLayout mSliderLayout;
    @BindView(R.id.swiperefreshlayout_ployee_profile)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.cardview_ployee_profile_image_container)
    CardView mCardViewImageContainer;
    private GoogleMap mMap;
    private WorkAroundMapFragment mMapFragment;
    private AccountApi mApi;
    private ProfileGson.Data mData;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };

    private RetrofitCallUtils.RetrofitCallback<ProfileGson> mCallbackLoadData = new RetrofitCallUtils.RetrofitCallback<ProfileGson>() {
        @Override
        public void onDataSuccess(ProfileGson data) {
            if (null != data && null != data.getData()) {
                bindData(data.getData());
            }
        }

        @Override
        public void onDataFailure(String failCause) {

        }
    };


    private void bindData(ProfileGson.Data data) {
        mData = data;
        if (null != data) {
            mEditTextAboutMe.setText(data.getAboutMe());
            mEditTextEducation.setText(data.getEducation());
            mEditTextInterest.setText(data.getInterest());
            mEditTextProfileWork.setText(data.getWork());
        }

    }

    private void refreshData() {
        RetrofitCallUtils.with(mApi.getProfileGson(mUserId), mCallbackLoadData);
        refreshSlider();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ployee_profile);
        ButterKnife.bind(this);
        mMapFragment = (WorkAroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment_ployee_profile);
        mApi = getRetrofit().create(AccountApi.class);
        initMap();
        initToolbar();
        initView();
    }


    private Action1<List<ProfileImageGson.Data>> mOnLoadProfileImageFinish = new Action1<List<ProfileImageGson.Data>>() {
        @Override
        public void call(final List<ProfileImageGson.Data> datas) {
            if (null != datas) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (ProfileImageGson.Data image : datas) {
                            DefaultSliderView sliderView = new DefaultSliderView(PloyeeProfileActivity.this);
                            sliderView.image(image.getImagePath());
                            mSliderLayout.addSlider(sliderView);
                        }
                    }
                });
            }
        }
    };

    private void refreshSlider() {
        AccountInfoLoader.getProfileImage(this, mUserId, false, mOnLoadProfileImageFinish);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (null == mData) {
            refreshData();
        }
    }

    private void initToolbar() {
        mToolbar.setTitle(LProfile);
        enableBackButton(mToolbar);
        mToolbar.inflateMenu(R.menu.menu_done);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_done_item_done) {
                    onClickDone();
                }
                return false;
            }


        });
    }

    private void onClickDone() {

    }

    private void initView() {
        mButtonEmail.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSliderLayout.setOnClickListener(this);
        mCardViewImageContainer.setOnClickListener(this);
        mSliderLayout.stopAutoCycle();

    }

    private void initMap() {
        if (null != mMapFragment) {
            mMapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMapFragment.setListener(new WorkAroundMapFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mButtonEmail.getId()) {
            mButtonEmail.setActivated(!mButtonEmail.isActivated());
        } else if (id == mSliderLayout.getId()) {
            Toast.makeText(this, "a;sldkfj;laskdfj,", Toast.LENGTH_LONG).show();
        }else if(id == mCardViewImageContainer.getId()){
            Toast.makeText(this, "a;sldkfj;laskdfj,", Toast.LENGTH_LONG).show();
        }
    }
}
