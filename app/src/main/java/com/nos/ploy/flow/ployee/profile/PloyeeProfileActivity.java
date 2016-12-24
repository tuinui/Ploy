package com.nos.ploy.flow.ployee.profile;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nos.ploy.R;
import com.nos.ploy.api.account.AccountApi;
import com.nos.ploy.api.account.model.ProfileGson;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.ResponseMessage;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.flow.ployee.profile.upload.UploadPhotoFragment;
import com.nos.ploy.utils.FragmentTransactionUtils;
import com.nos.ploy.utils.GoogleApiAvailabilityUtils;
import com.nos.ploy.utils.MyLocationUtils;
import com.nos.ploy.utils.RecyclerUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Saran on 15/12/2559.
 */

public class PloyeeProfileActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, ViewPager.OnPageChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.button_ployee_profile_show_email)
    Button mButtonEmail;
    @BindView(R.id.button_ployee_profile_show_phone_no)
    Button mButtonPhone;
    @BindView(R.id.textview_ployee_profile_address)
    TextView mTextViewAddress;
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
    @BindView(R.id.linearlayout_profile_image_slider_counts_dot)
    LinearLayout mLinearLayoutDotsContainer;
    @BindView(R.id.fab_ployee_profile_image)
    FloatingActionButton mFabProfileImage;
    //    @BindView(R.id.sliderlayout_ployee_profile_image)
//    SliderLayout mSliderLayout;
    @BindView(R.id.viewpager_profile_image_slider)
    ViewPager mViewPagerSlider;
    @BindView(R.id.swiperefreshlayout_ployee_profile)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.imagebutton_ployee_profile_checkin)
    ImageButton mImageButtonCheckin;
    @BindDrawable(R.drawable.nonselecteditem_dot)
    Drawable mDrawableNonSelecteddot;
    @BindDrawable(R.drawable.selecteditem_dot)
    Drawable mDrawableSelectedDot;

    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
    private AccountApi mApi;
    private ProfileGson.Data mData;

    private boolean shouldRequestCallSave = false;

    private LocationListener mLocationManagerListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private RetrofitCallUtils.RetrofitCallback<ProfileGson> mCallbackLoadData = new RetrofitCallUtils.RetrofitCallback<ProfileGson>() {
        @Override
        public void onDataSuccess(ProfileGson data) {
            dismissLoading();
            dismissRefreshing();
            if (null != data && null != data.getData()) {
                bindData(data.getData());
            }
        }

        @Override
        public void onDataFailure(ResponseMessage failCause) {
            dismissLoading();
            dismissRefreshing();
            if (TextUtils.equals(failCause.getMessageCode(), ResponseMessage.CODE_DATA_NOT_FOUND)) {
                shouldRequestCallSave = true;
            }

        }
    };
    private ImageSliderPagerAdapter mAdapter;
    private int mDotsCount;
    private List<ImageView> mImageViewDots = new ArrayList<>();
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;


    private void bindData(ProfileGson.Data data) {
        mData = data;
        if (null != data) {
            mEditTextAboutMe.setText(data.getAboutMe());
            mEditTextEducation.setText(data.getEducation());
            mEditTextInterest.setText(data.getInterest());
            mEditTextProfileWork.setText(data.getWork());
            mButtonEmail.setActivated(data.isContactEmail());
            mButtonPhone.setActivated(data.isContactPhone());
        }

    }

    private void refreshData(Context context) {
        showRefreshing();
        RetrofitCallUtils.with(mApi.getProfileGson(mUserId), mCallbackLoadData).enqueue(context);
        refreshSlider();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ployee_profile);
        ButterKnife.bind(this);
        mApi = getRetrofit().create(AccountApi.class);
        initToolbar();
        initView();
        initSlider();
        if (GoogleApiAvailabilityUtils.checkPlayServices(this)) {
            mGoogleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        }

    }

    private void initLocationManager() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, mLocationManagerListener, null);
    }

    private void initSlider() {
        mAdapter = new ImageSliderPagerAdapter(this);
        mViewPagerSlider.setAdapter(mAdapter);
        mViewPagerSlider.setCurrentItem(0);
        mViewPagerSlider.addOnPageChangeListener(this);

    }

    private void setUiPageViewController() {
        mLinearLayoutDotsContainer.removeAllViews();
        mDotsCount = mAdapter.getCount();
        if (mDotsCount > 0) {
            mImageViewDots.clear();


            for (int i = 0; i < mDotsCount; i++) {
                ImageView img = new ImageView(this);
                img.setImageDrawable(mDrawableNonSelecteddot);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);
                mLinearLayoutDotsContainer.addView(img, params);
                mImageViewDots.add(img);


            }
            if (RecyclerUtils.isAvailableData(mImageViewDots, 0)) {
                mImageViewDots.get(0).setImageDrawable(mDrawableSelectedDot);
            }

        }

    }


    private Action1<List<ProfileImageGson.Data>> mOnLoadProfileImageFinish = new Action1<List<ProfileImageGson.Data>>() {
        @Override
        public void call(final List<ProfileImageGson.Data> datas) {
            if (null != datas) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.replaceData(datas);
                        setUiPageViewController();
                    }
                });
            }
        }
    };

    private void refreshSlider() {
        AccountInfoLoader.getProfileImage(this, mUserId, true, mOnLoadProfileImageFinish);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != mGoogleApiClient) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (null != mGoogleApiClient) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null == mData) {
            refreshData(PloyeeProfileActivity.this);
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
        showRefreshing();
        if (shouldRequestCallSave) {
            RetrofitCallUtils.with(mApi.postSaveProfileGson(gatheredData()), mCallbackLoadData).enqueue(this);
        } else {
            RetrofitCallUtils.with(mApi.postUpdateProfileGson(gatheredData()), mCallbackLoadData).enqueue(this);
        }
    }

    private ProfileGson.Data gatheredData() {
        ProfileGson.Data data;
        if (null != mData) {
            data = mData.cloneThis();
        } else {
            data = new ProfileGson.Data();
        }
        data.setAboutMe(extractString(mEditTextAboutMe));
        data.setEducation(extractString(mEditTextEducation));
        data.setInterest(extractString(mEditTextInterest));
        data.setUserId(mUserId);
        data.setWork(extractString(mEditTextProfileWork));
        data.setContactEmail(mButtonEmail.isActivated());
        data.setContactPhone(mButtonPhone.isActivated());
        return data;
    }

    private void initView() {
        mButtonEmail.setOnClickListener(this);
        mButtonPhone.setOnClickListener(this);
        mImageButtonCheckin.setOnClickListener(this);
        setRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(mSwipeRefreshLayout.getContext());
            }
        });
//        mSliderLayout.setOnClickListener(this);
        mFabProfileImage.setOnClickListener(this);
//        mSliderLayout.stopAutoCycle();

    }

    private void initMap() {
        FragmentTransactionUtils.addFragmentToActivity(getSupportFragmentManager(), mMapFragment, R.id.framelayout_ployee_profile_maps_container);
        if (null != mMapFragment) {
            mMapFragment.getMapAsync(this);
            if (null != mMapFragment.getView()) {
                mMapFragment.getView().setClickable(false);
            }

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (mGoogleMap != null) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            moveCameraToCurrentLocation();
        }
    }

    private void moveCameraToCurrentLocation() {
        if (mGoogleMap != null && null != mGoogleApiClient) {
            MyLocationUtils.getLastKnownLocation(this, mGoogleApiClient, new Action1<Location>() {
                @Override
                public void call(Location location) {
                    if(null != location){
                        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

                        mGoogleMap.addMarker(new MarkerOptions().position(latlng));
                    }

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mButtonEmail.getId()) {
            mButtonEmail.setActivated(!mButtonEmail.isActivated());
        } else if (id == mButtonPhone.getId()) {
            mButtonPhone.setActivated(!mButtonPhone.isActivated());
        } else if (id == mFabProfileImage.getId()) {
            showUploadPhotoFragment();
        } else if (id == mImageButtonCheckin.getId()) {
            checkInAndShowCurrentAddress();
        }
    }


    private void checkInAndShowCurrentAddress() {
        MyLocationUtils.getLastKnownLocation(this, mGoogleApiClient, true, new Action1<Location>() {
            @Override
            public void call(Location location) {
                if(null != location){
                    moveCameraToCurrentLocation();
                    String address = MyLocationUtils.getCompleteAddressString(PloyeeProfileActivity.this, location.getLatitude(), location.getLongitude());
                    mTextViewAddress.setText(address);
                }

            }
        });

    }

    private void dummyData() {
        mEditTextProfileWork.setText("workๆๆๆๆ");
        mEditTextInterest.setText("ก้สนใจยู่อิอิ");
        mEditTextEducation.setText("เอดูดุ้000");
        mEditTextAboutMe.setText("หล่อ");
    }

    private void showUploadPhotoFragment() {
        AccountInfoLoader.getProfileImage(this, mUserId, false, new Action1<List<ProfileImageGson.Data>>() {
                    @Override
                    public void call(List<ProfileImageGson.Data> datas) {
                        showFragment(UploadPhotoFragment.newInstance(mUserId, new ArrayList<>(datas), new UploadPhotoFragment.OnDataChangeListener() {
                            @Override
                            public void onDataChange() {
                                refreshSlider();
                            }
                        }));
                    }
                }
        );
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mDotsCount; i++) {
            if (RecyclerUtils.isAvailableData(mImageViewDots, i)) {
                mImageViewDots.get(i).setImageDrawable(mDrawableNonSelecteddot);
            }
        }
        if (RecyclerUtils.isAvailableData(mImageViewDots, position)) {
            mImageViewDots.get(position).setImageDrawable(mDrawableSelectedDot);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initMap();
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
